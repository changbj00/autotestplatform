package com.autotestplatform.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.autotestplatform.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {
    public String getToken(User user){
        String token="";
        token = JWT.create().withAudience(user.getEmail())
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }
}
