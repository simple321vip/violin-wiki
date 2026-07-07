package cn.violin.wiki.dto.result;

import cn.violin.wiki.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Category response payload (exposed to clients).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResult {

    private String id;
    private String categoryName;
    private Integer sortOrder;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

 /** Associated essays (lazy-loading / hierarchical response). */
    private List<EssayResult> essays;

    public static CategoryResult from(CategoryEntity c) {
        if (c == null) return null;
        return CategoryResult.builder()
            .id(c.getId())
            .categoryName(c.getCategoryName())
            .sortOrder(c.getSortOrder())
            .createdBy(c.getCreatedBy())
            .createdAt(c.getCreatedAt())
            .updatedBy(c.getUpdatedBy())
            .updatedAt(c.getUpdatedAt())
            .essays(c.getEssays() == null ? null :
                c.getEssays().stream().map(EssayResult::from).toList())
            .build();
    }
}