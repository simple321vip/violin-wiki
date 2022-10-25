package cn.violin.home.book.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
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
