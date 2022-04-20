package com.g.estate.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogBoxVo {

    @JsonProperty("blog_type_id")
    private String blogTypeId;

    @JsonProperty("blog_type_name")
    private String blogTypeName;

    @JsonProperty("blog_list")
    private List<BlogVo> blogVoList;

}
