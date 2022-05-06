package com.g.estate.controller;

import com.g.estate.io.BlogIn;
import com.g.estate.io.BlogTypeIn;
import com.g.estate.service.BlogEditService;
import com.g.estate.vo.BlogBoxVo;
import com.g.estate.vo.BlogContent;
import com.g.estate.vo.BlogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/api/vi/author")
public class BlogEditCtl {

    @Autowired
    private BlogEditService blogEditService;

    /**
     * bidで該当blogの内容を取得する
     */
    @GetMapping("/blog/content/{id}")
    @ResponseBody
    public ResponseEntity<BlogContent> getBlogContent(@PathVariable(value = "id") String id) {
        BlogContent content = new BlogContent();
        content.setContent(blogEditService.getContent(id));
        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @PutMapping("/blog/content")
    @ResponseBody
    public ResponseEntity<BlogVo> newBlog(@Valid @RequestBody BlogIn input) {
        return new ResponseEntity<>(blogEditService.insertContent(input), HttpStatus.OK);
    }

    @PostMapping("/blog/content")
    @ResponseBody
    public ResponseEntity<Void> updateBlog(@Valid @RequestBody BlogIn input) {
        blogEditService.updateContent(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/blog/{bid}")
    @ResponseBody
    public ResponseEntity<Void> deleteBlog(@PathVariable(value = "bid") String bid) {
        blogEditService.deleteContent(bid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/blog/list")
    @ResponseBody
    public ResponseEntity<List<BlogBoxVo>> blogView() {

        return new ResponseEntity<>(blogEditService.listAll(), HttpStatus.OK);
    }

    @PutMapping(value = "/blog_type", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<BlogBoxVo> createBlogType(@Valid @RequestBody() BlogTypeIn input) {
        BlogBoxVo vo = blogEditService.insertBlogType(input);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @DeleteMapping(value = "/blog_type", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> deleteBlogType(@Valid @RequestBody() BlogTypeIn input) {
        try {
            blogEditService.deleteBlogType(input.getBtId());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/blog_type", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> updateBlogType(@Valid @RequestBody() BlogTypeIn input) {
        blogEditService.updateBlogTypeName(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
