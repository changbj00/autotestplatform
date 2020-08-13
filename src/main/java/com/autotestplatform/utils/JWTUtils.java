package com.autotestplatform.utils;

import com.alibaba.druid.support.json.JSONUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
@Deprecated
public class JWTUtils {

    /*
     * jwt就是基于json签发token和校验token的一种机制。主要功能是权限验证和存储加密的信息。
     * jwt由3部分组成(base64解密工具可以解密)：
     * eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
     * eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.
     * SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
     * header(头信息):解密为{"alg": "HS256","typ": "JWT"}是算法和类型。
     * playload(荷载):解密为{"sub": "1234567890","name": "John Doe","iat": 1516239022} 载荷就是存放有效信息的地方。
     * verify signature(校验签名):由 base64UrlEncode(header) + "." +base64UrlEncode(payload)。
     */

    private JWTUtils() {
    }

    private final static Logger logger = LoggerFactory.getLogger(JWTUtils.class);

    //在验证或签名实例中使用的密钥(自定义和密码加盐差不多)
    private static final String SECRET = "123";

    private static final String KEYID = "123";

    //使用HS256算法
    private static Algorithm algorithm = Algorithm.HMAC256(SECRET);

    /**
     * 加密
     *
     * @param name
     * @param secret
     * @param data
     */
    public static String encrypt(String name, String data) {
        try {
            //通过调用 JWT.create()来创建 jwt实例
            JWTCreator.Builder builder = JWT.create();
            builder.withJWTId(KEYID);
            //设置过期时间一个小时
            builder.withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000));
            //索赔:添加自定义声明值,完成荷载的信息
            builder.withClaim(name, data);
            //签署:调用sign()传递算法实例
            return builder.sign(algorithm);
        } catch (JWTCreationException e) {
            logger.error("无效的签名配置！", e.getMessage());
        }
        return null;
    }


    /***
     *校验
     * @param token
     * @param value
     * @return json
     */
    public static String verify(String token, String key) {
        try {
            //这将用于验证令牌的签名
            JWTVerifier verifier = JWT.require(algorithm).build();
            //针对给定令牌执行验证
            DecodedJWT jwt = verifier.verify(token);
            //获取令牌中定义的声明
            Map<String, Claim> claims = jwt.getClaims();
            //返回指定键映射到的值
            return claims.get(key).asString();
        } catch (JWTVerificationException e) {
            logger.error("校验失败或token已过期!", e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(encrypt("123", "123"));
        System.out.println(verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyIxMjMiOiIxMjMiLCJleHAiOjE1OTcyMTI0OTcsImp0aSI6IjEyMyJ9.uSSdtBqDSiqCPRD72uwfG1o8qp1ndAu-TCBXxTzg-1M", "123"));
    }
}