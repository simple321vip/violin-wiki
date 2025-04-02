package cn.violin.wiki.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "cn.violin.wiki.dao")
public class JpaConfig {
}
