package cn.violin.wiki.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogContent {

    @JsonProperty("content")
    private String content;
}
