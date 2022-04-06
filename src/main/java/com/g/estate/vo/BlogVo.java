package com.g.estate.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogVo {
    @JsonProperty("blog_id")
    private long id;

    @JsonProperty("blog_type_id")
    private long blogTypeId;

    @JsonProperty("blog_type_name")
    private String blogTypeName;

    @JsonProperty("blog_title")
    private String blogTitle;

    @JsonProperty("blog_prex")
    private String blogPrex;

    @JsonProperty("blog_text")
    private String blogText;
}
