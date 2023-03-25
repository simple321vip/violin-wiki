package cn.violin.wiki.controller;

import cn.violin.wiki.io.SortData;
import cn.violin.wiki.service.BlogEditService;
import cn.violin.wiki.io.BlogIn;
import cn.violin.wiki.io.BlogTypeIn;
import cn.violin.wiki.vo.BlogBoxVo;
import cn.violin.wiki.vo.BlogVo;
import cn.violin.common.annotation.CurrentUser;
import cn.violin.common.entity.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
@RequestMapping("/api/v1/author")
public class BlogEditCtl {

    @Autowired
    private BlogEditService blogEditService;

    @PostMapping(value = "/wiki/type", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<BlogBoxVo> createBlogType(@Valid @RequestBody() BlogTypeIn input,
        @CurrentUser Tenant tenant) {
        BlogBoxVo vo = blogEditService.insertBlogType(input, tenant);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @DeleteMapping(value = "/wiki/type", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<BlogBoxVo>> deleteBlogType(@Valid @RequestBody() BlogTypeIn input,
        @CurrentUser Tenant tenant) {
        try {
            List<BlogBoxVo> result = blogEditService.deleteBlogType(input.getBtId(), tenant);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/wiki/type", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> updateWikiType(@Valid @RequestBody() BlogTypeIn input, @CurrentUser Tenant tenant) {
        blogEditService.updateWikiType(input, tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/wiki/type/{btId}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<BlogBoxVo> getWikiList(@NotNull @PathVariable(value = "btId") String btId,
        @CurrentUser Tenant tenant) {

        BlogBoxVo vo = blogEditService.getWikiList(btId, tenant);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @GetMapping(value = "/wiki/type", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<BlogBoxVo>> getWikiTypeList(@CurrentUser Tenant tenant) {

        List<BlogBoxVo> vo = blogEditService.getWikiTypeList(tenant);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @PostMapping(value = "/wiki/type/sort", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<BlogBoxVo>> sortBlogType(@Valid @RequestBody() SortData input,
        @CurrentUser Tenant tenant) {

        List<BlogBoxVo> vo = blogEditService.sortBlogType(input, tenant);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    /**
     * bidで該当blogの内容を取得する
     */
    @GetMapping("/wiki/{id}")
    @ResponseBody
    public ResponseEntity<BlogVo> getBlogContent(@PathVariable(value = "id") String id, @CurrentUser Tenant tenant)
        throws Exception {
        BlogVo result = blogEditService.getWiki(id, tenant);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/blog/content")
    @ResponseBody
    public ResponseEntity<BlogVo> newBlog(@Valid @RequestBody BlogIn input, @CurrentUser Tenant tenant) {
        return new ResponseEntity<>(blogEditService.insertContent(input, tenant), HttpStatus.OK);
    }

    @PostMapping("/blog/content")
    @ResponseBody
    public ResponseEntity<Void> updateBlog(@Valid @RequestBody BlogIn input) {
        blogEditService.updateContent(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/wiki")
    @ResponseBody
    public ResponseEntity<BlogBoxVo> deleteBlog(@Valid @RequestBody BlogIn input, @CurrentUser Tenant user)
        throws Exception {
        BlogBoxVo result = blogEditService.deleteWiki(input, user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/blogs/{btId}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> sortBlog(@PathVariable(value = "btId") String btId,
        @Valid @RequestBody SortData sortData, @CurrentUser Tenant tenant) {

        blogEditService.sortWiki(sortData, tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/blog/list")
    @ResponseBody
    public ResponseEntity<List<BlogBoxVo>> blogView(@CurrentUser Tenant tenant) {

        return new ResponseEntity<>(blogEditService.listAll(tenant), HttpStatus.OK);
    }
}
