package com.g.estate.controller;

import com.g.estate.io.BookmarkIn;
import com.g.estate.service.BookmarkService;
import com.g.estate.vo.BookmarkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin
public class BookmarkCtl {

    @Autowired
    private BookmarkService bookmarkService;

    @ResponseBody
    @RequestMapping("/bookmark")
    public List<BookmarkVo> bookmark(@RequestParam(value = "type_id") String typeId) {
        return bookmarkService.getBookmarks(typeId);
    }

    @ResponseBody
    @RequestMapping("/master/bookmark/type")
    public List<String> bookmarkType() {
        List<String> list = bookmarkService.getBookmarkTypes().stream()
                .map(item -> item.getTypeId() + "|" + item.getTypeName()).collect(Collectors.toList());
        return list;
    }


    @PutMapping("/bookmark/insert")
    public void putBookmark(@Valid @RequestBody() BookmarkIn input){
        bookmarkService.insertBookmark(input);
    }

    @PostMapping("/bookmark/update")
    public void updateBookmark(@Valid @RequestBody() BookmarkIn input){
        bookmarkService.updateBookmark(input);
    }

}
