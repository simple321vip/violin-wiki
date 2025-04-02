package cn.violin.wiki.controller;

import cn.violin.wiki.service.WikiBoxService;
import cn.violin.wiki.service.WikiService;
import cn.violin.wiki.service.WikiViewService;
import cn.violin.wiki.vo.WikiBoxVo;
import cn.violin.wiki.vo.WikiVo;
import cn.violin.common.annotation.CurrentUser;
import cn.violin.common.entity.Tenant;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static cn.violin.common.utils.CommonConstant.YYYY_MM_DD_HH_MM_SS;

@Controller
@RequestMapping("/api/v1/reader")
@AllArgsConstructor
@CrossOrigin
public class WikiViewController {

    @Autowired
    private WikiViewService wikiViewService;

    @Autowired
    private WikiBoxService wikiBoxService;

    @Autowired
    private WikiService wikiService;

    /**
     * 在线显示文件
     *
     *
     */
    @GetMapping("/blogs")
    @ResponseBody
    public ResponseEntity<List<WikiVo>> lists(@RequestParam(value = "btId", required = false) String btId,
                                              @RequestParam(value = "key_word", required = false) String keyWord,
                                              @RequestParam(value = "start_day", required = false) @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS) LocalDate startDay,
                                              @RequestParam(value = "end_day", required = false) @DateTimeFormat(pattern = YYYY_MM_DD_HH_MM_SS) LocalDate endDay,
                                              @RequestParam(value = "page_number")  Integer pageNum,
                                              @RequestParam(value = "page_size")  Integer pageSize,
                                              @CurrentUser Tenant tenant) {
        tenant.setPageNo(pageNum);
        tenant.setPageSize(pageSize);
        List<WikiVo> vos = wikiViewService.selectBlogs(btId, keyWord, startDay, endDay, tenant);
        return new ResponseEntity<>(vos, HttpStatus.OK);
    }

    /**
     * 在线显示文件
     *
     * @param tenant tenant
     */
    @GetMapping("/blog/{bid}")
    @ResponseBody
    public ResponseEntity<WikiVo> oneBlog(@PathVariable(value = "bid") String bid, @CurrentUser Tenant tenant) throws Exception {
        WikiVo vo = wikiService.get(bid, tenant);
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
    public ResponseEntity<List<WikiBoxVo>> btLists(@CurrentUser Tenant tenant) {
        List<WikiBoxVo> vos = wikiBoxService.getWikiTypeList(tenant);
        return new ResponseEntity<>(vos, HttpStatus.OK);
    }

    /**
     * @param tenant tenant
     *
     * @return Void
     */
    @GetMapping("/blogs/publish/all")
    public ResponseEntity<Void> publishAll(@CurrentUser Tenant tenant) throws Exception {
        wikiViewService.publishAll(tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param bid bid
     * @param tenant tenant
     *
     * @return Void
     */
    @GetMapping("/blogs/publish/{bid}")
    public ResponseEntity<Void> publish(@PathVariable(value = "bid") String bid, @CurrentUser Tenant tenant) throws Exception {
        wikiViewService.publish(bid, tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param name name
     * @param tenant tenant
     *
     * @return Void
     */
    @PutMapping("/docs/{wiki_name}")
    public ResponseEntity<Void> putWikiName(@PathVariable(value = "wiki_name") String name, @CurrentUser Tenant tenant) {
        wikiViewService.putWiki();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param tenant tenant
     *
     * @return BlogVo
     */
    @GetMapping("/wiki/count")
    public ResponseEntity<WikiVo> getWikiCount(@CurrentUser Tenant tenant) {

        long count = wikiViewService.getWikiCount(tenant);
        WikiVo vo = WikiVo.builder().count(count).build();

        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

}
