package com.yz.kronos.message;

/**
 * 通知处理器
 */
public interface MessageHandler {

    /**
     * 处理
     * @param title
     * @param message
     */
    void handle(String title,String message);

}
