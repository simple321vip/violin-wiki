package cn.violin.wiki.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WikiBoxVo {

    @JsonProperty("btId")
    private String btId;

    @JsonProperty("btName")
    private String btName;

    @JsonProperty("blog_list")
    private List<WikiVo> wikiVoList;

    @JsonProperty("order")
    private int order;

}
