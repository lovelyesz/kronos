package com.yz.kronos.spring;

import com.yz.kronos.KubernetesConfig;
import com.yz.kronos.message.MessageHandler;
import com.yz.kronos.schedule.flow.AbstractFlowManage;
import com.yz.kronos.schedule.flow.SimpleFlowManage;
import com.yz.kronos.schedule.flow.FlowInterceptor;
import com.yz.kronos.schedule.job.*;
import com.yz.kronos.schedule.listener.DefaultEventHandlerManage;
import com.yz.kronos.schedule.listener.EventHandlerManage;
import com.yz.kronos.schedule.listener.JobProcessListener;
import com.yz.kronos.schedule.listener.JobSynchronousEventHandler;
import com.yz.kronos.schedule.queue.JobQueue;
import com.yz.kronos.schedule.repository.DefaultJobExecuteRepository;
import com.yz.kronos.schedule.repository.JobExecuteRepository;
import com.yz.kronos.schedule.synchronizer.JobProcessSynchronizer;
import com.yz.kronos.spring.redis.RedisJobProcessSynchronizer;
import com.yz.kronos.spring.redis.RedisJobQueue;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shanchong
 * @date 2019-12-23
 **/
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "kronos",name = "enable",havingValue = "true",matchIfMissing = true)
public class KronosAutoConfiguration {

    /**
     * kronos配置
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "kronos")
    public KubernetesConfig kubernetesConfig(){
        return new KubernetesConfig();
    }

    /**
     * 任务信息队列
     * @param redissonClient
     * @return
     */
    @Bean
    @ConditionalOnBean(RedissonClient.class)
    public JobQueue jobQueue(RedissonClient redissonClient){
        return new RedisJobQueue(redissonClient);
    }

    /**
     * 维护任务分片的同步器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public JobSynchronousEventHandler jobSynchronousEventHandler(JobProcessSynchronizer jobProcessSynchronizer,
                                                                 MessageHandler messageHandler){
        final JobSynchronousEventHandler jobSynchronousEventHandler = new JobSynchronousEventHandler();
        jobSynchronousEventHandler.setJobProcessSynchronizer(jobProcessSynchronizer);
        jobSynchronousEventHandler.setAlertHandler(messageHandler);
        return jobSynchronousEventHandler;
    }

    /**
     * 任务监听处理管理
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public EventHandlerManage eventHandlerManage(JobSynchronousEventHandler jobSynchronousEventHandler){
        final DefaultEventHandlerManage defaultEventHandlerManage = new DefaultEventHandlerManage();
        defaultEventHandlerManage.add(jobSynchronousEventHandler);
        return defaultEventHandlerManage;
    }

    /**
     * 任务进程监听器
     * @param kubernetesConfig
     * @param eventHandlerManage
     * @return
     */
    @Bean(initMethod = "run")
    public JobProcessListener processListener(KubernetesConfig kubernetesConfig,
                                              EventHandlerManage eventHandlerManage){
        final JobProcessListener jobProcessListener = new JobProcessListener();
        jobProcessListener.setEventHandlerManage(eventHandlerManage);
        jobProcessListener.setKubernetesConfig(kubernetesConfig);
        return jobProcessListener;
    }

    @Bean
    @ConditionalOnMissingBean
    public JobLaunchManage jobExecuteManage(JobQueue jobQueue, JobExecuteRepository jobExecuteRepository){
        final SimpleJobLaunchManage simpleJobManage = new SimpleJobLaunchManage();
        simpleJobManage.setJobExecuteRepository(jobExecuteRepository);
        simpleJobManage.setJobQueue(jobQueue);
        return simpleJobManage;
    }

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    public JobProcessSynchronizer jobProcessSynchronizer(RedissonClient redissonClient){
        return new RedisJobProcessSynchronizer(redissonClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public JobExecuteRepository jobExecuteRepository(){
        return new DefaultJobExecuteRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    public FlowInterceptor flowInterceptor(){
        return new FlowInterceptor.DefaultFlowInterceptor();
    }

    /**
     * 工作流调度器
     * @param jobLaunchManage
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(JobSchedule.class)
    public AbstractFlowManage flowManage(KubernetesConfig kubernetesConfig,
                                         JobLaunchManage jobLaunchManage,
                                         JobProcessSynchronizer jobProcessSynchronizer,
                                         FlowInterceptor flowInterceptor){
        final SimpleFlowManage simpleFlowManage = new SimpleFlowManage();
        simpleFlowManage.setConfig(kubernetesConfig);
        simpleFlowManage.setFlowInterceptor(flowInterceptor);
        simpleFlowManage.setJobProcessSynchronizer(jobProcessSynchronizer);
        simpleFlowManage.setJobLaunchManage(jobLaunchManage);
        return simpleFlowManage;
    }

}
