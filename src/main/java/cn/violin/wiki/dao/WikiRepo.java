package cn.violin.wiki.dao;

import cn.violin.wiki.entity.Wiki;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WikiRepo extends JpaRepository<Wiki, String> {

    @Query(value = "SELECT * FROM T_WIKI WHERE bid = :bid　AND tenant_id = :tenant_id", nativeQuery = true)
    Wiki findByBid(@Param("bid") String bid, @Param("tenant_id") String tenantId);

    @Query(value = "SELECT COUNT(*) FROM T_WIKI WHERE btId = :btId　AND tenant_id = :tenant_id", nativeQuery = true)
    long count(@Param("btId") String btId, @Param("tenant_id") String tenantId);

    @Query(value = "SELECT COUNT(*) FROM T_WIKI WHERE tenant_id = :tenant_id", nativeQuery = true)
    long count(@Param("tenant_id") String tenantId);

    @Query(value = "SELECT * FROM T_WIKI WHERE tenant_id = :tenant_id ORDER BY order ASC", nativeQuery = true)
    List<Wiki> findAll(@Param("tenant_id") String tenantId);

    @Query(value = "SELECT * FROM T_WIKI WHERE btId = :btId AND tenant_id = :tenant_id ORDER BY order ASC", nativeQuery = true)
    List<Wiki> findAllByBtId(@Param("btId") String btId, @Param("tenant_id") String tenantId);

    @Query(value = "SELECT * FROM T_WIKI WHERE btId = :btId AND tenant_id = :tenant_id AND order > :order ORDER BY order ASC", nativeQuery = true)
    List<Wiki> findAllByBtIdAndOrder(@Param("btId") String btId, @Param("tenant_id") String tenantId, @Param("order") int order);
}
