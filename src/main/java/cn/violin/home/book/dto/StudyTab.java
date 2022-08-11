package cn.violin.home.book.dto;

import lombok.*;

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
