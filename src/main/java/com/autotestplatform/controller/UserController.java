package com.autotestplatform.controller;

import com.autotestplatform.api.UserLoginToken;
import com.autotestplatform.entity.User;
import com.autotestplatform.service.UserService;
import com.autotestplatform.utils.RestApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public RestApiResult login(@Valid String email, @Valid String password) {

        log.info("输入参数email:{},pwd:{}", email, password);
        return userService.login(email, password);
    }

    @UserLoginToken
    @PostMapping(value = "/logOut")
    public RestApiResult logOut(@Valid String email) {
        return null;
    }

    @PostMapping(value = "/register")
    public RestApiResult registerUser(@Validated User user) {
        log.info("输入参数：{}", user);
        return userService.register(user);
    }

    @UserLoginToken
    @PostMapping(value = "/forgetUser")
    public RestApiResult updateUser(User user, @Valid String code) {
        log.info("输入参数：{}", user);
        return userService.forgetPwd(user, code);
    }

    @UserLoginToken
    @PostMapping(value = "/deleteUser")
    public RestApiResult deleteUser(@Valid String email) {
        log.info("输入参数{}", email);
        return userService.deleteUser(email);
    }
}
