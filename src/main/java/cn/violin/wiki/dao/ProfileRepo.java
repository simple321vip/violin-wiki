package cn.violin.wiki.dao;

import cn.violin.wiki.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepo extends JpaRepository<Profile, String> {
}
