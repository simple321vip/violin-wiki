package cn.violin.home.book.Interceptor;

import cn.violin.home.book.annotation.PassToken;
import cn.violin.home.book.annotation.UserLoginToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import cn.violin.home.book.entity.Tenant;
import cn.violin.home.book.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Interceptor トーケンの検証
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    TenantService tenantService;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
        String token = req.getHeader("token");
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
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userToken = method.getAnnotation(UserLoginToken.class);
            if (userToken.required()) {
                // ユーザトーケン認証
                if (token == null) {
                    throw new RuntimeException("トーケンがないため、ご登録してください。");
                }
                String userId;
                try {
                    userId = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("401");
                }
//                Optional<Tenant> tenant = tenantService.findUserById(userId);
//                if (!tenant.isPresent()) {
//                    throw new RuntimeException("用户不存在，请重新登录");
//                }
                // トーケン認証
//                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(tenant.get().getPassword())).build();
                JWTVerifier jwtVerifier =null;
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new RuntimeException("401");
                }
                return true;
            }
         }
        return false;
    }
}
