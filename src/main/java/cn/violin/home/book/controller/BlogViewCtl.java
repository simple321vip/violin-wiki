package cn.violin.home.book.controller;

import cn.violin.home.book.service.BlogViewService;
import cn.violin.home.book.vo.BlogBoxVo;
import cn.violin.home.book.vo.BlogVo;
import cn.violin.home.common.annotation.CurrentUser;
import cn.violin.home.common.entity.Tenant;
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
     *
     *
     */
    @GetMapping("/blogs")
    @ResponseBody
    public ResponseEntity<List<BlogVo>> lists(@RequestParam(value = "btId", required = false) String btId,
                                              @RequestParam(value = "key_word", required = false) String keyWord,
                                              @RequestParam(value = "start_day", required = false) LocalDate startDay,
                                              @RequestParam(value = "end_day", required = false) LocalDate endDay,
                                              @CurrentUser Tenant tenant) {
        List<BlogVo> vos = blogViewService.selectBlogs(btId, keyWord, startDay, endDay, tenant);
        return new ResponseEntity<>(vos, HttpStatus.OK);
    }

    /**
     * 在线显示文件
     *
     * @param tenant tenant
     */
    @GetMapping("/blog/{bid}")
    @ResponseBody
    public ResponseEntity<BlogVo> oneBlog(@PathVariable(value = "bid") String bid, @CurrentUser Tenant tenant) {
        BlogVo vo = blogViewService.selectBlog(bid);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    /**
     * btNameリスト一覧取得
     *
     * @param tenant tenant
     *
     */
    @GetMapping("/blog_type")
    @ResponseBody
    public ResponseEntity<List<BlogBoxVo>> btLists(@CurrentUser Tenant tenant) {
        List<BlogBoxVo> vos = blogViewService.selectBtName(tenant);
        return new ResponseEntity<>(vos, HttpStatus.OK);
    }

    /**
     * @param tenant tenant
     *
     * @return Void
     */
    @GetMapping("/blogs/publish/all")
    public ResponseEntity<Void> publishAll(@CurrentUser Tenant tenant) {
        blogViewService.publishAll(tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param bid bid
     * @param tenant tenant
     *
     * @return Void
     */
    @GetMapping("/blogs/publish/{bid}")
    public ResponseEntity<Void> publish(@PathVariable(value = "bid") String bid, @CurrentUser Tenant tenant) {
        blogViewService.publish(bid, tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param
     * @param tenant tenant
     *
     * @return Void
     */
    @PutMapping("/docs/{wiki_name}")
    public ResponseEntity<Void> putWikiName(@PathVariable(value = "wiki_name") String name, @CurrentUser Tenant tenant) {
        blogViewService.putWiki(name, tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
