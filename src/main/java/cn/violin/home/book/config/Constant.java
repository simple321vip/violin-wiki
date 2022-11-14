package cn.violin.home.book.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class Constant {

    @Value("${constant.docsify-workspace}")
    private String DOCSIFY_WORKSPACE;
}
