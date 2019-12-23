package com.yz.kronos.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shanchong
 * @date 2019-11-07
 **/
@Configuration
@EnableConfigurationProperties(RedissonProperties.class)
public class RedisConfig {

//    @Bean
//    public RedissonClient redissonClient(RedissonProperties redissonProperties){
//        final KubeConfig config = new KubeConfig();
//        final SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
//        sentinelServersConfig.setMasterName(redissonProperties.getMasterName());
//        sentinelServersConfig.setDatabase(1);
//        final String[] sentinelAddresses = redissonProperties.getSentinelAddresses();
//        for (int i = 0; i < sentinelAddresses.length; i++) {
//            sentinelServersConfig.addSentinelAddress(sentinelAddresses[i]);
//        }
//        config.setCodec(StringCodec.INSTANCE);
//        return Redisson.create(config);
//    }

    @Bean
    public RedissonClient redissonClient(){
        final Config config = new Config();
        final SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://10.200.46.238:6379");
        singleServerConfig.setDatabase(0);
        config.setCodec(StringCodec.INSTANCE);
        return Redisson.create(config);
    }




}
