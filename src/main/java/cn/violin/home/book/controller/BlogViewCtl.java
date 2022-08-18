package cn.violin.home.book.controller;

import cn.violin.home.book.service.BlogViewService;
import cn.violin.home.book.vo.BlogBoxVo;
import cn.violin.home.book.vo.BlogVo;
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

    @GetMapping("/blogs/publish/all")
    public ResponseEntity<Void> publishAll() {
        blogViewService.publishAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/blogs/publish/{bid}")
    public ResponseEntity<Void> publish(@PathVariable(value = "bid") String bid) {
        blogViewService.publish(bid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
