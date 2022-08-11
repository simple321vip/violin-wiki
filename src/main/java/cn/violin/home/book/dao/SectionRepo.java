package cn.violin.home.book.dao;

import cn.violin.home.book.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepo extends JpaRepository<Section, Long> {
}
