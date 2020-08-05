package com.autotestplatform.service.impl;

import com.autotestplatform.entity.User;
import com.autotestplatform.entity.UserInfo;
import com.autotestplatform.mapper.UserInfoMapper;
import com.autotestplatform.mapper.UserMapper;
import com.autotestplatform.service.UserService;
import com.autotestplatform.utils.RequestResultEnum;
import com.autotestplatform.utils.RestApiResult;
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
    private UserInfoMapper userInfoMapper;

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
                //restApiResult.build(1001,"用户不存在");
                restApiResult.setData(null);
            } else {
                log.info("user{}", user);
                restApiResult.setCode(RequestResultEnum.SUCCESS.getCode());
                restApiResult.setMsg(RequestResultEnum.SUCCESS.getMsg());
                restApiResult.setData(user);
                //restApiResult.build(1000, "登陆成功", user);
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
        if (user != null) {
            User existUser = getUser(user.getEmail());
            if (existUser != null) {
                log.info("该用户存在{}",existUser);
                restApiResult.setCode(RequestResultEnum.SUCCESS.getCode());
                restApiResult.setMsg(RequestResultEnum.register.getMsg());
            } else {
                log.info("新用户{}",user);
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
    public boolean forgetPwd(User user, String code) {

        return false;
    }

    @Override
    public boolean deleteUser(String uid) {
        return false;
    }
}
