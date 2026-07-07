package cn.violin.wiki.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Article entity (snake_case columns + audit + soft-delete).
 *
 * <p>Field order: PK / multi-tenant -> business -> audit -> soft-delete.</p>
 *
 * <p>Does not extend {@code BaseEntity}: the shared {@code BaseEntity} still carries JPA
 * annotations and column names ({@code create_by} / {@code create_date}) that don't match
 * this service's schema ({@code created_by} / {@code created_at}). Keeping the fields inline
 * per service preserves column independence; revisit if {@code core} ever refactors
 * {@code BaseEntity}.</p>
 */
@Data
@TableName("essays")
public class EssayEntity implements Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @TableField("customer_id")
    private String customerId;

    @TableField("category_id")
    private String categoryId;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField("created_by")
    private String createdBy;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_by")
    private String updatedBy;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @TableLogic
    @TableField("deleted_at")
    private LocalDateTime deletedAt;
}