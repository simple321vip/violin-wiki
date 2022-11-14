package cn.violin.home.book.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogBoxVo {

    @JsonProperty("btId")
    private String btId;

    @JsonProperty("btName")
    private String btName;

    @JsonProperty("blog_list")
    private List<BlogVo> blogVoList;

    @JsonProperty("order")
    private int order;

}
