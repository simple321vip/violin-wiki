package cn.violin.home.book.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

@Configuration
public class DatabaseConfig {

//    @Bean
//    public DataSource dataSource() {
//        return new EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.H2)
//                .setScriptEncoding("UTF-8")
//                .addScript("xxx.sql")
//                .build();
//    }
    public static final Properties properties = System.getProperties();
    static {
        properties.setProperty("BLOG_FOLDER_HOME", "E:\\Project\\g-estate");
    }
}
