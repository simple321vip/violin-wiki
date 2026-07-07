package cn.violin.wiki.mapper;

import cn.violin.wiki.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Category mapper. Multi-tenant filtering and soft-delete are applied automatically by the
 * MyBatis Plus interceptors configured in {@code violin-core}.
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryEntity> {
}