package com.g.estate.controller;

import com.g.estate.service.BlogService;
import com.g.estate.vo.BlogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
public class BlogCtl {

    @Autowired
    private BlogService blogService;

    @ResponseBody
    @RequestMapping("/blog")
    public ResponseEntity<List<BlogVo>> blogs(@RequestParam(value = "type_id", required = false) Long[] typeId,
                                              @RequestParam(value = "comment", required = false) String comment) {
        return new ResponseEntity<>(blogService.getBlogs(), HttpStatus.OK);
    }

//    @ResponseBody
//    @RequestMapping("/blog/{blog_id}")
//    public ResponseEntity<Void> blog(@PathVariable(value = "blog_id", required = false) Long blogId
//    ) {
//        blogService.getBlog(blogId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @ResponseBody
    @RequestMapping("/blog/{blog_id}")
    public ResponseEntity<BlogVo> getBlog(@PathVariable(value = "blog_id") Long blogId
    ) {
        return new ResponseEntity<>(blogService.getBlog(blogId), HttpStatus.OK);
    }

}
