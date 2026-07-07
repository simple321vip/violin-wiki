package cn.violin.wiki.controller;

import cn.violin.wiki.annotation.RequirePermission;
import cn.violin.wiki.dto.params.EssayRequest;
import cn.violin.wiki.dto.result.EssayResult;
import cn.violin.wiki.dto.params.SortRequest;
import cn.violin.wiki.service.EssayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ============================
 * Domain module: essays (article)
 * ============================
 *
 * <p>URL: {@code /api/v1/essays} (plural + kebab-case).</p>
 */
@RestController
@RequestMapping("/api/v1/essays")
@RequiredArgsConstructor
public class EssayController {

    private final EssayService service;

    @GetMapping("/{id}")
    public EssayResult get(@PathVariable String id) {
        return service.get(id);
    }

    @GetMapping
    public List<EssayResult> list(@RequestParam(required = false) String categoryId) {
        return service.listByCategory(categoryId);
    }

    /** Create / auto-save (with id -> update, otherwise insert). */
    @PostMapping
    @RequirePermission(resource = "essay", verb = "create")
    @ResponseStatus(HttpStatus.CREATED)
    public EssayResult create(@Valid @RequestBody EssayRequest request) {
        return service.autoSave(request);
    }

    @PutMapping("/{id}")
    @RequirePermission(resource = "essay", verb = "update")
    public EssayResult update(@PathVariable String id,
                              @Valid @RequestBody EssayRequest request) {
        request.setId(id);
        return service.autoSave(request);
    }

    @DeleteMapping("/{id}")
    @RequirePermission(resource = "essay", verb = "delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    /** Reorder essays within a category (rewrites sort_order by ids order). */
    @PostMapping("/sort")
    @RequirePermission(resource = "essay", verb = "reorder")
    public void sort(@RequestParam String categoryId,
                     @Valid @RequestBody SortRequest request) {
        service.sortEssays(categoryId, request.getIds());
    }
}