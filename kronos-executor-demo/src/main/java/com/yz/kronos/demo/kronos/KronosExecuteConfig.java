package com.yz.kronos.demo.kronos;

import com.yz.kronos.exception.JobException;
import com.yz.kronos.execute.DefaultJobExecuteFactory;
import com.yz.kronos.execute.IsolatedJavaJob;
import com.yz.kronos.execute.JobExecuteFactory;
import com.yz.kronos.model.ExecuteJobInfo;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shanchong
 * @date 2019-12-05
 **/
@Configuration
public class KronosExecuteConfig implements ApplicationContextAware {

    ApplicationContext applicationContext;

    @ConditionalOnProperty(prefix = "kronos.execute",name = "enable",havingValue = "true")
    @Bean(initMethod = "init")
    public JobExecuteFactory executeManage(RedisJobInfoFactory redisJobInfoFactory)
            throws ClassNotFoundException, JobException {
        ExecuteJobInfo executeJobInfo = redisJobInfoFactory.executeJobInfo();
        String clazz = executeJobInfo.getClazz();
        IsolatedJavaJob job = (IsolatedJavaJob) applicationContext.getBean(Class.forName(clazz));
        DefaultJobExecuteFactory jobExecuteFactory = new DefaultJobExecuteFactory();
        jobExecuteFactory.setExecuteJobInfo(executeJobInfo);
        jobExecuteFactory.setIsolatedJavaJob(job);
        return jobExecuteFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
