package com.g.estate.controller;

import com.g.estate.io.BlogIn;
import com.g.estate.io.BlogTypeIn;
import com.g.estate.service.BlogInfoService;
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
public class BlogInfoCtl {

    @Autowired
    private BlogInfoService blogInfoService;

    /**
     * 在线显示文件
     */
    @GetMapping("/author/blog/{id}/content")
    @ResponseBody
    public ResponseEntity<BlogContent> getBlogContent(@PathVariable(value = "id") String id) {
        BlogContent content = new BlogContent();
        content.setContent(blogInfoService.getContent(id));
        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @PostMapping("/author/blog/createContent")
    @ResponseBody
    public ResponseEntity<BlogVo> newBlog(@Valid @RequestBody BlogIn input) {
        return new ResponseEntity<>(blogInfoService.insertContent(input), HttpStatus.OK);
    }

    @PostMapping("/author/blog/updateContent")
    @ResponseBody
    public ResponseEntity<Void> updateBlog(@Valid @RequestBody BlogIn input) {
        blogInfoService.updateContent(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/author/blog/delete/{bid}")
    @ResponseBody
    public ResponseEntity<Void> deleteBlog(@PathVariable(value = "bid") String bid) {
        blogInfoService.deleteContent(bid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/author/blog/list")
    @ResponseBody
    public ResponseEntity<List<BlogBoxVo>> blogView() {

        return new ResponseEntity<>(blogInfoService.listAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/author/blog/update_blog_type", produces = {"application/json"})
    public ResponseEntity<Void> updateBlogType(@Valid @RequestBody() BlogTypeIn input) {
        blogInfoService.updateBlogTypeName(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
