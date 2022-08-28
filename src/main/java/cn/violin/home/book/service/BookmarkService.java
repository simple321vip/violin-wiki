package cn.violin.home.book.service;

import cn.violin.home.book.entity.*;
import cn.violin.home.book.io.BookmarkIn;
import cn.violin.home.book.utils.NumberEnum;
import cn.violin.home.book.vo.BookmarkVo;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static cn.violin.home.book.utils.Constant.*;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    @Autowired
    private NumberService numberService;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * bookmark 条件検索
     *
     * @param comment コメント
     * @param typeIds　bookmark type id リスト
     * @param isDelete　削除フラグ
     * @return List<Bookmark>
     */
    public List<BookmarkVo> getBookmarks(Long[] typeIds, String comment, String isDelete) {

        Criteria criteria = Criteria.where("deleteFlg").is(isDelete);
        if (StringUtils.hasLength(comment)) {
            criteria.and("comment").is(comment);
        }
        if (typeIds != null && typeIds.length != 0) {
            criteria.and("typeId").in(typeIds);
        }
        Query query = Query.query(criteria);
        List<Bookmark> bookmarks = mongoTemplate.find(query, Bookmark.class);

        return bookmarks.stream().map(bm -> BookmarkVo.builder()
                .id(bm.getId())
                .comment(bm.getComment())
                .typeId(bm.getTypeId())
                .url(bm.getUrl())
                .build()).collect(Collectors.toList());
    }

    /**
     * bookmarkを登録する
     *
     * @param bookmarkIn 11
     */
    @Transactional
    public void insertBookmark(BookmarkIn bookmarkIn) {
        String id = LocalDateTime.now().format(FORMATTER_DATETIME) + numberService.getNumberId(NumberEnum.T_BOOKMARK);
        Bookmark bookmark = new Bookmark();
        bookmark.setId(id);
        bookmark.setTypeId(bookmarkIn.getTypeId());
        bookmark.setComment(bookmarkIn.getComment());
        bookmark.setUrl(bookmarkIn.getUrl());
        bookmark.setDeleteFlg(FALSE_FLAG);
        mongoTemplate.save(bookmark);
    }

    /**
     * bookmarkを更新する
     *
     * @param bookmarkIn 11
     */
    @Transactional()
    public void updateBookmark(BookmarkIn bookmarkIn) {
        Criteria criteria = Criteria.where("id").is(bookmarkIn.getId());
        Query query = Query.query(criteria);
        Update update = Update.update("comment", bookmarkIn.getComment());
        update = update.set("url", bookmarkIn.getUrl());
        update = update.set("typeId", bookmarkIn.getTypeId());
        mongoTemplate.updateFirst(query, update, Bookmark.class);
    }

    /**
     * bookmarkを削除する
     * deleteFlg を　true　にする
     *
     * @param id bookmark id
     */
    @Transactional()
    public void delete(String id) {

        Criteria criteria = Criteria.where("id").is(id);
        Query query = Query.query(criteria);
        Update update = Update.update("isDelete", TRUE_FLAG);
        mongoTemplate.updateFirst(query, update, Bookmark.class);
    }

    /**
     * Bookmark タイプ全件検索
     *
     * @return List<BookmarkType> 11
     */
    public List<BookmarkType> getBookmarkTypes() {
        return mongoTemplate.findAll(BookmarkType.class);
    }

    /**
     * bookmarkTypeを登録する
     *
     * @param input 11
     */
    @Transactional
    public void insertBookmarkType(BookmarkIn input) {
        String id = LocalDateTime.now().format(FORMATTER_DATETIME);
        BookmarkType bookmarkType = new BookmarkType();
        bookmarkType.setTypeId(id);
        bookmarkType.setTypeName(input.getTypeName());
        mongoTemplate.save(bookmarkType);
    }

    /**
     * delete bookmarkType by type_id
     *
     * @param typeId bookmark_type_id
     */
    @Transactional
    public void deleteBookmarkType(String typeId) {
//        String id = LocalDateTime.now().format(FORMATTER_DATETIME);
//        BookmarkType bookmarkType = new BookmarkType();
//        bookmarkType.setTypeId(id);
//        bookmarkType.setTypeName(input.getTypeName());
//        mongoTemplate.save(bookmarkType);
    }

    /**
     * delete bookmarkType by type_id
     *
     * @param input bookmark_type_id
     */
    @Transactional
    public void editBookmarkType(BookmarkIn input) {
//        String id = LocalDateTime.now().format(FORMATTER_DATETIME);
//        BookmarkType bookmarkType = new BookmarkType();
//        bookmarkType.setTypeId(id);
//        bookmarkType.setTypeName(input.getTypeName());
//        mongoTemplate.save(bookmarkType);
    }

}
