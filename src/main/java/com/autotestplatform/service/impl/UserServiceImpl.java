package com.autotestplatform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.autotestplatform.entity.User;
import com.autotestplatform.mapper.UserMapper;
import com.autotestplatform.service.UserService;
import com.autotestplatform.utils.RedisKey;
import com.autotestplatform.utils.RequestResultEnum;
import com.autotestplatform.utils.RestApiResult;
import com.autotestplatform.utils.TokenUtil;
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
    private UserMapper userMapper;
    @Autowired
    private RestApiResult restApiResult;
    @Autowired
    private RedisKey redisKey;
    @Autowired
    private TokenUtil tokenUtil;

    @Override
    public RestApiResult login(String email, String password) {
        JSONObject object = new JSONObject();
        if (email != null && password != null) {
            password = DigestUtils.md5DigestAsHex(password.getBytes());
            User user = userMapper.login(email, password);
            if (user == null) {
                log.warn("用户不存在{}", email);
                return restApiResult.build(RequestResultEnum.login.getCode(), RequestResultEnum.login.getMsg());
//                restApiResult.setCode(RequestResultEnum.login.getCode());
//                restApiResult.setMsg(RequestResultEnum.login.getMsg());
//                restApiResult.setData(null);
            } else {
                log.info("user{}", user);
                String tokenKey = "token:" + user.getEmail();
                String token = tokenUtil.getToken(user);
                redisKey.setKey(tokenKey, token, 60);
                object.put("token", token);
                object.put("user", user);
                return restApiResult.success(object);
//                restApiResult.setCode(RequestResultEnum.SUCCESS.getCode());
//                restApiResult.setMsg(RequestResultEnum.SUCCESS.getMsg());
//                restApiResult.setData(user);
            }
            //return restApiResult;
        }
        return restApiResult.faild();
    }

    @Override
    public User getUser(String email) {
        User user = userMapper.getUser(email);
        return user;
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
                String tokenKey = "token:" + user.getEmail();
                String token = tokenUtil.getToken(user);
                redisKey.setKey(tokenKey, token, 60);
                object.put("token", token);
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
                    String tokenKey = "token:" + email;
                    String getCode = redisKey.getKey(emailKey);
                    if (getCode.equals(code)) {
                        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
                        userMapper.forgetPwd(user);
                        object.put("user", user);
                        redisKey.deleteKey(emailKey);
                        redisKey.deleteKey(tokenKey);
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
                String tokenKey = "token:" + email;
                userMapper.deleteUser(email);
                redisKey.deleteKey(tokenKey);
                return restApiResult.success();
            } else {
                return restApiResult.build(RequestResultEnum.delete.getCode(), RequestResultEnum.delete.getMsg());
            }

        }
        return restApiResult.faild();
    }

    @Override
    public RestApiResult getToken(String email) {
        String tokenKey = "token:" + email;
        JSONObject object = new JSONObject();
        if (email != null) {
            if (redisKey.hasKey(tokenKey)) {
                object.put("token", redisKey.getKey(tokenKey));
                return restApiResult.success(object);
            } else {
                User user = getUser(email);
                if (user != null) {
                    object.put("token", tokenUtil.getToken(user));
                    return restApiResult.success(object);
                }
            }
        }
        return restApiResult.faild();
    }

    @Override
    public RestApiResult logout(String email) {
        String tokenKey = "token:" + email;
        if (email != null) {
            redisKey.deleteKey(tokenKey);
            return restApiResult.success();
        }
        return restApiResult.faild();
    }
}
