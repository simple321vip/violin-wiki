package cn.violin.wiki.controller;

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
 * 业务模块：essays（文章）
 * ============================
 *
 * <p>URL：{@code /api/v1/essays}（复数 + kebab-case）。</p>
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

    /** 创建 / 自动保存（带 id 走 update，否则 insert）。 */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EssayResult create(@Valid @RequestBody EssayRequest request) {
        return service.autoSave(request);
    }

    @PutMapping("/{id}")
    public EssayResult update(@PathVariable String id,
                              @Valid @RequestBody EssayRequest request) {
        request.setId(id);
        return service.autoSave(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    /** 文章排序（按 categoryId 内的 sort_order 重排）。 */
    @PostMapping("/sort")
    public void sort(@RequestParam String categoryId,
                     @Valid @RequestBody SortRequest request) {
        service.sortEssays(categoryId, request.getIds());
    }
}