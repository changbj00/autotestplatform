package com.autotestplatform.service;

import com.autotestplatform.entity.User;
import com.autotestplatform.entity.UserInfo;
import com.autotestplatform.utils.RestApiResult;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

public interface UserService {
    RestApiResult login(String email,String pwd);
    User getUser(String email);
    RestApiResult register(User user);
    RestApiResult forgetPwd(User user,String code);
    RestApiResult deleteUser(String email);
}
