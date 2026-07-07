package cn.violin.wiki.security;

import cn.violin.core.iam.CheckResult;
import cn.violin.core.iam.IamClient;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Local short-TTL cache for {@code IamClient.checkPermission(...)} lookups so the wiki
 * service doesn't hammer IAM on every call.
 *
 * <p>The cache key is {@code (userId|customerId|action|resource|resourceName)}. TTL is small
 * (default 30 s) by design — IAM remains the authoritative answer; the cache only absorbs
 * bursts. Revocation is still effective within one TTL window.</p>
 *
 * <p>This is a defensive latency optimization; correctness does not depend on it.</p>
 *
 * <p>Only constructed when {@code violin.iam.service-token-secret} is configured, so a
 * wiki deployment without IAM service credentials boots cleanly without instantiating
 * {@link ServiceTokenSigner} (which fails-fast on missing secret).</p>
 */
@Component
@ConditionalOnProperty(name = "violin.iam.service-token-secret", matchIfMissing = false)
@Slf4j
public class WikiPermissionCache {

    private final IamClient iamClient;
    private final Cache<String, CheckResult> cache;

    public WikiPermissionCache(
            IamClient iamClient,
            @Value("${violin.wiki.permission.cache.seconds:30}") long ttlSeconds,
            @Value("${violin.wiki.permission.cache.max-size:10000}") long maxSize) {
        this.iamClient = iamClient;
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(ttlSeconds))
                .maximumSize(maxSize)
                .build();
        log.info("WikiPermissionCache ready: ttl={}s, maxSize={}", ttlSeconds, maxSize);
    }

    public CheckResult check(String userId,
                             String customerId,
                             String action,
                             String resource,
                             String resourceName) {
        String key = userId + "|" + customerId + "|" + action + "|" + resource + "|" + resourceName;
        CheckResult cached = cache.getIfPresent(key);
        if (cached != null) {
            return cached;
        }
        CheckResult fresh = iamClient.checkPermission(userId, customerId, action, resource, resourceName);
        if (fresh != null) {
            cache.put(key, fresh);
        }
        return fresh;
    }
}
