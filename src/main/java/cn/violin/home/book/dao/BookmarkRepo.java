package cn.violin.home.book.dao;

import cn.violin.home.book.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepo extends JpaRepository<Bookmark, Long> {
}
