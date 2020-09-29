package com.autotestplatform.controller;

import com.autotestplatform.api.UserLoginToken;
import com.autotestplatform.entity.User;
import com.autotestplatform.service.UserService;
import com.autotestplatform.utils.RestApiResult;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

/**
 * 用户处理
 * 登录、退出、注册、忘记密码、删除用户、token
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @CrossOrigin
    @PostMapping(value = "/login")
    public RestApiResult login(@Valid@Email String username, @Valid String password) {

        log.info("输入参数email:{},pwd:{}", username, password);
        return userService.login(username, password);
    }

    @UserLoginToken
    @PostMapping(value = "/logOut")
    public RestApiResult logOut(@Valid@Email String username) {
        log.info("输入参数：{}", username);
        return userService.logout(username);
    }

    @PostMapping(value = "/register")
    public RestApiResult registerUser( User user) {
        log.info("输入参数：{}", user);
        return userService.register(user);
    }

    @PostMapping(value = "/forgetUser")
    public RestApiResult forgetUser( User user, @Valid String code) {
        log.info("输入参数：{}", user);
        return userService.forgetUser(user, code);
    }

    @UserLoginToken
    @PostMapping(value = "/deleteUser")
    public RestApiResult deleteUser(@Valid@Email String username) {
        log.info("输入参数{}", username);
        return userService.deleteUser(username);
    }

    @GetMapping(value = "/getToken")
    public RestApiResult getToken(@Valid@Email String username) {
        log.info("输入参数：{}", username);
        return userService.getToken(username);
    }

    @UserLoginToken
    @PostMapping(value = "/getuser")
    public RestApiResult getUser(@Valid@Email String username) {
        RestApiResult restApiResult = new RestApiResult();
        return restApiResult.success(userService.getUser(username));
    }
}
