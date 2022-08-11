package cn.violin.home.book.dao;

import cn.violin.home.book.entity.BookmarkSeq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkSeqRepo extends JpaRepository<BookmarkSeq, Integer> {
}
