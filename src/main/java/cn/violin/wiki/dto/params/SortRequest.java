package cn.violin.wiki.dto.params;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sort request payload (batch updates sort_order).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortRequest {

    /** Category ordering payload. */
    @NotEmpty
    private String[] ids;

    /** Essay ordering payload (paired with categoryId on the request URI). */
    private String[] essayIds;
}