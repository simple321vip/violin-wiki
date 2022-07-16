package com.g.estate.controller;

import com.g.estate.service.BlogEditService;
import com.g.estate.service.BlogViewService;
import com.g.estate.vo.BlogBoxVo;
import com.g.estate.vo.BlogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/api/v1/reader")
public class BlogViewCtl {

    @Autowired
    private BlogViewService blogViewService;

    /**
     * 在线显示文件
     */
    @GetMapping("/blogs")
    @ResponseBody
    public ResponseEntity<List<BlogVo>> lists(@RequestParam(value = "btId", required = false) String btId,
                                              @RequestParam(value = "key_word", required = false) String keyWord,
                                              @RequestParam(value = "start_day", required = false) LocalDate startDay,
                                              @RequestParam(value = "end_day", required = false) LocalDate endDay) {
        List<BlogVo> vos = blogViewService.selectBlogs(btId, keyWord, startDay, endDay);
        return new ResponseEntity<>(vos, HttpStatus.OK);
    }

    /**
     * 在线显示文件
     */
    @GetMapping("/blog/{bid}")
    @ResponseBody
    public ResponseEntity<BlogVo> oneBlog(@PathVariable(value = "bid") String bid) {
        BlogVo vo = blogViewService.selectBlog(bid);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    /**
     * btNameリスト一覧取得
     */
    @GetMapping("/blog_type")
    @ResponseBody
    public ResponseEntity<List<BlogBoxVo>> btLists() {
        List<BlogBoxVo> vos = blogViewService.selectBtName();
        return new ResponseEntity<>(vos, HttpStatus.OK);
    }

}
