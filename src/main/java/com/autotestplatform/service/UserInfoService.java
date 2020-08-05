package com.autotestplatform.service;

import com.autotestplatform.entity.UserInfo;

public interface UserInfoService {
    UserInfo getUserInfo(String uid);
    UserInfo insertUserInfo(UserInfo userInfo);
    UserInfo updateUserInfo(UserInfo userInfo);
    boolean deleteUserInfo(String uid);

}
