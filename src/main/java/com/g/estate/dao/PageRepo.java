package com.g.estate.dao;

import com.g.estate.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepo extends JpaRepository<Page, Long> {
}
