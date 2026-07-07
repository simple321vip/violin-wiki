# AGENT.md — violin-wiki

> Engineering notes for the violin-wiki microservice. **Code is the source of truth**;
> any conflict with this document means the code wins. Update this file when adding
> new conventions, gotchas, or design intent.

---

## Scope

violin-wiki is a **business subdomain service** of the violin microservice suite: it
manages user-authored wiki content (articles + categories) under multi-tenant
isolation. It does **not** own authentication, customer provisioning, or policy
storage — those belong to `violin-iam` / `violin-customer` and are reached through
the shared framework in `violin-core`.

Deployment: K8s, single pod, context-path `/wiki`, public-facing at
`https://wiki.violin-work.online`.

---

## Boot rules (v2.1 alignment)

Inherited from the violin-iam engineering retrospective; do not skip:

- **`@SpringBootApplication` MUST declare
  `scanBasePackages = {"cn.violin"}`** so framework beans in `cn.violin.core.*`
  (interceptors, resolvers, message source, etc.) are picked up.
- **`@MapperScan("cn.violin.*.mapper")` on the application class** keeps MyBatis
  from treating framework interfaces like `JwksClient` as mapper proxies.
- **`maven-compiler-plugin` must declare a Lombok
  `<annotationProcessorPaths>` block** — otherwise `@Data` / `@Builder` silently
  produces no methods and the context fails to wire.
- **JWT verification is RS256 + JWKS only.** `violin.jwt.jwks-url` must be set in
  every profile (default points at `http://localhost:8080/violin-iam/.well-known/jwks.json`).
  There is **no** HS256 silent fallback and `violin.jwt.secret` is intentionally
  not a configurable key.
- **Token claims are informational only.** The `customerId` JWT claim, when
  present, is recorded into `RequestContext` as a *hint* by
  `AuthenticationInterceptor`. Tenant-scoped reads still have to look up the
  authoritative binding from the database; do **not** treat the hint as an
  authorization decision. See "Design notes" below.

---

## Configuration layout

| File | Purpose |
|---|---|
| `application.yml` | Shared defaults. JWT fields default to `violin-iam` issuer / `violin-services` audience. |
| `application-local.yml` | Standalone debug: dev workstation Postgres (`192.168.3.49` / `violin_local` / `wiki_local`), snowflake worker-id 110, SQL trace. |
| `application-dev.yml` | Shared dev: K8s-injected env vars, snowflake worker-id 12. |
| `application-prod.yml` | Production: env-driven everything, snowflake worker-id 22. |

`VIOLIN_JWT_JWKS_URL` is the one env-var you absolutely must align with the IAM
deployment. local: `http://localhost:8080/violin-iam/.well-known/jwks.json`,
dev/prod: cross-cluster IAM `/.well-known/jwks.json` endpoint.

---

## Domain design notes

### Authentication boundary

`AuthenticationInterceptor` validates the bearer token before the controller
runs and writes two pieces of state into `RequestContext`:

- `userId` — required; the JWT subject is the only reliable identity.
- `customerId` (hint only) — best-effort, taken from the `customerId` claim
  when present.

`Service` implementations read both. For business writes that pin a row to a
customer (e.g. `EssayEntity.customerId`), we currently snapshot the hint at
write time as an **audit label** — not as a security gate. Any future
authorization model must look up the live binding through `violin-iam` SAR or
read `t_user.customer_id` directly; the JWT claim never alone.

### Multi-tenant filtering

Wiki uses MyBatis Plus multi-tenant + soft-delete interceptors from
`violin-core` automatically. Service methods therefore don't need to plumb
`customer_id` through query wrappers. The interceptor matches the hint
seeded by `AuthenticationInterceptor` against the column, and soft-deletes
(`@TableLogic deleted_at`) are enforced consistently.

### Sort APIs

`POST /api/v1/essays/sort` and `POST /api/v1/wiki-categories/sort` rewrite the
`sort_order` column in one transaction. Both reject empty input, dedupe
duplicates, and assert the affected-row count equals the input length so we
catch missing targets (`COMMON_OPERATION_FAILED`).

---

## Conventions

- **English only** in code (javadoc, log messages, exception messages, comments).
- **Records + `@Builder` for DTOs.** `EssayResult` / `CategoryResult` use
  Lombok `@Builder` to keep the static `from(entity)` factory ergonomic.
- **Soft delete via `@TableLogic` on `deleted_at`**; never call
  `deleteById(...)` against a base entity — let MP rewrite the column.
- **`@RestControllerAdvice`** extends `GlobalExceptionHandler` from
  `violin-core`. Override the constructor only if you need to inject a
  specific `MessageSource`; otherwise the default auto-wires the project
  message bundles.

---

## Local smoke check (no PG dependency)

There is no `@SpringBootTest` yet because there is no test source set. If you
want to verify boot:

```powershell
# 1. Build common/core once
mvn -f E:\Project\violin-common clean install -DskipTests
mvn -f E:\Project\violin-core clean install -DskipTests

# 2. Compile wiki
mvn -f E:\Project\violin-wiki compile -q

# 3. (Optional) full test
mvn -f E:\Project\violin-wiki test    # currently no tests; expect "No tests to run"
```

Boot up the service yourself:

```powershell
$env:DB_PASSWORD = "postgres"
$env:VIOLIN_JWT_JWKS_URL = "http://localhost:8080/violin-iam/.well-known/jwks.json"
mvn -f E:\Project\violin-wiki spring-boot:run "-Dspring-boot.run.profiles=local"
```

---

## Related projects

- `violin-common`, `violin-core` — framework dependencies.
- `violin-iam` — token issuer; source of truth for the JWKS endpoint this
  service verifies against.
- `violin-customer` (former `violin-auth`, customer-only after the 2.1 split) —
  provisions the `t_customer` rows referenced by wiki's `customer_id`.
- `violin-bookmark`, `violin-calendar` — sibling business services, same shape.
