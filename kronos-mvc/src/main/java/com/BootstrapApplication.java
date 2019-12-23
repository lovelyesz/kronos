package com;

import com.yz.kronos.config.CommandLineServer;
import com.yz.kronos.quartz.SchedulerFactory;
import com.yz.kronos.schedule.listener.JobProcessListener;
import com.yz.kronos.model.KubernetesConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author shanchong
 * @date 2019-11-11
 **/
@SpringBootApplication
public class BootstrapApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootstrapApplication.class,args);
    }

    /**
     * 定时调度器
     * @return
     */
    @Bean(initMethod = "init")
    public SchedulerFactory schedulerFactory(){
        return new SchedulerFactory();
    }

    /**
     * 命令行服务
     * @param threadPoolTaskExecutor
     * @return
     */
    @Bean(initMethod = "init")
    public CommandLineServer commandLineServer(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        return new CommandLineServer(threadPoolTaskExecutor);
    }




}
