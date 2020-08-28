package com.autotestplatform.controller;

import com.autotestplatform.api.UserLoginToken;
import com.autotestplatform.entity.User;
import com.autotestplatform.service.UserService;
import com.autotestplatform.utils.RestApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户处理
 * 登录、退出、注册、忘记密码、删除用户、token
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @CrossOrigin
    @PostMapping(value = "/login")
    public RestApiResult login(@Valid String email, @Valid String password) {

        log.info("输入参数email:{},pwd:{}", email, password);
        return userService.login(email, password);
    }

    @UserLoginToken
    @PostMapping(value = "/logOut")
    public RestApiResult logOut(@Valid String email) {
        log.info("输入参数：{}", email);
        return userService.logout(email);
    }

    @PostMapping(value = "/register")
    public RestApiResult registerUser(@Validated User user) {
        log.info("输入参数：{}", user);
        return userService.register(user);
    }

    @UserLoginToken
    @PostMapping(value = "/forgetUser")
    public RestApiResult forgetUser(User user, @Valid String code) {
        log.info("输入参数：{}", user);
        return userService.forgetUser(user, code);
    }

    @UserLoginToken
    @PostMapping(value = "/deleteUser")
    public RestApiResult deleteUser(@Valid String email) {
        log.info("输入参数{}", email);
        return userService.deleteUser(email);
    }

    @GetMapping(value = "/getToken")
    public RestApiResult getToken(@Valid String email) {
        log.info("输入参数：{}", email);
        return userService.getToken(email);
    }

    @UserLoginToken
    @PostMapping(value = "/getuser")
    public RestApiResult getUser(@Valid String email) {
        RestApiResult restApiResult = new RestApiResult();
        return restApiResult.success(userService.getUser(email));
    }
}
