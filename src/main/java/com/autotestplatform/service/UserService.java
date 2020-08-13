package com.autotestplatform.service;

import com.autotestplatform.entity.User;
import com.autotestplatform.utils.RestApiResult;

public interface UserService {
    RestApiResult login(String email,String pwd);
    RestApiResult logout(String email);
    User getUser(String email);
    RestApiResult register(User user);
    RestApiResult forgetUser(User user,String code);
    RestApiResult deleteUser(String email);
    RestApiResult getToken(String email);
}
