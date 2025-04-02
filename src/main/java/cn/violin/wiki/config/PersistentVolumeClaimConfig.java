package cn.violin.wiki.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class PersistentVolumeClaimConfig {

    @Value("${kubernetes.persistent-volume-claim.docsify-data-volume}")
    private String DOCSIFY_PVC;

    @Value("${kubernetes.persistent-volume-claim.nginx-static-data-volume}")
    private String NGINX_PVC;
}