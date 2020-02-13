package com.yz.kronos.message.service;

import com.yz.kronos.message.config.EmailConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * @author shanchong
 * @date 2020-02-13
 **/
@Slf4j
public class EmailSendClient {

    public static Boolean send(EmailConfig config, String title, String message) {
        HtmlEmail email = new HtmlEmail();
        email.setHostName(config.getHost());
        try {
            email.setFrom(config.getForm());
            email.setAuthentication(config.getUserName(),config.getPassword());
            email.setCharset("utf-8");
            email.setSubject(title);
            email.setMsg(message);
            String[] receivers = config.getReceiver().split(",");
            email.addTo(receivers);
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
            log.error("email send fail",e);
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        EmailConfig config = new EmailConfig();
        config.setReceiver("shanchong@yunzongnet.com");
        config.setHost("smtp.yunzongnet.com");
        config.setForm("zhifu@yunzongnet.com");
        config.setUserName("zhifu@yunzongnet.com");
        config.setPassword("4HwxfI7b");
        Boolean send = EmailSendClient.send(config, "测试标题", "测试内容");
        System.out.println(send);
    }
}
