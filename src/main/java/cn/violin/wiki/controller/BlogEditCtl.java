package cn.violin.wiki.controller;

import cn.violin.wiki.service.BlogEditService;
import cn.violin.wiki.io.BlogIn;
import cn.violin.wiki.io.BlogTypeIn;
import cn.violin.wiki.vo.BlogBoxVo;
import cn.violin.wiki.vo.BlogContent;
import cn.violin.wiki.vo.BlogVo;
import cn.violin.common.annotation.CurrentUser;
import cn.violin.common.entity.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/api/v1/author")
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
    public ResponseEntity<BlogVo> newBlog(@Valid @RequestBody BlogIn input, @CurrentUser Tenant tenant) {
        return new ResponseEntity<>(blogEditService.insertContent(input, tenant), HttpStatus.OK);
    }

    @PostMapping("/blog/content")
    @ResponseBody
    public ResponseEntity<Void> updateBlog(@Valid @RequestBody BlogIn input) {
        blogEditService.updateContent(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/blog/{bid}")
    @ResponseBody
    public ResponseEntity<BlogBoxVo> deleteBlog(@PathVariable(value = "bid") String bid, @Valid @RequestBody BlogIn input, @CurrentUser Tenant user) throws Exception {
        input.setBid(bid);
        BlogBoxVo result = blogEditService.deleteContent(input, user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/blogs/{btId}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> sortBlog(@PathVariable(value = "btId") String btId, @Valid @RequestBody() BlogIn[] input) {

        blogEditService.sortBlogFromFront(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/blog/list")
    @ResponseBody
    public ResponseEntity<List<BlogBoxVo>> blogView(@CurrentUser Tenant tenant) {

        return new ResponseEntity<>(blogEditService.listAll(tenant), HttpStatus.OK);
    }

    @PutMapping(value = "/blog_type", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<BlogBoxVo> createBlogType(@Valid @RequestBody() BlogTypeIn input, @CurrentUser Tenant tenant) {
        BlogBoxVo vo = blogEditService.insertBlogType(input, tenant);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @DeleteMapping(value = "/blog_type", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<List<BlogBoxVo>> deleteBlogType(@Valid @RequestBody() BlogTypeIn input, @CurrentUser Tenant tenant) {
        try {
            List<BlogBoxVo> result = blogEditService.deleteBlogType(input.getBtId(), tenant);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/blog_type", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> updateBlogType(@Valid @RequestBody() BlogTypeIn input) {
        blogEditService.updateBlogTypeName(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/blog_types", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> sortBlogType(@Valid @RequestBody() BlogTypeIn[] input) {

        blogEditService.sortBlogType(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
