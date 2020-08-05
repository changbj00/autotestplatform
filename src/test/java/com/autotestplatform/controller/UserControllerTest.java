package com.autotestplatform.controller;

import com.autotestplatform.entity.User;
import com.autotestplatform.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class UserControllerTest {
    @Autowired
    UserService userService;
    @Autowired
    UserController userController;
    @Test
    public void test(){
        User user=new User();
        user.setEmail("baojie.chang@100credit.com");
        user.setPassword("111");
        userController.registerUser(user);
    }

}