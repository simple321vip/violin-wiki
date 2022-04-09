package com.g.estate.dao;

import com.g.estate.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepo extends JpaRepository<Section, Long> {
}
