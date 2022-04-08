package com.g.estate.service;

import com.g.estate.dto.StudyTab;
import com.g.estate.entity.QPage;
import com.g.estate.entity.QSection;
import com.g.estate.vo.PageVo;
import com.g.estate.vo.SectionVo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final JPAQueryFactory jpaQueryFactory;

    public List<SectionVo> getStudy(long userId) {

        QSection qSection = QSection.section;
        QPage page = QPage.page;
        QBean<StudyTab> qBean = Projections.bean(StudyTab.class);

        List<StudyTab> result = jpaQueryFactory.select(qBean)
                .from(qSection, page)
                .leftJoin(page)
                .on(qSection.sectionId.eq(page.sectionId))
                .where(qSection.userId.eq(userId))
                .fetch();
        List<SectionVo> sectionVos = result.stream().collect(Collectors.groupingBy(StudyTab::getSectionId))
                .entrySet().stream().map(section -> {
                    long sectionId = section.getKey();
                    List<StudyTab> studyTabs = section.getValue();
                    List<PageVo> pageVos = studyTabs.stream().map(record -> PageVo.builder().pageId(record.getPageId())
                            .pageName(record.getPageName())
                            .sectionId(record.getSectionId())
                            .pagePath(record.getPagePath())
                            .build()).collect(Collectors.toList());
                    return SectionVo.builder().sectionId(sectionId)
                            .sectionName(studyTabs.get(0).getSectionName())
                            .pages(pageVos).build();
                }).collect(Collectors.toList());

        return sectionVos;
    }
}
