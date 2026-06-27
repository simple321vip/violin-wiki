package cn.violin.wiki.dto.params;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建 / 更新文章请求。
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

    /** 自动保存标识（"1" / "0"）。 */
    private String autoSave;

    private Integer sortOrder;
}