package com.autotestplatform.service;

import com.autotestplatform.entity.User;
import com.autotestplatform.entity.UserInfo;
import com.autotestplatform.utils.RestApiResult;


public interface UserService {
    RestApiResult login(String email, String pwd);
    User getUser(String email);
    RestApiResult register(User user);
    boolean forgetPwd(User user,String code);
    boolean deleteUser(String email);
}
