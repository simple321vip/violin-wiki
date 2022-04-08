package com.g.estate.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageVo {

    @JsonProperty("page_id")
    private long pageId;

    @JsonProperty("section_id")
    private long sectionId;

    @JsonProperty("page_name")
    private String pageName;

    @JsonProperty("page_path")
    private String pagePath;
}
