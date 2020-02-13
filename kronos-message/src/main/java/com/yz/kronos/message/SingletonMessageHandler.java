package com.yz.kronos.message;

import com.yz.kronos.config.MessageConfig;
import com.yz.kronos.config.MessageType;

/**
 * 只用一种方式去处理通知
 * @author shanchong
 * @date 2020-02-13
 **/
public class SingletonMessageHandler implements MessageHandler {

    private MessageConfig messageConfig;

    private MessageType[] messageTypes;

    public SingletonMessageHandler(final MessageConfig messageConfig, final MessageType... messageTypes) {
        this.messageConfig = messageConfig;
        this.messageTypes = messageTypes;
    }

    /**
     * 处理
     *  按顺序执行，有一个成功了就停止
     * @param title
     * @param message
     */
    @Override
    public void handle(final String title, final String message) {
        for (int i = 0; i < messageTypes.length; i++) {
            Boolean execute = messageTypes[i].execute(messageConfig, title, message);
            if (execute){
                break;
            }
        }
    }
}
