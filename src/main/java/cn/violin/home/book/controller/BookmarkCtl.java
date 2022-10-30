package cn.violin.home.book.controller;

import cn.violin.home.book.annotation.CurrentUser;
import cn.violin.home.book.entity.Tenant;
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
    public ResponseEntity<List<BookmarkVo>> bookmark(@RequestParam(value = "type_id" ,required = false) String[] typeId,
                                                     @RequestParam(value = "comment") String comment,
                                                     @CurrentUser Tenant tenant) {
        return new ResponseEntity<>(bookmarkService.getBookmarks(typeId, comment, "0", tenant), HttpStatus.OK);
    }

    @PutMapping("/bookmark/insert")
    public ResponseEntity<Void> putBookmark(@Valid @RequestBody() BookmarkIn input, @CurrentUser Tenant tenant) {
        bookmarkService.insertBookmark(input, tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/bookmark/update")
    public ResponseEntity<Void> updateBookmark(@Valid @RequestBody() BookmarkIn input, @CurrentUser Tenant tenant) {
        bookmarkService.updateBookmark(input, tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/bookmark/delete/{bk_id}")
    public ResponseEntity<Void> updateBookmark(@PathVariable("bk_id") String bkId, @CurrentUser Tenant tenant) {
        bookmarkService.delete(bkId, tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/bookmark_type/insert")
    public ResponseEntity<Void> putBookmarkType(@Valid @RequestBody() BookmarkIn input, @CurrentUser Tenant tenant) {
        bookmarkService.insertBookmarkType(input, tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping("/bookmark_type")
    public List<String> bookmarkType(@CurrentUser Tenant tenant) {
        return bookmarkService.getBookmarkTypes(tenant).stream()
                .map(item -> item.getTypeId() + "|" + item.getTypeName()).collect(Collectors.toList());
    }

    @ResponseBody
    @DeleteMapping("/bookmark_type")
    public ResponseEntity<Void> deleteBookmarkType(@PathVariable("bookmark_type_id") String typeId, @CurrentUser Tenant tenant) {
        bookmarkService.deleteBookmarkType(typeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/bookmark_type")
    public ResponseEntity<Void> deleteBookmarkType(@Valid @RequestBody() BookmarkIn input, @CurrentUser Tenant tenant) {
        bookmarkService.editBookmarkType(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
