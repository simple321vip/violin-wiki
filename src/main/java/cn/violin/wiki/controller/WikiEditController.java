package cn.violin.wiki.controller;

import cn.violin.wiki.io.SortData;
import cn.violin.wiki.service.WikiEditService;
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

/**
 * =====================================================================================================================
 * 
 * {@link #createWikiType W001} 创建wiki分类 <br>
 * {@link #deleteWikiType W002} 删除wiki分类 <br>
 * {@link #updateWikiType W003} 更新wiki分类 <br>
 * {@link #getWikiList W004} 获取该分类下的wiki <br>
 * {@link #getWikiTypeList W005} 获取wiki分类一览 <br>
 * {@link #sortWikiType W006} wiki分类重新排序 <br>
 * {@link #createWiki W007} 创建wiki <br>
 * {@link #deleteWiki W008} 删除wiki <br>
 * {@link #updateWiki W009} 修改wiki <br>
 * {@link #getWiki W010} 获取wiki <br>
 * {@link #sortWiki W011} 对wiki重新排序 <br>
 * {@link #wikiList W012} 页面初次加载时，wiki一览 <br>
 **/
@Controller
@RequestMapping("/api/v1/author")
@CrossOrigin
public class WikiEditController {

    @Autowired
    private WikiEditService wikiEditService;

    @PostMapping(value = "/wiki/type", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<BlogBoxVo> createWikiType(@Valid @RequestBody() BlogTypeIn input,
        @CurrentUser Tenant tenant) {
        BlogBoxVo vo = wikiEditService.insertBlogType(input, tenant);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @DeleteMapping(value = "/wiki/type", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<BlogBoxVo>> deleteWikiType(@Valid @RequestBody() BlogTypeIn input,
        @CurrentUser Tenant tenant) {
        try {
            List<BlogBoxVo> result = wikiEditService.deleteBlogType(input.getBtId(), tenant);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/wiki/type", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> updateWikiType(@Valid @RequestBody() BlogTypeIn input, @CurrentUser Tenant tenant) {
        wikiEditService.updateWikiType(input, tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/wiki/type/{btId}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<BlogBoxVo> getWikiList(@NotNull @PathVariable(value = "btId") String btId,
        @CurrentUser Tenant tenant) {

        BlogBoxVo vo = wikiEditService.getWikiList(btId, tenant);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @GetMapping(value = "/wiki/type", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<BlogBoxVo>> getWikiTypeList(@CurrentUser Tenant tenant) {

        List<BlogBoxVo> vo = wikiEditService.getWikiTypeList(tenant);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @PostMapping(value = "/wiki/type/sort", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<BlogBoxVo>> sortWikiType(@Valid @RequestBody() SortData input,
        @CurrentUser Tenant tenant) {

        List<BlogBoxVo> vo = wikiEditService.sortBlogType(input, tenant);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @PostMapping("/wiki")
    @ResponseBody
    public ResponseEntity<BlogVo> createWiki(@Valid @RequestBody BlogIn input, @CurrentUser Tenant tenant) {
        return new ResponseEntity<>(wikiEditService.insertContent(input, tenant), HttpStatus.OK);
    }

    @DeleteMapping("/wiki")
    @ResponseBody
    public ResponseEntity<BlogBoxVo> deleteWiki(@Valid @RequestBody BlogIn input, @CurrentUser Tenant user)
        throws Exception {
        BlogBoxVo result = wikiEditService.deleteWiki(input, user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/wiki")
    @ResponseBody
    public ResponseEntity<Void> updateWiki(@Valid @RequestBody BlogIn input) {
        wikiEditService.updateContent(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/wiki/{id}")
    @ResponseBody
    public ResponseEntity<BlogVo> getWiki(@PathVariable(value = "id") String id, @CurrentUser Tenant tenant)
        throws Exception {
        BlogVo result = wikiEditService.getWiki(id, tenant);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/wiki/{btId}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> sortWiki(@PathVariable(value = "btId") String btId,
        @Valid @RequestBody SortData sortData, @CurrentUser Tenant tenant) {

        wikiEditService.sortWiki(sortData, tenant);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/wiki/list")
    @ResponseBody
    public ResponseEntity<List<BlogBoxVo>> wikiList(@CurrentUser Tenant tenant) {

        return new ResponseEntity<>(wikiEditService.listAll(tenant), HttpStatus.OK);
    }
}
