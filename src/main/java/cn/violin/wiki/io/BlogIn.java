package cn.violin.wiki.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlogIn {
    @JsonProperty("btId")
    private String btId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("bid")
    private String bid;

    @JsonProperty("content")
    private String content;

    @JsonProperty("autoSave")
    private String autoSave;

    @JsonProperty("order")
    private int order;

}
