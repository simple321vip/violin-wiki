package com.g.estate.service;

import com.g.estate.entity.QBlog;
import com.g.estate.entity.QBlogType;
import com.g.estate.entity.QSection;
import com.g.estate.io.BlogTypeIn;
import com.g.estate.io.BookmarkIn;
import com.g.estate.utils.NumberEnum;
import com.g.estate.vo.BlogVo;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;

import static com.g.estate.utils.Constant.FALSE_FLAG;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    private NumberService numberService;

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

    public BlogVo getBlog(String blogId) {
        List<BlogVo> result;
        QBlog qBlog = QBlog.blog;
        QBlogType qBlogType = QBlogType.blogType;
        BooleanExpression searchConditions = qBlog.deleteFlg.eq(FALSE_FLAG);
        searchConditions = searchConditions.and(qBlog.id.eq(blogId));
        Expression<?>[] selectItems = new Expression[] {
                qBlog.id,
                qBlog.typeId.as("blogTypeId"),
                qBlog.blogTitle,
                qBlogType.typeName.as("blogTypeName")
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
        BlogVo vo = result.get(0);
        this.getBlogDetail(vo);
        return vo;
    }

    private void getBlogDetail(BlogVo vo) {
        String filePath = System.getProperty("BLOG_FOLDER_HOME");

        BufferedReader br;
        StringBuilder sb = new StringBuilder();
        try {
            br = new BufferedReader(new FileReader(new File(filePath + "\\README.md")));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.getProperty("line.separator"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        vo.setBlogText(sb.toString());
    }

    @Transactional()
    public void insertBlog(BlogTypeIn input) {
        QBlogType qBlogType = QBlogType.blogType;
        String user = "";
        List<?> result = jpaQueryFactory
                .selectOne()
                .from(qBlogType)
                .where(qBlogType.updaterId.eq(user).and(qBlogType.typeName.eq(input.getBlogTypeName())))
                .fetch();
        if (result.isEmpty()) {
            String blogTypeId = numberService.getNumberId(NumberEnum.T_BLOG_TYPE);
            jpaQueryFactory
                    .insert(qBlogType)
                    .columns()
                    .values(blogTypeId, input.getBlogTypeName())
                    .execute();
        } else {
            // TODO update_error
        }
    }

    @Transactional()
    public void updateBlogTypeName(BlogTypeIn input) {
        QBlogType qBlogType = QBlogType.blogType;
        jpaQueryFactory.update(qBlogType)
                .set(qBlogType.typeName, input.getBlogTypeName())
                .where(qBlogType.typeId.eq(input.getBlogTypeId()))
                .execute();
    }

}
