package com.g.estate.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SectionVo {

    @JsonProperty("section_id")
    private long sectionId;

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("section_id")
    private String sectionName;

    @JsonProperty("page_list")
    private List<PageVo> pages;

}
