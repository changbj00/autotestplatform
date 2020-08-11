package com.autotestplatform.utils;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class RedisKeyTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisKey redisKey;

    @Test
    void getKey() {
        System.out.println(redisKey.setKey("message:1127701980@qq.com",111));
    }
}