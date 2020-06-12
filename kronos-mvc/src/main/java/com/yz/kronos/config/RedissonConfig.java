package com.yz.kronos.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author shanchong
 * @date 2020-06-12
 **/
@Configuration
public class RedissonConfig {

    @Autowired
    private RedisTemplate redisTemplate;

//    @Bean
    public RedissonClient redissonClient(){
        RedissonClient redissonClient = Redisson.create();
        return redissonClient;
    }
}
