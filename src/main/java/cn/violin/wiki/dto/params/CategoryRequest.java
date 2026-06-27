package cn.violin.wiki.dto.params;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建 / 更新分类请求。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    private String id;

    @NotBlank
    private String categoryName;

    private Integer sortOrder;
}