package cn.violin.home.book.dao;

import cn.violin.home.book.entity.AuthMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthMasterRepo extends JpaRepository<AuthMaster, String> {
}
