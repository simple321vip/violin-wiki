package cn.violin.wiki.dto.params;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create / update category request payload.
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