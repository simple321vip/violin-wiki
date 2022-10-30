package cn.violin.home.book.Interceptor;

import cn.violin.home.book.annotation.PassToken;

import cn.violin.home.book.service.TenantService;
import cn.violin.home.book.utils.JedisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Interceptor トーケンの検証
 * work flow
 * 1.method check
 * 2.PassToken annotation check
 * 3.UserLoginToken annotation check
 *  3.1 token is null, throw exception
 *  3.2 to query token from redis and compare
 *
 *
 */
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    TenantService tenantService;

    @Autowired
    private JedisUtils redis;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {

        // メソッド以外は通る
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        // passTokenアノテーションの検証
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }

        String authorization = req.getHeader("Authorization");
        String tenantId = req.getHeader("tenantId");

        if (StringUtils.hasLength(authorization) && (authorization.split(":").length == 2) && StringUtils.hasLength(tenantId)) {
            String token = authorization.split(":")[1];
            Optional<String> tokenNullable = redis.get(tenantId);
            if (tokenNullable.isPresent() && tokenNullable.get().equals(token)) {
                return true;
            }
            log.info("token is wrong");
            return false;
        }
        if (!StringUtils.hasLength(authorization)) {
            log.info("authorization info is null or empty");
        } else if (!StringUtils.hasLength(tenantId)) {
            log.info("tenantId info is null or empty");
        }
        return false;
    }
}
