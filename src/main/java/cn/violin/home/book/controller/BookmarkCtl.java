package cn.violin.home.book.controller;

import cn.violin.home.book.io.BookmarkIn;
import cn.violin.home.book.service.BookmarkService;
import cn.violin.home.book.vo.BookmarkVo;
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
@RequestMapping("/api/v1")
public class BookmarkCtl {

    @Autowired
    private BookmarkService bookmarkService;

    @ResponseBody
    @RequestMapping("/bookmark")
    public ResponseEntity<List<BookmarkVo>> bookmark(@RequestParam(value = "type_id" ,required = false) Long[] typeId,
                                                     @RequestParam(value = "comment") String comment) {
        return new ResponseEntity<>(bookmarkService.getBookmarks(typeId, comment, "0"), HttpStatus.OK);
    }

    @PutMapping("/bookmark/insert")
    public ResponseEntity<Void> putBookmark(@Valid @RequestBody() BookmarkIn input) {
        bookmarkService.insertBookmark(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/bookmark/update")
    public ResponseEntity<Void> updateBookmark(@Valid @RequestBody() BookmarkIn input) {
        bookmarkService.updateBookmark(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/bookmark/delete/{bk_id}")
    public ResponseEntity<Void> updateBookmark(@PathVariable("bk_id") String bkId) {
        bookmarkService.delete(bkId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/bookmark_type/insert")
    public ResponseEntity<Void> putBookmarkType(@Valid @RequestBody() BookmarkIn input) {
        bookmarkService.insertBookmarkType(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping("/bookmark_type")
    public List<String> bookmarkType() {
        return bookmarkService.getBookmarkTypes().stream()
                .map(item -> item.getTypeId() + "|" + item.getTypeName()).collect(Collectors.toList());
    }

    @ResponseBody
    @DeleteMapping("/bookmark_type")
    public ResponseEntity<Void> deleteBookmarkType(@PathVariable("bookmark_type_id") String typeId) {
        bookmarkService.deleteBookmarkType(typeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/bookmark_type")
    public ResponseEntity<Void> deleteBookmarkType(@Valid @RequestBody() BookmarkIn input) {
        bookmarkService.editBookmarkType(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
