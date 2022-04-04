package com.g.estate.controller;

import com.g.estate.io.BookmarkIn;
import com.g.estate.service.BookmarkService;
import com.g.estate.vo.BookmarkVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<BookmarkVo>> bookmark(@RequestParam(value = "type_id") String typeId) {
        return new ResponseEntity<>(bookmarkService.getBookmarks(typeId), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping("/master/bookmark/type")
    public List<String> bookmarkType() {
        return bookmarkService.getBookmarkTypes().stream()
                .map(item -> item.getTypeId() + "|" + item.getTypeName()).collect(Collectors.toList());
    }


    @PutMapping("/bookmark/insert")
    public ResponseEntity<Void> putBookmark(@Valid @RequestBody() BookmarkIn input){
        bookmarkService.insertBookmark(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/bookmark/update")
    public ResponseEntity<Void> updateBookmark(@Valid @RequestBody() BookmarkIn input){
        bookmarkService.updateBookmark(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/bookmark/delete/{bk_id}")
    public ResponseEntity<Void> updateBookmark(@PathVariable("bk_id") long bkId){
        bookmarkService.delete(bkId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
