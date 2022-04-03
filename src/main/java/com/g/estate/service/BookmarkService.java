package com.g.estate.service;

import com.g.estate.dao.BookmarkRepo;
import com.g.estate.entity.Bookmark;
import com.g.estate.io.BookmarkIn;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

//    private final JPAQueryFactory jpaQueryFactory;
    @Autowired
    private BookmarkRepo bookmarkRepo;


    /**
     * bookmarkを登録する
     * @param bookmarkIn
     */
    public void insertBookmark(BookmarkIn bookmarkIn) {
        Bookmark bookmark = new Bookmark();
        bookmark.setType(bookmarkIn.getType());
        bookmark.setComment(bookmarkIn.getComment());
        bookmark.setUrl(bookmarkIn.getUrl());
        bookmark.setDeleteFlg("0");
        bookmarkRepo.save(bookmark);
    }
}
