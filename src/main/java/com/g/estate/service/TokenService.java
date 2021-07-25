package com.g.estate.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.g.estate.entity.User;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public String getToken(User user) {
        String token = JWT.create().withAudience(user.getId())
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }
}
