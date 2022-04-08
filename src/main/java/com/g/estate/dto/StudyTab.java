package com.g.estate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.g.estate.entity.Page;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyTab {

    private long pageId;

    private long sectionId;

    private long userId;

    private String pageName;

    private String pagePath;

    private String sectionName;

}
