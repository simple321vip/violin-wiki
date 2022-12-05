package cn.violin.wiki.dao;

import cn.violin.wiki.entity.BlogSeq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogSeqRepo extends JpaRepository<BlogSeq, Integer> {
}
