package cn.violin.home.book.controller;

import cn.violin.home.book.annotation.CurrentUser;
import cn.violin.home.book.entity.Tenant;
import cn.violin.home.book.service.BlogEditService;
import cn.violin.home.book.io.BlogIn;
import cn.violin.home.book.io.BlogTypeIn;
import cn.violin.home.book.vo.BlogBoxVo;
import cn.violin.home.book.vo.BlogContent;
import cn.violin.home.book.vo.BlogVo;
import cn.violin.home.book.vo.ResultVo;
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
    public ResponseEntity<BlogVo> newBlog(@Valid @RequestBody BlogIn input) {
        return new ResponseEntity<>(blogEditService.insertContent(input), HttpStatus.OK);
    }

    @PostMapping("/blog/content")
    @ResponseBody
    public ResponseEntity<Void> updateBlog(@Valid @RequestBody BlogIn input) {
        blogEditService.updateContent(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/blog/{bid}")
    @ResponseBody
    public ResponseEntity<ResultVo> deleteBlog(@PathVariable(value = "bid") String bid, @Valid @RequestBody BlogIn input) {
        boolean result = blogEditService.deleteContent(bid, input.getBtId());
        return new ResponseEntity<>(ResultVo.builder().processStatus(result).build(), HttpStatus.OK);
    }

    @PostMapping(value = "/blogs/{btId}", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> sortBlog(@PathVariable(value = "btId") String btId, @Valid @RequestBody() BlogIn[] input) {

        blogEditService.sortBlogFromFront(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/blog/list")
    @ResponseBody
    public ResponseEntity<List<BlogBoxVo>> blogView(@CurrentUser Tenant user) {

        return new ResponseEntity<>(blogEditService.listAll(user.getTenantId()), HttpStatus.OK);
    }

    @PutMapping(value = "/blog_type", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<BlogBoxVo> createBlogType(@Valid @RequestBody() BlogTypeIn input, @CurrentUser Tenant user) {
        BlogBoxVo vo = blogEditService.insertBlogType(input, user.getTenantId());
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @DeleteMapping(value = "/blog_type", produces = {"application/json"})
    @ResponseBody
    public ResponseEntity<Void> deleteBlogType(@Valid @RequestBody() BlogTypeIn input) {
        try {
            blogEditService.deleteBlogType(input.getBtId());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
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
