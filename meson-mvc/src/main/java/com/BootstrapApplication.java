package com;

import com.yz.meson.config.CommandLineServer;
import com.yz.meson.quartz.SchedulerFactory;
import com.yz.meson.schedule.JobProcessListener;
import com.yz.meson.schedule.ScheduleSynchronizer;
import com.yz.meson.schedule.config.KubernetesConfig;
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

    @Bean(initMethod = "run")
    public JobProcessListener jobProcessListener(KubernetesConfig kubernetesConfig,
                                                 ScheduleSynchronizer scheduleSynchronizer){
        return new JobProcessListener(kubernetesConfig,scheduleSynchronizer);
    }


}