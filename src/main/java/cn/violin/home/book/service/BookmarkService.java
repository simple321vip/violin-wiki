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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     * @param deleteFlg　削除フラグ
     * @return List<Bookmark>
     */
    public List<BookmarkVo> getBookmarks(String[] typeIds, String comment, String deleteFlg, Tenant tenant) {

        // 1. select bookmark_type
        Criteria criteria1 = new Criteria();
        criteria1.and("owner").is(tenant.getTenantId());
        if (typeIds != null && typeIds.length != 0) {
            criteria1.and("typeId").in(typeIds);
        }
        Query query1 = Query.query(criteria1);
        List<BookmarkType> bookmarkTypes= mongoTemplate.find(query1, BookmarkType.class);
        Map<String, String> types = bookmarkTypes
                .stream()
                .collect(
                        HashMap::new,
                        (map, item)->map.put(item.getTypeId(), item.getTypeName()),
                        HashMap::putAll
                );

        // 2. select bookmark
        Criteria criteria2 = Criteria.where("deleteFlg").is(deleteFlg);
        criteria2.and("owner").is(tenant.getTenantId());
        if (StringUtils.hasLength(comment)) {
            criteria2.and("comment").is(comment);
        }
        if (typeIds != null && typeIds.length != 0) {
            criteria2.and("typeId").in(typeIds);
        }
        Query query2 = Query.query(criteria2);
        List<Bookmark> bookmarks = mongoTemplate.find(query2, Bookmark.class);

        // 3. matching process
        return bookmarks.stream().map(bm -> BookmarkVo.builder()
                .id(bm.getId())
                .comment(bm.getComment())
                .typeId(bm.getTypeId())
                .typeName(types.get(bm.getTypeId()))
                .url(bm.getUrl())
                .build()).collect(Collectors.toList());
    }

    /**
     * bookmarkを登録する
     *
     * @param bookmarkIn 11
     */
    @Transactional
    public void insertBookmark(BookmarkIn bookmarkIn, Tenant tenant) {
        String id = LocalDateTime.now().format(FORMATTER_DATETIME) + numberService.getNumberId(NumberEnum.T_BOOKMARK);
        Bookmark bookmark = Bookmark.builder()
                .id(id)
                .typeId(bookmarkIn.getTypeId())
                .comment(bookmarkIn.getComment())
                .url(bookmarkIn.getUrl())
                .deleteFlg(FALSE_FLAG)
                .owner(tenant.getTenantId()).build();
        mongoTemplate.save(bookmark);
    }

    /**
     * bookmarkを更新する
     *
     * @param bookmarkIn 11
     */
    @Transactional()
    public void updateBookmark(BookmarkIn bookmarkIn, Tenant tenant) {
        Criteria criteria = Criteria.where("id").is(bookmarkIn.getId())
                .and("owner").is(tenant.getTenantId());
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
    public void delete(String id, Tenant tenant) {

        Criteria criteria = Criteria.where("id").is(id)
                .and("owner").is(tenant.getTenantId());
        Query query = Query.query(criteria);
        Update update = Update.update("deleteFlg", TRUE_FLAG);
        mongoTemplate.updateFirst(query, update, Bookmark.class);
    }

    /**
     * Bookmark タイプ全件検索
     *
     * @return List<BookmarkType> 11
     */
    public List<BookmarkType> getBookmarkTypes(Tenant tenant) {
        Criteria criteria = Criteria.where("owner").is(tenant.getTenantId());
        Query query = Query.query(criteria);
        return mongoTemplate.find(query, BookmarkType.class);
    }

    /**
     * bookmarkTypeを登録する
     *
     * @param input 11
     */
    @Transactional
    public void insertBookmarkType(BookmarkIn input, Tenant tenant) {
        String id = LocalDateTime.now().format(FORMATTER_DATETIME);
        BookmarkType bookmarkType = new BookmarkType();
        bookmarkType.setTypeId(id);
        bookmarkType.setTypeName(input.getTypeName());
        bookmarkType.setOwner(tenant.getTenantId());
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
