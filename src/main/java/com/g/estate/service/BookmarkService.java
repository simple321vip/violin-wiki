package com.g.estate.service;

import com.g.estate.dao.BookmarkRepo;
import com.g.estate.dao.BookmarkTypeRepo;
import com.g.estate.entity.Bookmark;
import com.g.estate.entity.BookmarkType;
import com.g.estate.entity.QBookmark;
import com.g.estate.entity.QBookmarkType;
import com.g.estate.io.BookmarkIn;
import com.g.estate.vo.BookmarkVo;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.g.estate.utils.Constant.FALSE_FLAG;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final JPAQueryFactory jpaQueryFactory;
    @Autowired
    private BookmarkRepo bookmarkRepo;

    @Autowired
    private BookmarkTypeRepo bookmarkTypeRepo;

    /**
     * bookmark全件検索
     *
     * @param typeId
     * @return List<Bookmark>
     */
    public List<BookmarkVo> getBookmarks(String typeId) {

        List<BookmarkVo> result;

        QBookmark qBookmark = QBookmark.bookmark;
        QBookmarkType qBookmarkType = QBookmarkType.bookmarkType;
        QBean<BookmarkVo> viewObject = Projections.bean(BookmarkVo.class, qBookmarkType.typeName, qBookmark.comment, qBookmark.url);

        result = jpaQueryFactory
                .select(viewObject)
                .from(qBookmark)
                .innerJoin(qBookmarkType)
                .on(qBookmark.typeId.eq(qBookmarkType.typeId))
                .orderBy(qBookmark.id.asc())
                // 执行查询
                .fetch();
        return result;
    }

    /**
     * bookmarkを登録する
     *
     * @param bookmarkIn 11
     */
    public void insertBookmark(BookmarkIn bookmarkIn) {
        Bookmark bookmark = new Bookmark();
        bookmark.setTypeId(bookmarkIn.getTypeId());
        bookmark.setComment(bookmarkIn.getComment());
        bookmark.setUrl(bookmarkIn.getUrl());
        bookmark.setDeleteFlg(FALSE_FLAG);
        bookmarkRepo.save(bookmark);
    }

    /**
     * bookmarkを更新する
     *
     * @param bookmarkIn 11
     */
    @Transactional()
    public void updateBookmark(BookmarkIn bookmarkIn) {
        QBookmark qBookmark = QBookmark.bookmark;
        qBookmark.comment.eq(bookmarkIn.getComment());
        qBookmark.url.eq(bookmarkIn.getUrl());
        qBookmark.typeId.eq(bookmarkIn.getTypeId());
        jpaQueryFactory.update(qBookmark)
                .set(qBookmark.comment, bookmarkIn.getComment())
                .set(qBookmark.typeId, bookmarkIn.getTypeId())
                .set(qBookmark.url, bookmarkIn.getUrl())
                .where(qBookmark.id.eq(bookmarkIn.getId()))
                .execute();
    }

    /**
     * Bookmark タイプ全件検索
     *
     * @return List<BookmarkType> 11
     */
    public List<BookmarkType> getBookmarkTypes() {
        return bookmarkTypeRepo.findAll();
    }
}
