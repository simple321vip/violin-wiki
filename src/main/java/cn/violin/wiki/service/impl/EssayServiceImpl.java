package cn.violin.wiki.service.impl;

import cn.violin.common.context.RequestContext;
import cn.violin.common.exception.BusinessException;
import cn.violin.common.exception.ResourceNotFoundException;
import cn.violin.wiki.dto.params.EssayRequest;
import cn.violin.wiki.dto.result.EssayResult;
import cn.violin.wiki.entity.CategoryEntity;
import cn.violin.wiki.entity.EssayEntity;
import cn.violin.wiki.mapper.CategoryMapper;
import cn.violin.wiki.mapper.EssayMapper;
import cn.violin.wiki.service.EssayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 文章业务实现。
 */
@Service
@RequiredArgsConstructor
public class EssayServiceImpl implements EssayService {

    private final EssayMapper essayMapper;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public EssayResult get(String id) {
        EssayEntity e = essayMapper.selectById(id);
        if (e == null) throw new ResourceNotFoundException(id);
        return EssayResult.from(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EssayResult> listByCategory(String categoryId) {
        return essayMapper.selectByCategoryId(categoryId).stream()
            .map(EssayResult::from)
            .toList();
    }

    @Override
    @Transactional
    public EssayResult autoSave(EssayRequest request) {
        CategoryEntity category = categoryMapper.selectById(request.getCategoryId());
        if (category == null) throw new ResourceNotFoundException(request.getCategoryId());

        EssayEntity e = new EssayEntity();
        if (request.getId() != null && !request.getId().isBlank()) {
            EssayEntity existing = essayMapper.selectById(request.getId());
            if (existing == null) throw new ResourceNotFoundException(request.getId());
            e.setId(existing.getId());
            e.setCreatedBy(existing.getCreatedBy());
            e.setCreatedAt(existing.getCreatedAt());
            e.setCustomerId(existing.getCustomerId());
        } else {
            e.setId(UUID.randomUUID().toString());
            e.setCustomerId(currentCustomerId());
            e.setCreatedBy(currentUserId());
        }
        e.setCategoryId(request.getCategoryId());
        e.setTitle(request.getTitle());
        e.setContent(request.getContent());
        e.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0);
        e.setUpdatedBy(currentUserId());

        if (request.getId() != null && !request.getId().isBlank()) {
            essayMapper.updateById(e);
        } else {
            essayMapper.insert(e);
        }
        return EssayResult.from(e);
    }

    @Override
    @Transactional
    public void delete(String id) {
        EssayEntity existing = essayMapper.selectById(id);
        if (existing == null) throw new ResourceNotFoundException(id);

        essayMapper.deleteById(id);

        List<EssayEntity> remaining = essayMapper.selectByCategoryId(existing.getCategoryId());
        for (int i = 0; i < remaining.size(); i++) {
            EssayEntity e = remaining.get(i);
            e.setSortOrder(i + 1);
            essayMapper.updateById(e);
        }
    }

    @Override
    @Transactional
    public void sortEssays(String categoryId, String[] ids) {
        if (ids == null || ids.length == 0) {
            throw new BusinessException("COMMON_INVALID_PARAM", "排序ID数组不能为空");
        }

        java.util.Set<String> dedup = new java.util.HashSet<>(java.util.Arrays.asList(ids));
        if (dedup.size() != ids.length) {
            throw new BusinessException("COMMON_INVALID_PARAM", "排序ID数组存在重复值");
        }

        java.util.Map<String, Integer> idToOrder = new HashMap<>(ids.length);
        for (int i = 0; i < ids.length; i++) {
            idToOrder.put(ids[i], i + 1);
        }

        int updated = essayMapper.batchUpdateSortOrder(idToOrder, currentCustomerId());
        if (updated != ids.length) {
            throw new BusinessException("COMMON_OPERATION_FAILED", "排序更新失败，影响行数与预期不符");
        }
    }

    private String currentCustomerId() {
        String id = RequestContext.getCustomerId();
        if (id == null) throw new IllegalStateException("RequestContext.customerId 未设置");
        return id;
    }

    private String currentUserId() {
        String u = RequestContext.getUserId();
        return u == null ? "system" : u;
    }
}