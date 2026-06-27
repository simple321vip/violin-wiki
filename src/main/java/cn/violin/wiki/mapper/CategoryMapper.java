package cn.violin.wiki.mapper;

import cn.violin.wiki.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类 Mapper。多租户 / 软删除由 MyBatis Plus 拦截器自动处理。
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryEntity> {
}