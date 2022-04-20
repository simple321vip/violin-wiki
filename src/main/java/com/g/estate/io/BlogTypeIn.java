package com.g.estate.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BlogTypeIn {

    @JsonProperty("blog_type_id")
    private String blogTypeId;

    @JsonProperty("blog_type_name")
    @NotNull
    private String blogTypeName;
}
