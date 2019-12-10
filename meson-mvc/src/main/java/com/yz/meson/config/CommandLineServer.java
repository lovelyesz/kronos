package com.yz.meson.config;

import io.termd.core.telnet.netty.NettyTelnetTtyBootstrap;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.TimeUnit;

/**
 * 命令行工具
 * @author shanchong
 * @date 2019-11-14
 **/
public class CommandLineServer {

    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public CommandLineServer(ThreadPoolTaskExecutor threadPoolTaskExecutor){
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    public void init() {
        threadPoolTaskExecutor.execute(()->{
            try {
                NettyTelnetTtyBootstrap bootstrap = new NettyTelnetTtyBootstrap().setHost("127.0.0.1").setPort(1335);
                bootstrap.start(new Shell()).get(10, TimeUnit.SECONDS);
            } catch (Throwable e){
                e.printStackTrace();
            }
        });

    }


}
