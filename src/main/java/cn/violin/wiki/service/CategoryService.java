package cn.violin.wiki.service;

import cn.violin.wiki.dto.params.CategoryRequest;
import cn.violin.wiki.dto.result.CategoryResult;

import java.util.List;

/**
 * 分类业务接口。
 */
public interface CategoryService {

    CategoryResult create(CategoryRequest request);

    List<CategoryResult> list();

    CategoryResult get(String id);

    CategoryResult update(String id, CategoryRequest request);

    void delete(String id);

    void sortCategories(String[] ids);
}