package cn.violin.wiki.dto.result;

import cn.violin.wiki.entity.EssayEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Article response payload (exposed to clients).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EssayResult {

    private String id;
    private String categoryId;
    private String title;
    private String content;
    private Integer sortOrder;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

    public static EssayResult from(EssayEntity e) {
        if (e == null) return null;
        return EssayResult.builder()
            .id(e.getId())
            .categoryId(e.getCategoryId())
            .title(e.getTitle())
            .content(e.getContent())
            .sortOrder(e.getSortOrder())
            .createdBy(e.getCreatedBy())
            .createdAt(e.getCreatedAt())
            .updatedBy(e.getUpdatedBy())
            .updatedAt(e.getUpdatedAt())
            .build();
    }
}