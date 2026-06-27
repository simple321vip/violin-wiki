package cn.violin.wiki.dto.params;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 排序请求（批量更新 sort_order）。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortRequest {

    /** 分类排序用。 */
    @NotEmpty
    private String[] ids;

    /** 文章排序用（与 categoryId 配合）。 */
    private String[] essayIds;
}