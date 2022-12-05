package cn.violin.wiki.dao;

import cn.violin.wiki.entity.BlogTypeSeq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogTypeSeqRepo extends JpaRepository<BlogTypeSeq, Integer> {
}
