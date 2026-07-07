package cn.violin.wiki.config;

import cn.violin.common.context.RequestContext;
import cn.violin.core.customer.CustomerHandler;
import org.springframework.stereotype.Component;

/**
 * Wiki-side {@link CustomerHandler} for MyBatis Plus tenant filtering.
 *
 * <p>Falls back from verified to hinted {@code customerId} so the wiki service remains
 * usable while IAM has not yet exposed a verified binding endpoint per user. The verified
 * value is preferred when present so RBAC-graded pages route through the SAR-verified
 * tenant. Once {@code violin-iam} exposes the per-sub binding endpoint (M4), the hint
 * fallback can be tightened to verified-only.</p>
 *
 * <p>All wiki tables use {@code customer_id} as the multi-tenant column.</p>
 */
@Component
public class WikiCustomerHandler implements CustomerHandler {

    @Override
    public String getCustomerId() {
        String verified = RequestContext.getVerifiedCustomerId();
        if (verified != null && !verified.isBlank()) {
            return verified;
        }
        String hint = RequestContext.getCustomerId();
        return hint == null ? "" : hint;
    }

    @Override
    public String getCustomerIdColumn() {
        return "customer_id";
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // All wiki-owned tables are tenant-isolated.
        return false;
    }
}
