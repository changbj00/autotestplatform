package com.autotestplatform.mapper;

import com.autotestplatform.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserInfoMapper {
    UserInfo getUserInfo(@Param("uid") int uid);
}
