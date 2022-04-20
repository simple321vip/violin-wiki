package com.g.estate.controller;

import com.g.estate.io.BlogTypeIn;
import com.g.estate.service.BlogService;
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
public class BlogCtl {

    @Autowired
    private BlogService blogService;

    @ResponseBody
    @RequestMapping("/blog")
    public ResponseEntity<List<BlogVo>> blogs(@RequestParam(value = "type_id", required = false) String[] typeId,
                                              @RequestParam(value = "comment", required = false) String comment) {
        return new ResponseEntity<>(blogService.getBlogs(), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping("/blog/{blog_id}")
    public ResponseEntity<BlogVo> getBlog(@PathVariable(value = "blog_id") String blogId
    ) {
        return new ResponseEntity<>(blogService.getBlog(blogId), HttpStatus.OK);
    }


    @PostMapping(value = "/blog/update_blog_type", produces = {"application/json"})
    public ResponseEntity<Void> updateBlogType(@Valid @RequestBody() BlogTypeIn input) {
        blogService.updateBlogTypeName(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/blog/insert_blog_type", produces = {"application/json"})
    public ResponseEntity<Void> insertBlogType(@Valid @RequestBody() BlogTypeIn input) {
        blogService.insertBlog(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
