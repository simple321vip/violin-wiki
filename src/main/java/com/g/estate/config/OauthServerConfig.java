package com.g.estate.config;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;

@Configuration
@EnableAuthorizationServer
public class OauthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthorizationManager authorizationManager;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;


//    @Bean
//    public ClientDetailsService clientDetailsService() {
//        InMemoryClientDetailsService inMemoryClientDetailsService = new InMemoryClientDetailsService();
//        BaseClientDetails baseClientDetails = new BaseClientDetails();
//        baseClientDetails.setClientId("WksD0FOVAp8zSRV62qNKxtnCexArVPOf");
//    }

}
