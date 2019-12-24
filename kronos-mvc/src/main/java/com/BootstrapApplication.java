package com;

import com.yz.kronos.quartz.SchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author shanchong
 * @date 2019-11-11
 **/
@SpringBootApplication
@EnableAsync
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



}
