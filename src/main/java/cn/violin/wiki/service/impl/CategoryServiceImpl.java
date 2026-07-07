package cn.violin.wiki.service.impl;

import cn.violin.common.context.RequestContext;
import cn.violin.common.exception.BusinessException;
import cn.violin.common.exception.ResourceNotFoundException;
import cn.violin.wiki.dto.params.CategoryRequest;
import cn.violin.wiki.dto.result.CategoryResult;
import cn.violin.wiki.entity.CategoryEntity;
import cn.violin.wiki.entity.EssayEntity;
import cn.violin.wiki.mapper.CategoryMapper;
import cn.violin.wiki.mapper.EssayMapper;
import cn.violin.wiki.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Category business implementation.
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final EssayMapper essayMapper;

    @Override
    @Transactional
    public CategoryResult create(CategoryRequest request) {
        CategoryEntity c = new CategoryEntity();
        c.setId(UUID.randomUUID().toString());
        c.setCustomerId(currentCustomerId());
        c.setCategoryName(request.getCategoryName().strip());
        c.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        c.setCreatedBy(currentUserId());
        c.setUpdatedBy(currentUserId());
        categoryMapper.insert(c);
        return CategoryResult.from(c);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResult> list() {
        return categoryMapper.selectList(
            new LambdaQueryWrapper<CategoryEntity>().orderByAsc(CategoryEntity::getSortOrder)
        ).stream().map(CategoryResult::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResult get(String id) {
        CategoryEntity c = categoryMapper.selectById(id);
        if (c == null) throw new ResourceNotFoundException(id);
        return CategoryResult.from(c);
    }

    @Override
    @Transactional
    public CategoryResult update(String id, CategoryRequest request) {
        CategoryEntity existing = categoryMapper.selectById(id);
        if (existing == null) throw new ResourceNotFoundException(id);

        existing.setCategoryName(request.getCategoryName());
        existing.setSortOrder(request.getSortOrder());
        existing.setUpdatedBy(currentUserId());
        categoryMapper.updateById(existing);
        return CategoryResult.from(existing);
    }

    @Override
    @Transactional
    public void delete(String id) {
        CategoryEntity existing = categoryMapper.selectById(id);
        if (existing == null) throw new ResourceNotFoundException(id);

        List<CategoryEntity> all = categoryMapper.selectList(null);
        if (all.size() == 1) {
            throw new BusinessException("COMMON_OPERATION_FAILED", "Cannot delete: keep at least one category");
        }

        categoryMapper.deleteById(id);
        essayMapper.delete(new LambdaQueryWrapper<EssayEntity>().eq(EssayEntity::getCategoryId, id));

        for (int i = 0; i < all.size() - 1; i++) {
            CategoryEntity c = all.get(i);
            if (!c.getId().equals(id)) {
                c.setSortOrder(i + 1);
                categoryMapper.updateById(c);
            }
        }
    }

    @Override
    @Transactional
    public void sortCategories(String[] ids) {
        if (ids == null || ids.length == 0) {
            throw new BusinessException("COMMON_INVALID_PARAM", "sort ids must not be empty");
        }

        List<CategoryEntity> all = categoryMapper.selectList(null);
        if (all.size() != ids.length) {
            throw new BusinessException("COMMON_OPERATION_FAILED", "data incomplete, cannot sort");
        }

        java.util.Map<String, CategoryEntity> idToCategory = new java.util.HashMap<>();
        for (CategoryEntity c : all) {
            idToCategory.put(c.getId(), c);
        }

        for (int i = 0; i < ids.length; i++) {
            CategoryEntity c = idToCategory.get(ids[i]);
            if (c == null) {
                throw new BusinessException("COMMON_INVALID_PARAM", "sort id not found: " + ids[i]);
            }
            c.setSortOrder(i + 1);
            categoryMapper.updateById(c);
        }
    }

    private String currentCustomerId() {
        String id = RequestContext.getCustomerId();
        if (id == null) throw new IllegalStateException("RequestContext.customerId is not set");
        return id;
    }

    private String currentUserId() {
        String u = RequestContext.getUserId();
        return u == null ? "system" : u;
    }
}