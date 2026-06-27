# violin-wiki

Wiki / 笔记服务（文章 + 分类管理）。

## 技术栈

- Spring Boot 3.5 + Java 17
- PostgreSQL + Flyway（migration 位于 `db/migration-wiki/V110__init_wiki.sql`）
- MyBatis Plus（多租户 + 软删除由拦截器自动处理）
- 依赖 [violin-core](../violin-core)（JWT 鉴权、RBAC、Customer 拦截器等基础设施）

## 业务模块

| URL | 说明 |
|---|---|
| `GET/POST/PUT/DELETE /api/v1/essays` | 文章 CRUD |
| `POST /api/v1/essays/sort` | 文章排序（按分类） |
| `GET/POST/PUT/DELETE /api/v1/wiki-categories` | Wiki 分类 CRUD |
| `POST /api/v1/wiki-categories/sort` | 分类排序 |

## 数据库

- 业务表：`essays` / `wiki_categories`（snake_case + 审计字段 + `deleted_at` 软删除）
- 多租户隔离：每张业务表有 `customer_id` 列，violin-core 拦截器自动加 `WHERE customer_id = ?`
- 共享 `core` 表（在 `public` schema）：`t_user` / `t_customer` / `t_role` / `t_policy_rule` / `t_role_policy` / `t_role_binding` / `t_resource_type`

## 本地启动

```powershell
$env:DB_PASSWORD = "postgres"
$env:VIOLIN_JWT_SECRET = "violin-local-secret-key-32bytes-aaaaaa!"
mvn -f E:\Project\violin-wiki spring-boot:run "-Dspring-boot.run.profiles=local"
```

端口：`8081` + context-path `/wiki` → API 前缀 `http://localhost:8081/wiki/api/v1/...`

## K8s 部署

- Namespace：`violin-dev` / `violin-prod`
- 镜像：`registry.violin-work.online/violin-wiki:2.1`
- 详见 `example/`（待补：configmap / secret 模板）