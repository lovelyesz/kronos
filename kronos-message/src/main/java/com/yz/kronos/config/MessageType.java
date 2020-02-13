package com.yz.kronos.config;

import com.yz.kronos.client.DingtalkSendClient;
import com.yz.kronos.client.EmailSendClient;

/**
 * 通知类型
 * @author shanchong
 */
public enum MessageType {

    DING(){
        @Override
        public Boolean execute(MessageConfig messageConfig,String title,String message) {
            return DingtalkSendClient.send(messageConfig.getDing(),title,message);
        }
    },
    EMAIL() {
        @Override
        public Boolean execute(MessageConfig messageConfig,String title,String message) {
            return EmailSendClient.send(messageConfig.getEmail(),title,message);
        }
    };

    public abstract Boolean execute(MessageConfig messageConfig,String title,String message);


}
