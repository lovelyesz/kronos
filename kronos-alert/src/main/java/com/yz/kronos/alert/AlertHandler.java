package com.yz.kronos.alert;

/**
 * 告警处理器
 * @author shanchong
 */
public interface AlertHandler {

    void handle(Exception e);

}
