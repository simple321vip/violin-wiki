package com.g.estate.service;

import com.g.estate.dao.BookmarkRepo;
import com.g.estate.dao.BookmarkTypeRepo;
import com.g.estate.entity.Bookmark;
import com.g.estate.entity.BookmarkType;
import com.g.estate.entity.QBookmark;
import com.g.estate.entity.QBookmarkType;
import com.g.estate.io.BookmarkIn;
import com.g.estate.utils.NumberEnum;
import com.g.estate.vo.BookmarkVo;
import com.g.estate.vo.ResponsePageVo;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.g.estate.utils.Constant.*;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    private NumberService numberService;

    @Autowired
    private BookmarkRepo bookmarkRepo;

    @Autowired
    private BookmarkTypeRepo bookmarkTypeRepo;

    /**
     * bookmark全件検索
     *
     * @param typeIds
     * @return List<Bookmark>
     */
    public List<BookmarkVo> getBookmarks(Long[] typeIds, String comment) {

        List<BookmarkVo> result;
        QBookmark qBookmark = QBookmark.bookmark;
        QBookmarkType qBookmarkType = QBookmarkType.bookmarkType;
        Expression<?>[] selectItems = new Expression[]{
                qBookmark.id,
                qBookmark.typeId,
                qBookmarkType.typeName,
                qBookmark.comment,
                qBookmark.url
        };
        BooleanExpression searchConditions = qBookmark.deleteFlg.eq(FALSE_FLAG);
        if (typeIds != null && typeIds.length != 0) {
            searchConditions = searchConditions.and(qBookmarkType.typeId.in(typeIds));
        }
        if (StringUtils.hasLength(comment)) {
            searchConditions = searchConditions.and(qBookmark.comment.like(LIKE_FIX + comment + LIKE_FIX));
        }

        QBean<BookmarkVo> viewObject = Projections.bean(BookmarkVo.class, selectItems);
        result = jpaQueryFactory
                .select(viewObject)
                .from(qBookmark)
                .innerJoin(qBookmarkType)
                .on(qBookmark.typeId.eq(qBookmarkType.typeId))
                .where(searchConditions)
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
    @Transactional
    public void insertBookmark(BookmarkIn bookmarkIn) {
        String bkId = numberService.getNumberId(NumberEnum.T_BOOKMARK);
        Bookmark bookmark = new Bookmark();
        bookmark.setId(bkId);
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
        jpaQueryFactory.update(qBookmark)
                .set(qBookmark.comment, bookmarkIn.getComment())
                .set(qBookmark.typeId, bookmarkIn.getTypeId())
                .set(qBookmark.url, bookmarkIn.getUrl())
                .where(qBookmark.id.eq(bookmarkIn.getId()))
                .execute();
    }

    /**
     * bookmarkを削除する
     * deleteFlg を　true　にする
     *
     * @param
     */
    @Transactional()
    public void delete(String bkId) {
        QBookmark qBookmark = QBookmark.bookmark;
        jpaQueryFactory.update(qBookmark)
                .set(qBookmark.deleteFlg, TRUE_FLAG)
                .where(qBookmark.id.eq(bkId))
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
