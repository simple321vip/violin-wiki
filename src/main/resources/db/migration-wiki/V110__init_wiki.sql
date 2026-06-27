-- ============================================================
-- V110__init_wiki.sql — violin-wiki 初始 schema
-- ============================================================
-- 业界标准：
--   - 表名复数（essays / wiki_categories）
--   - 列名 snake_case
--   - customer_id 隔离（业务表）
--   - 审计字段 + 软删除（@TableLogic 基于 deleted_at）
--   - 外键约束
-- ============================================================

CREATE TABLE IF NOT EXISTS wiki_categories (
    id           VARCHAR(36)    PRIMARY KEY,
    customer_id  VARCHAR(36)    NOT NULL,
    category_name VARCHAR(128)  NOT NULL,
    sort_order   INTEGER        NOT NULL DEFAULT 0,

    created_by   VARCHAR(64),
    created_at   TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by   VARCHAR(64),
    updated_at   TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at   TIMESTAMPTZ
);

CREATE INDEX IF NOT EXISTS idx_wiki_categories_customer ON wiki_categories (customer_id);
CREATE INDEX IF NOT EXISTS idx_wiki_categories_alive    ON wiki_categories (customer_id) WHERE deleted_at IS NULL;

CREATE TABLE IF NOT EXISTS essays (
    id           VARCHAR(36)    PRIMARY KEY,
    customer_id  VARCHAR(36)    NOT NULL,
    category_id  VARCHAR(36)    NOT NULL,
    title        VARCHAR(255),
    content      TEXT,
    sort_order   INTEGER        NOT NULL DEFAULT 0,

    created_by   VARCHAR(64),
    created_at   TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by   VARCHAR(64),
    updated_at   TIMESTAMPTZ    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at   TIMESTAMPTZ,

    CONSTRAINT fk_essays_category
        FOREIGN KEY (category_id) REFERENCES wiki_categories (id)
        ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_essays_customer ON essays (customer_id);
CREATE INDEX IF NOT EXISTS idx_essays_category ON essays (category_id);
CREATE INDEX IF NOT EXISTS idx_essays_alive    ON essays (customer_id) WHERE deleted_at IS NULL;

COMMENT ON TABLE  wiki_categories IS 'Wiki 分类';
COMMENT ON TABLE  essays           IS 'Wiki 文章';
COMMENT ON COLUMN essays.customer_id IS '所属客户 ID（多租户隔离）';
COMMENT ON COLUMN essays.deleted_at  IS '软删除时间（NULL 表示未删除）';