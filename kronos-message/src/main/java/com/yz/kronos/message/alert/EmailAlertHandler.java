package com.yz.kronos.message.alert;


import com.yz.kronos.message.config.EmailConfig;
import com.yz.kronos.message.config.MessageConfig;
import com.yz.kronos.message.service.EmailSendClient;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮件提醒
 * @author shanchong
 * @date 2020-01-23
 **/
@Slf4j
public class EmailAlertHandler implements AlertHandler {

    MessageConfig messageConfig;

    public EmailAlertHandler(final MessageConfig messageConfig) {
        this.messageConfig = messageConfig;
    }

    @Override
    public void handle(final Exception e) {
        EmailConfig emailConfig = this.messageConfig.getEmail();
        if (emailConfig==null){
            log.error("email config is null");
            return;
        }
        EmailSendClient.send(emailConfig,"[Kronos系统异常告警]",e.getMessage());
    }

}
