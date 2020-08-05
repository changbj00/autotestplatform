package com.autotestplatform.controller;

import com.autotestplatform.entity.User;
import com.autotestplatform.service.UserService;
import com.autotestplatform.utils.RestApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public RestApiResult login(String email, String pwd) {
        log.info("输入参数email:{},pwd:{}", email, pwd);
        return userService.login(email, pwd);
    }

    public RestApiResult logOut(String uid) {
        return null;
    }

    @PostMapping(value = "/register")
    public RestApiResult registerUser(@Validated @RequestBody User user) {
        log.info("输入参数user{}",user);
        return userService.register(user);
    }

    public RestApiResult updateUser(User user) {
        return null;
    }

    public RestApiResult deleteUser(User user) {
        return null;
    }
}
