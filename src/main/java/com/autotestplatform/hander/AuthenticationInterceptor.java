package com.autotestplatform.hander;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.autotestplatform.api.PassToken;
import com.autotestplatform.api.UserLoginToken;
import com.autotestplatform.entity.User;
import com.autotestplatform.service.UserService;
import com.autotestplatform.utils.RequestResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * handler拦截器，查看请求头是否需要校验token
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String usertoken = request.getHeader("usertoken");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required())
                return true;
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (usertoken == null) {
                    //return false;
                    throw new CatchException(RequestResultEnum.ILLEGAL_ACCESS);
                }
                // 获取 token 中的 user id
                String email;
                try {
                    email = JWT.decode(usertoken).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    //token无效
                    throw new CatchException(RequestResultEnum.ILLEGAL_ACCESS);
                }

                User user = userService.getUser(email);
                if (user == null) {
                    //用户不存在
                    throw new CatchException(RequestResultEnum.ILLEGAL_ACCESS);
                }
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
                try {
                    jwtVerifier.verify(usertoken);
                } catch (JWTVerificationException e) {
                    //token过期
                    throw new CatchException(RequestResultEnum.ILLEGAL_ACCESS);
                }
                return true;
            }
        }
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

