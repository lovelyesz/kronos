package com.yz.kronos.message.alert;


import com.yz.kronos.message.config.MessageConfig;
import com.yz.kronos.message.config.DingConfig;
import com.yz.kronos.message.service.DingtalkSendClient;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shanchong
 * @date 2020-01-23
 **/
@Slf4j
public class DingAlertHandler implements AlertHandler {

    MessageConfig messageConfig;

    public DingAlertHandler(final MessageConfig messageConfig) {
        this.messageConfig = messageConfig;
    }

    @Override
    public void handle(final Exception e) {
        DingConfig dingConfig = messageConfig.getDing();
        if (dingConfig==null){
            log.error("ding config is null");
            return;
        }
        DingtalkSendClient.send(dingConfig,"[Kronos系统异常告警]",e.getMessage());

    }
}
