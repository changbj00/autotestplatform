package com.autotestplatform.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.autotestplatform.entity.User;
import com.autotestplatform.mapper.UserMapper;
import com.autotestplatform.service.UserService;
import com.autotestplatform.utils.*;
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
    private RestApiResult restApiResult;
    @Autowired
    private RedisKey redisKey;
    @Autowired
    private TokenUtil tokenUtil;

    @Override
    public RestApiResult login(String email, String password) {
        //RestApiResult restApiResult = new RestApiResult();
        if (email != null && password != null) {
            password = DigestUtils.md5DigestAsHex(password.getBytes());
            User user = userMapper.login(email, password);
            if (user == null) {
                log.info("email{},pwd{}", email, password);
                return restApiResult.build(RequestResultEnum.login.getCode(),RequestResultEnum.login.getMsg());
//                restApiResult.setCode(RequestResultEnum.login.getCode());
//                restApiResult.setMsg(RequestResultEnum.login.getMsg());
//                restApiResult.setData(null);
            } else {
                log.info("user{}", user);
                String token=tokenUtil.getToken(user);
                JSONObject object=new JSONObject();
                object.put("token",token);
                object.put("user",user);
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
       // RestApiResult restApiResult = new RestApiResult();
        if (user.getEmail() == null || user.getPassword() == null || user.getDepartment() == null || user.getPhone() == null || user.getRole() == null || user.getUsername() == null) {
            return restApiResult.build(RequestResultEnum.user.getCode(), RequestResultEnum.user.getMsg());
        }
        if (user != null) {
            User existUser = getUser(user.getEmail());
            if (existUser != null) {
                log.info("该用户存在{}", existUser);
                return restApiResult.build(RequestResultEnum.register.getCode(),RequestResultEnum.register.getMsg());
            } else {
                log.info("新用户{}", user);
                user.setId(null);
                user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
                userMapper.register(user);
                return restApiResult.success(user);
            }
        }
        return restApiResult.faild();
    }

    @Override
    public RestApiResult forgetPwd(User user, String code) {
        if (user.getEmail() == null || user.getPassword() == null ) {
            return restApiResult.build(RequestResultEnum.user.getCode(), RequestResultEnum.user.getMsg());
        }
        try {
            String email = user.getEmail();
            if (user != null) {
                User existUser = getUser(email);
                if (existUser != null) {
                    String emailKey="message:"+email;
                    String getCode = redisKey.getKey(emailKey);
                    if (getCode.equals(code)) {
                        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
                        userMapper.forgetPwd(user);
                        redisKey.deleteKey(emailKey);
                        return restApiResult.success(user);
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
        if (email!=null){
            User existUser=getUser(email);
            if (existUser!=null){
                userMapper.deleteUser(email);
                return restApiResult.success();
            }else {
                return restApiResult.build(RequestResultEnum.delete.getCode(),RequestResultEnum.delete.getMsg());
            }

        }
        return restApiResult.faild();
    }
}
