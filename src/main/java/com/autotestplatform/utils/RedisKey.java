package com.autotestplatform.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;
@Slf4j
@Component
@Transactional(rollbackFor = RuntimeException.class)
public class RedisKey {
    @Autowired
    RedisTemplate redisTemplate;

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public String getKey(String key) {
        ValueOperations operations = redisTemplate.opsForValue();
        boolean haskey = hasKey(key);
        if (haskey) {
            Object value = operations.get(key);
            return value.toString();
        } else {
            return "请重新获取";
        }
    }
    @Async("asyncThreadPoolTaskExecutor")
    public boolean setKey(String key, Object value) {
        if (hasKey(key)){
            return false;
        }
        sleep();
        log.info("异步方法内部线程名称：{}", Thread.currentThread().getName());
        ValueOperations operations = redisTemplate.opsForValue();
        operations.set(key, value, 1, TimeUnit.MINUTES);
        return true;
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }
    private void sleep() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
