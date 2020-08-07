package com.autotestplatform.service.impl;

import com.autotestplatform.entity.User;
import com.autotestplatform.mapper.UserMapper;
import com.autotestplatform.service.UserService;
import com.autotestplatform.utils.RedisKey;
import com.autotestplatform.utils.RequestResultEnum;
import com.autotestplatform.utils.RestApiResult;
import com.autotestplatform.utils.SendEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SendEmail sendEmail;
    @Autowired
    private RestApiResult restApiResult;
    @Autowired
    private RedisKey redisKey;

    @Override
    public RestApiResult login(String email, String pwd) {
        RestApiResult restApiResult = new RestApiResult();
        if (email != null && pwd != null) {
            pwd = DigestUtils.md5DigestAsHex(pwd.getBytes());
            User user = userMapper.login(email, pwd);
            if (user == null) {
                log.info("email{},pwd{}", email, pwd);
                restApiResult.setCode(RequestResultEnum.login.getCode());
                restApiResult.setMsg(RequestResultEnum.login.getMsg());
                restApiResult.setData(null);
            } else {
                log.info("user{}", user);
                restApiResult.setCode(RequestResultEnum.SUCCESS.getCode());
                restApiResult.setMsg(RequestResultEnum.SUCCESS.getMsg());
                restApiResult.setData(user);
            }
            return restApiResult;
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
        RestApiResult restApiResult = new RestApiResult();
        if (user.getEmail() == null || user.getPassword() == null || user.getDepartment() == null || user.getPhone() == null || user.getRole() == null || user.getUsername() == null) {
            return restApiResult.build(RequestResultEnum.user.getCode(), RequestResultEnum.user.getMsg());
        }
        if (user != null) {
            User existUser = getUser(user.getEmail());
            if (existUser != null) {
                log.info("该用户存在{}", existUser);
                restApiResult.setCode(RequestResultEnum.SUCCESS.getCode());
                restApiResult.setMsg(RequestResultEnum.register.getMsg());
            } else {
                log.info("新用户{}", user);
                user.setId(null);
                user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
                userMapper.register(user);
                restApiResult.setCode(RequestResultEnum.SUCCESS.getCode());
                restApiResult.setMsg(RequestResultEnum.SUCCESS.getMsg());
                restApiResult.setData(user);
            }
            return restApiResult;
        }
        return restApiResult.faild();
    }

    @Override
    public RestApiResult forgetPwd(User user, String code) {
        try {
            String email = user.getEmail();
            if (user != null) {
                User existUser = getUser(email);
                if (existUser != null) {
                    String getCode = redisKey.getKey(email);
                    if (getCode.equals(code)) {
                        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
                        userMapper.forgetPwd(user);
                        return restApiResult.success(user);
                    }
                    return restApiResult.build(RequestResultEnum.emailcode.getCode(), RequestResultEnum.emailcode.getMsg());
                }
                return restApiResult.build(RequestResultEnum.forgot.getCode(), RequestResultEnum.forgot.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            //return restApiResult.faild();
        }
        return restApiResult.faild();
    }

    @Override
    public boolean deleteUser(String uid) {
        return false;
    }
}
