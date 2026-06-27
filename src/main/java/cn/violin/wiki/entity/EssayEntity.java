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
 * 文章实体（snake_case 列名 + 审计 + 软删除）。
 *
 * <p>字段顺序：主键 / 多租户 → 业务 → 审计 → 软删除。</p>
 *
 * <p>不继承 BaseEntity：BaseEntity 仍带 JPA 注解 + 字段名（{@code create_by}/{@code create_date}）
 * 与本服务 schema ({@code created_by}/{@code created_at}) 不一致；为保持各服务独立，
 * 这里直接定义所有字段。后续如果 core 重构 BaseEntity，再统一切换。</p>
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