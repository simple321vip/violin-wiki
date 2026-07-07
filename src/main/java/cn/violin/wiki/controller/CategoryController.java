package cn.violin.wiki.controller;

import cn.violin.wiki.annotation.RequirePermission;
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
 * Domain module: wiki-categories (Wiki category)
 * ============================
 *
 * <p>URL: {@code /api/v1/wiki-categories} (plural + kebab-case, to avoid colliding with the generic
 * {@code categories} namespace).</p>
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
    @RequirePermission(resource = "wiki-category", verb = "create")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResult create(@Valid @RequestBody CategoryRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @RequirePermission(resource = "wiki-category", verb = "update")
    public CategoryResult update(@PathVariable String id,
                                 @Valid @RequestBody CategoryRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @RequirePermission(resource = "wiki-category", verb = "delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    /** Reorder categories. */
    @PostMapping("/sort")
    @RequirePermission(resource = "wiki-category", verb = "reorder")
    public void sort(@Valid @RequestBody SortRequest request) {
        service.sortCategories(request.getIds());
    }
}