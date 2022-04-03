package com.g.estate.controller;

import com.g.estate.entity.Bookmark;
import com.g.estate.io.BookmarkIn;
import com.g.estate.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin
public class BookmarkCtl {

    @Autowired
    private BookmarkService bookmarkService;

    @ResponseBody
    @RequestMapping("/bookmark")
    public List<Bookmark> bookmark() {
        List<Bookmark> list = new ArrayList<>();
        Bookmark bookmark = new Bookmark();
        bookmark.setId(1);
        bookmark.setType("java");
        bookmark.setDeleteFlg("0");
        bookmark.setUrl("https://www.baidu.com/");
        bookmark.setComment("百度");
        list.add(bookmark);
        list.add(bookmark);
        list.add(bookmark);
        return list;
//        return locationRepo.getLocationsByType("");
    }


    @PutMapping("/bookmark")
    public void uploadBookmark(@Valid @RequestBody() BookmarkIn input){
        bookmarkService.insertBookmark(input);
    }

}
