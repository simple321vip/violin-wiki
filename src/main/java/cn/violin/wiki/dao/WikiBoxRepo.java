package cn.violin.wiki.dao;

import cn.violin.wiki.entity.WikiBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WikiBoxRepo extends JpaRepository<WikiBox, String> {

    @Query(value = "SELECT EXISTS (SELECT 1 FROM T_WIKI_BOX WHERE btId = :btId　AND tenant_id = :tenant_id)", nativeQuery = true)
    boolean exists(@Param("btId") String btId, @Param("tenant_id") String tenantId);

    @Query(value = "SELECT EXISTS (SELECT 1 FROM T_WIKI_BOX WHERE tenant_id = :tenant_id)", nativeQuery = true)
    long count(@Param("tenant_id") String tenantId);

    @Query(value = "SELECT * FROM T_WIKI_BOX WHERE tenant_id = :tenant_id ORDER BY order ASC", nativeQuery = true)
    List<WikiBox> findAll(@Param("tenant_id") String tenantId);

    @Query(value = "SELECT MAX(order) FROM T_WIKI_BOX WHERE tenant_id = :tenant_id", nativeQuery = true)
    int getMaxOrder(@Param("tenant_id") String tenantId);

    @Query(value = "SELECT * FROM T_WIKI_BOX WHERE btId = :btId AND tenant_id = :tenant_id", nativeQuery = true)
    WikiBox findByBtIdAndTenantId(@Param("btId") String btId, @Param("tenant_id") String tenantId);

    @Query(value = "SELECT * FROM T_WIKI_BOX WHERE tenant_id = :tenant_id ORDER BY order ASC", nativeQuery = true)
    String[] findBtIdsWithOrder(@Param("tenant_id") String tenantId);
}
