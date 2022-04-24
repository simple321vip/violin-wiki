package com.g.estate.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogIn {
    @JsonProperty("blog_type_id")
    private String blogTypeId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("bid")
    private String bid;

    @JsonProperty("content")
    private String content;

}
