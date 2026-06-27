package cn.violin.wiki.controller;

import cn.violin.wiki.dto.params.CategoryRequest;
import cn.violin.wiki.dto.result.CategoryResult;
import cn.violin.wiki.dto.params.SortRequest;
import cn.violin.wiki.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ============================
 * 业务模块：wiki-categories（Wiki 分类）
 * ============================
 *
 * <p>URL：{@code /api/v1/wiki-categories}（复数 + kebab-case，避免与通用 categories 冲突）。</p>
 */
@RestController
@RequestMapping("/api/v1/wiki-categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public List<CategoryResult> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public CategoryResult get(@PathVariable String id) {
        return service.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResult create(@Valid @RequestBody CategoryRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public CategoryResult update(@PathVariable String id,
                                 @Valid @RequestBody CategoryRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    /** 分类排序。 */
    @PostMapping("/sort")
    public void sort(@Valid @RequestBody SortRequest request) {
        service.sortCategories(request.getIds());
    }
}