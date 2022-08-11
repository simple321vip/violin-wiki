package cn.violin.home.book.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class BlogVo {
    @JsonProperty("bid")
    private String bid;

    @JsonProperty("auto_save_control")
    private int autoSaveControl;

    @JsonProperty("title")
    private String title;

    @JsonProperty("blog_prex")
    private String blogPrex;

    @JsonProperty("content")
    private String content;

    @JsonProperty("btId")
    private String btId;

    @JsonProperty("autoSave")
    private String autoSave;

}
