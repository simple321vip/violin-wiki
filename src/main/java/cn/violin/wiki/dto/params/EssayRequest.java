package cn.violin.wiki.dto.params;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create / update article request payload.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EssayRequest {

    private String id;

    @NotBlank
    private String title;

    @NotBlank
    private String categoryId;

    private String content;

    /** Auto-save marker ("1" / "0"). */
    private String autoSave;

    private Integer sortOrder;
}