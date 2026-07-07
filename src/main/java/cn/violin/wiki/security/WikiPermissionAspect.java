package cn.violin.wiki.security;

import cn.violin.common.context.RequestContext;
import cn.violin.common.exception.PermissionDeniedException;
import cn.violin.common.exception.UnauthorizedException;
import cn.violin.core.aspect.AspectOrders;
import cn.violin.core.iam.CheckResult;
import cn.violin.wiki.annotation.RequirePermission;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Wiki-side RBAC aspect.
 *
 * <p>Resolves {@code @RequirePermission} via the {@link WikiPermissionCache}, which itself
 * talks to IAM through {@code IamClient.checkPermission(...)}. Because the call is
 * service-to-service, IAM marks the {@code customerVerified} flag as {@code true} and
 * gates the binding end-to-end.</p>
 *
 * <p>Soft-launch via {@code violin.wiki.permission.enabled} (default {@code false}).
 * When disabled the aspect is a no-op so existing flows keep working until the IAM
 * policy bindings are populated; flip the flag once the {@code wiki:&lt;resource&gt;:&lt;verb&gt;}
 * rules are in place in IAM and the aspect will start enforcing.</p>
 */
@Aspect
@Component
@org.springframework.boot.autoconfigure.condition.ConditionalOnBean(WikiPermissionCache.class)
@Slf4j
@Order(AspectOrders.PERMISSION)
public class WikiPermissionAspect {

    private final WikiPermissionCache cache;
    private final boolean permissionEnabled;

    public WikiPermissionAspect(
            WikiPermissionCache cache,
            @Value("${violin.wiki.permission.enabled:false}") boolean permissionEnabled) {
        this.cache = cache;
        this.permissionEnabled = permissionEnabled;
        log.info("WikiPermissionAspect: enabled={}", permissionEnabled);
    }

    @Around("@annotation(RequirePermission)")
    public Object check(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!permissionEnabled) {
            return joinPoint.proceed();
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequirePermission annotation = method.getAnnotation(RequirePermission.class);

        String userSub = RequestContext.getUserId();
        if (userSub == null || userSub.isBlank()) {
            throw new UnauthorizedException();
        }

        String verified = RequestContext.getVerifiedCustomerId();
        if (verified == null) {
            log.warn("permission gate denied: customerId is not verified for user={}; "
                    + "wiki requires SAR-verified tenant scope before any RBAC grant", userSub);
            throw new UnauthorizedException(
                    "customerId is not verified; tenant scope cannot be enforced");
        }

        String action = annotation.apiGroup() + ":" + annotation.resource() + ":" + annotation.verb();
        CheckResult result = cache.check(
                userSub, verified, action, annotation.resource(), annotation.verb());

        if (result == null || !result.isAllowed()) {
            log.warn("permission denied: user={}, {}:{}:{} - {}",
                    userSub, annotation.apiGroup(), annotation.resource(), annotation.verb(),
                    result == null ? "null" : result.getReason());
            throw new PermissionDeniedException("common.forbidden",
                    new Object[]{result == null ? "no response" : result.getReason()});
        }

        return joinPoint.proceed();
    }
}
