package com.yz.kronos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author shanchong
 */
@ConfigurationProperties(prefix = "redisson.sentinel",ignoreUnknownFields = false)
public class RedissonProperties{

    private String masterName;

    private String[] sentinelAddresses;

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(final String masterName) {
        this.masterName = masterName;
    }

    public String[] getSentinelAddresses() {
        return sentinelAddresses;
    }

    public void setSentinelAddresses(final String[] sentinelAddresses) {
        this.sentinelAddresses = sentinelAddresses;
    }
}
