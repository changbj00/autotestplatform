package com.autotestplatform.mapper;

import com.autotestplatform.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    User login(@Param("email") String email, @Param("password") String password);

    User getUser(@Param("email") String email);

    void register(User user);

    void forgetPwd(User user);

    void deleteUser(@Param("email") String email);
}
