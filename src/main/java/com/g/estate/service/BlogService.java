package com.g.estate.service;

import com.g.estate.entity.QBlog;
import com.g.estate.entity.QBlogType;
import com.g.estate.vo.BlogVo;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.g.estate.utils.Constant.FALSE_FLAG;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final JPAQueryFactory jpaQueryFactory;

    public List<BlogVo> getBlogs() {

        List<BlogVo> result;
        QBlog qBlog = QBlog.blog;
        QBlogType qBlogType = QBlogType.blogType;
        BooleanExpression searchConditions = qBlog.deleteFlg.eq(FALSE_FLAG);
        Expression<?>[] selectItems = new Expression[] {
                qBlog.id,
                qBlog.typeId.as("blogTypeId"),
                qBlog.blogTitle,
                qBlog.blogPrex,
                qBlogType.typeName.as("blogTypeName")
        };
        QBean<BlogVo> viewObject = Projections.bean(BlogVo.class, selectItems);
        result = jpaQueryFactory.select(viewObject)
                .from(qBlog)
                .innerJoin(qBlogType)
                .on(qBlog.typeId.eq(qBlogType.typeId))
                .where(searchConditions)
                .fetch();


        return result;
    }

    public BlogVo getBlog(Long blogId) {
        List<BlogVo> result;
        QBlog qBlog = QBlog.blog;
        QBlogType qBlogType = QBlogType.blogType;
        BooleanExpression searchConditions = qBlog.deleteFlg.eq(FALSE_FLAG);
        searchConditions = searchConditions.and(qBlog.id.eq(blogId));
        Expression<?>[] selectItems = new Expression[] {
                qBlog.id,
                qBlog.typeId.as("blogTypeId"),
                qBlog.blogTitle,
                qBlogType.typeName.as("blogTypeName"),
                qBlog.blogText
        };
        QBean<BlogVo> viewObject = Projections.bean(BlogVo.class, selectItems);
        result = jpaQueryFactory.select(viewObject)
                .from(qBlog)
                .innerJoin(qBlogType)
                .on(qBlog.typeId.eq(qBlogType.typeId))
                .where(searchConditions)
                .fetch();
        if (result.size() == 0) {
            return new BlogVo();
        }
        return result.get(0);
    }

}
