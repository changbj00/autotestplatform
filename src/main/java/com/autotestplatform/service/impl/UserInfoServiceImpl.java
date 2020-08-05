package com.autotestplatform.service.impl;

import com.autotestplatform.entity.UserInfo;
import com.autotestplatform.service.UserInfoService;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Override
    public UserInfo getUserInfo(String uid) {
        return null;
    }

    @Override
    public UserInfo insertUserInfo(UserInfo userInfo) {
        return null;
    }

    @Override
    public UserInfo updateUserInfo(UserInfo userInfo) {
        return null;
    }

    @Override
    public boolean deleteUserInfo(String uid) {
        return false;
    }
}
