package cn.violin.wiki.mapper;

import cn.violin.wiki.entity.EssayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * Article mapper. Multi-tenant filtering and soft-delete are applied automatically by the
 * MyBatis Plus interceptors configured in {@code violin-core}.
 */
@Mapper
public interface EssayMapper extends BaseMapper<EssayEntity> {

    @Select("SELECT * FROM essays WHERE category_id = #{categoryId} AND deleted_at IS NULL ORDER BY sort_order, created_at")
    List<EssayEntity> selectByCategoryId(@Param("categoryId") String categoryId);

    @Update({
        "<script>",
        "UPDATE essays SET sort_order = CASE id ",
        "<foreach collection='idToOrder' item='v' index='k' separator=''>",
        "WHEN #{k} THEN #{v} ",
        "</foreach>",
        "END ",
        "WHERE customer_id = #{customerId} ",
        "AND id IN ",
        "<foreach collection='idToOrder.keys' item='id' open='(' separator=',' close=')'>",
        "#{id}",
        "</foreach>",
        "</script>"
    })
    int batchUpdateSortOrder(@Param("idToOrder") Map<String, Integer> idToOrder,
                             @Param("customerId") String customerId);
}