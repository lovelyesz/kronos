package com.yz.kronos.config;

import lombok.Data;

/**
 * @author shanchong
 * @date 2020-02-13
 **/
@Data
public class EmailConfig {

    private String receiver;
    private String host;
    private String form;
    private String userName;
    private String password;
}
