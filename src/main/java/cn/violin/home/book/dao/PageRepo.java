package cn.violin.home.book.dao;

import cn.violin.home.book.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepo extends JpaRepository<Page, Long> {
}
