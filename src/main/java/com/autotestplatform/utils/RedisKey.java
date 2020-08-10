package com.autotestplatform.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
@Component
public class RedisKey {
    @Autowired
    RedisTemplate redisTemplate;
    public String getKey(String key){
        ValueOperations operations = redisTemplate.opsForValue();
        boolean haskey = redisTemplate.hasKey(key);
        if (haskey){
            Object value= operations.get(key);
            return value.toString();
        }else {
            return "请重新获取";
        }
    }
    public void setKey(String key,String value){
        ValueOperations operations=redisTemplate.opsForValue();
        operations.set(key, value, 1, TimeUnit.MINUTES);
    }
    public void deleteKey(String key){
//        ValueOperations operations=redisTemplate.opsForValue();
        redisTemplate.delete(key);
    }

}
