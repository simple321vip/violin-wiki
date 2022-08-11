package cn.violin.home.book.dao;

import cn.violin.home.book.entity.BookmarkType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkTypeRepo extends JpaRepository<BookmarkType, Long> {
}
