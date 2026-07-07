package cn.violin.wiki.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a controller method as requiring a verified IAM RBAC grant.
 *
 * <p>Wiki re-declares this annotation locally so the wiki JAR does not have to depend
 * on {@code violin-iam} classes. The matching {@code WikiPermissionAspect} resolves the
 * grant via {@code IamClient.checkPermission(...)} against
 * {@code POST {iam}/api/v1/internal/check-permission}.</p>
 *
 * <p>Defaults to {@code apiGroup="wiki"} so most call sites only specify
 * {@code resource} and {@code verb}.</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {

    String apiGroup() default "wiki";

    String resource();

    String verb();
}
