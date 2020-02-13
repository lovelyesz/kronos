package com.yz.kronos.message.config;


import lombok.Data;

/**
 * @author shanchong
 * @date 2020-01-23
 **/
@Data
public class MessageConfig {

    private DingConfig ding;

    private EmailConfig email;

}
