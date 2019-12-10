package com.yz.meson.demo.meson;

import com.yz.meson.exception.MesonException;
import com.yz.meson.execute.DefaultMesonExecuteFactory;
import com.yz.meson.execute.IsolatedJavaJob;
import com.yz.meson.execute.MesonExecuteFactory;
import com.yz.meson.model.ExecuteJobInfo;
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
public class MesonExecuteConfig implements ApplicationContextAware {

    ApplicationContext applicationContext;

    @ConditionalOnProperty(prefix = "meson.execute",name = "enable",havingValue = "true")
    @Bean(initMethod = "init")
    public MesonExecuteFactory executeManage(RedisMesonJobInfoFactory redisMesonJobQueue)
            throws ClassNotFoundException, MesonException {
        ExecuteJobInfo executeJobInfo = redisMesonJobQueue.executeJobInfo();
        String clazz = executeJobInfo.getClazz();
        IsolatedJavaJob job = (IsolatedJavaJob) applicationContext.getBean(Class.forName(clazz));
        DefaultMesonExecuteFactory mesonExecuteFactory = new DefaultMesonExecuteFactory();
        mesonExecuteFactory.setExecuteJobInfo(executeJobInfo);
        mesonExecuteFactory.setIsolatedJavaJob(job);
        return mesonExecuteFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
