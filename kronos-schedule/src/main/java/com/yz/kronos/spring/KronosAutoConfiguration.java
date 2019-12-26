package com.yz.kronos.spring;

import com.yz.kronos.KubernetesConfig;
import com.yz.kronos.schedule.flow.AbstractFlowManage;
import com.yz.kronos.schedule.flow.DefaultFlowManage;
import com.yz.kronos.schedule.job.DefaultJobSchedule;
import com.yz.kronos.schedule.job.DefaultJobShutdown;
import com.yz.kronos.schedule.job.JobSchedule;
import com.yz.kronos.schedule.job.JobShutdown;
import com.yz.kronos.schedule.listener.DefaultEventHandlerManage;
import com.yz.kronos.schedule.listener.EventHandlerManage;
import com.yz.kronos.schedule.listener.JobProcessListener;
import com.yz.kronos.schedule.listener.JobSynchronzeEventHandler;
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
     * 任务监听处理管理
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public EventHandlerManage eventHandlerManage(JobProcessSynchronizer jobProcessSynchronizer){
        final DefaultEventHandlerManage defaultEventHandlerManage = new DefaultEventHandlerManage();
        //维护任务分片的同步器
        final JobSynchronzeEventHandler jobSynchronzeEventHandler = new JobSynchronzeEventHandler(jobProcessSynchronizer);
        defaultEventHandlerManage.add(jobSynchronzeEventHandler);
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
        return new JobProcessListener(kubernetesConfig,eventHandlerManage);
    }

    /**
     * 任务调度器
     * @param kubernetesConfig
     * @param jobQueue
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public JobSchedule jobSchedule(KubernetesConfig kubernetesConfig,
                                   JobQueue jobQueue,
                                   JobExecuteRepository jobExecuteRepository){
        return new DefaultJobSchedule(kubernetesConfig,jobQueue,jobExecuteRepository);
    }

    /**
     * 任务关停器
     * @param jobQueue
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public JobShutdown jobShutdown(JobQueue jobQueue){
        return new DefaultJobShutdown(jobQueue);
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
    /**
     * 工作流调度器
     * @param jobSchedule
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(JobSchedule.class)
    public AbstractFlowManage flowManage(KubernetesConfig kubernetesConfig,
                                           JobSchedule jobSchedule, JobShutdown jobShutdown, JobProcessSynchronizer jobProcessSynchronizer){
        return new DefaultFlowManage(kubernetesConfig,jobSchedule,jobProcessSynchronizer,jobShutdown);
    }

}