package com.yz.kronos.config;

import com.yz.kronos.message.MessageHandler;
import com.yz.kronos.message.SingletonMessageHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shanchong
 * @date 2020-02-13
 **/
@Configuration
public class AlertHandleConfig {

    @Bean
    @ConfigurationProperties(prefix = "alert")
    public MessageConfig messageConfig(){
        return new MessageConfig();
    }

    @Bean
    @ConditionalOnBean(value = MessageConfig.class)
    public MessageHandler alertHandler(MessageConfig messageConfig){
        return new SingletonMessageHandler(messageConfig,MessageType.DING,MessageType.EMAIL);
    }

}
