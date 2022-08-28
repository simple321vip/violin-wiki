package cn.violin.home.book.Interceptor;

import cn.violin.home.book.annotation.PassToken;
import cn.violin.home.book.annotation.UserLoginToken;
import cn.violin.home.book.utils.RedisUtils;

import cn.violin.home.book.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

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
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    TenantService tenantService;

    @Autowired
    private RedisUtils redis;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        String authorization = req.getHeader("Authorization");
        String id = req.getHeader("id");
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
        // ユーザー権限アノテーションの検証
//        if (method.isAnnotationPresent(UserLoginToken.class)) {
//            UserLoginToken userToken = method.getAnnotation(UserLoginToken.class);
//            if (userToken.required()) {
                // ユーザトーケン認証
                if (authorization == null || id == null) {
                    throw new RuntimeException("token or id is null, please login again");
                }
                String tokenNullable = redis.get(id);

                if (tokenNullable == null) {
                    throw new RuntimeException("token is expire, please login again");
                }
                if (!authorization.contains(tokenNullable)) {
                    throw new RuntimeException("token is wrong, please login again");
                }
//                return true;
//            }
//         }
        return true;
    }
}
