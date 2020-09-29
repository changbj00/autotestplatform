package com.autotestplatform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.autotestplatform.entity.User;
import com.autotestplatform.mapper.UserMapper;
import com.autotestplatform.service.UserService;
import com.autotestplatform.utils.RedisUtil;
import com.autotestplatform.utils.RequestResultEnum;
import com.autotestplatform.utils.RestApiResult;
import com.autotestplatform.utils.TokenUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

/**
 * 用户处理service
 */
@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserServiceImpl implements UserService {
    @Autowired
    private RestApiResult restApiResult;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private UserMapper userMapper;

    @DS("slave")
    @Override
    public RestApiResult login(String email, String password) {
        JSONObject object = new JSONObject();
        if (email != null && password != null) {
            password = DigestUtils.md5DigestAsHex(password.getBytes());
            User user = userMapper.login(email, password);
            if (user == null) {
                log.warn("用户名或密码错误:{},{}", email, password);
                return restApiResult.build(RequestResultEnum.login.getCode(), RequestResultEnum.login.getMsg());
            } else {
                log.info("user{}", user);
                String tokenKey = "usertoken:" + user.getEmail();
                String usertoken;
                if (redisUtil.hasKey(tokenKey)) {
                    usertoken = redisUtil.getKey(tokenKey);
                } else {
                    usertoken = tokenUtil.getToken(user);
                    redisUtil.setKey(tokenKey, usertoken, 60);
                }
                object.put("usertoken", usertoken);
                object.put("user", user);
                return restApiResult.success(object);
            }
        }
        return restApiResult.faild();
    }
    @Override
    public User getUser(String email) {
        User user = userMapper.getUser(email);
        if (user!=null)
            return user;
        else
            return null;
            //throw new CatchException(RequestResultEnum.forgot);
    }
    @Override
    public RestApiResult register(User user) {
        JSONObject object = new JSONObject();
        if (user.getEmail() == null || user.getPassword() == null || user.getDepartment() == null || user.getPhone() == null || user.getRole() == null || user.getUsername() == null) {
            return restApiResult.build(RequestResultEnum.user.getCode(), RequestResultEnum.user.getMsg());
        }
        if (user != null) {
            User existUser = getUser(user.getEmail());
            if (existUser != null) {
                log.warn("该用户存在{}", existUser);
                return restApiResult.build(RequestResultEnum.register.getCode(), RequestResultEnum.register.getMsg());
            } else {
                log.info("新用户{}", user);
                user.setId(null);
                user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
                userMapper.register(user);
                String tokenKey = "usertoken:" + user.getEmail();
                String usertoken = tokenUtil.getToken(user);
                redisUtil.setKey(tokenKey, usertoken, 60);
                object.put("usertoken", usertoken);
                object.put("user", user);
                return restApiResult.success(object);
            }
        }
        return restApiResult.faild();
    }
    @Override
    public RestApiResult forgetUser(User user, String code) {
        JSONObject object = new JSONObject();
        if (user.getEmail() == null || user.getPassword() == null) {
            return restApiResult.build(RequestResultEnum.user.getCode(), RequestResultEnum.user.getMsg());
        }
        try {
            String email = user.getEmail();
            if (user != null) {
                User existUser = getUser(email);
                if (existUser != null) {
                    String emailKey = "message:" + email;
                    String tokenKey = "usertoken:" + email;
                    String getCode = redisUtil.getKey(emailKey);
                    if (getCode.equals(code)) {
                        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
                        userMapper.forgetPwd(user);
                        object.put("user", user);
                        redisUtil.deleteKey(emailKey);
                        redisUtil.deleteKey(tokenKey);
                        return restApiResult.success(object);
                    }
                    return restApiResult.build(RequestResultEnum.emailcode.getCode(), RequestResultEnum.emailcode.getMsg());
                }
                return restApiResult.build(RequestResultEnum.forgot.getCode(), RequestResultEnum.forgot.getMsg());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return restApiResult.faild();
    }
    @Override
    public RestApiResult deleteUser(String email) {
        if (email != null) {
            User existUser = getUser(email);
            if (existUser != null) {
                String tokenKey = "usertoken:" + email;
                userMapper.deleteUser(email);
                redisUtil.deleteKey(tokenKey);
                return restApiResult.success();
            } else {
                return restApiResult.build(RequestResultEnum.delete.getCode(), RequestResultEnum.delete.getMsg());
            }

        }
        return restApiResult.faild();
    }
    @Override
    public RestApiResult getToken(String email) {
        String tokenKey = "usertoken:" + email;
        JSONObject object = new JSONObject();
        if (email != null) {
            if (redisUtil.hasKey(tokenKey)) {
                object.put("usertoken", redisUtil.getKey(tokenKey));
                return restApiResult.success(object);
            } else {
                User user = getUser(email);
                if (user != null) {
                    String usertoken = tokenUtil.getToken(user);
                    log.info("获取token{}", usertoken);
                    redisUtil.setKey(tokenKey, usertoken, 60);
                    object.put("usertoken", usertoken);
                    return restApiResult.success(object);
                }
            }
        }
        return restApiResult.faild();
    }
    @Override
    public RestApiResult logout(String email) {
        String tokenKey = "usertoken:" + email;
        if (email != null) {
            redisUtil.deleteKey(tokenKey);
            return restApiResult.success();
        }
        return restApiResult.faild();
    }
}
