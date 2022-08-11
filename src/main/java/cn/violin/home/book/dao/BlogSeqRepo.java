package cn.violin.home.book.dao;

import cn.violin.home.book.entity.BlogSeq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogSeqRepo extends JpaRepository<BlogSeq, Integer> {
}
