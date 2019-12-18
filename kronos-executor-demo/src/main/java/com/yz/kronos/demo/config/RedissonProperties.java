package com.yz.kronos.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "redisson.sentinel")
public class RedissonProperties{

    private String masterName;

    private String[] sentinelAddresses;

}