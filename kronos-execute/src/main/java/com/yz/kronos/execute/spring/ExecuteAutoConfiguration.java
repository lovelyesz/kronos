package com.yz.kronos.execute.spring;

import com.yz.kronos.ExecuteConstant;
import com.yz.kronos.JobInfo;
import com.yz.kronos.alert.AlertHandler;
import com.yz.kronos.alert.DefaultAlertHandler;
import com.yz.kronos.execute.ExecuteErrorHandle;
import com.yz.kronos.execute.IsolatedJavaJob;
import com.yz.kronos.execute.JobInfoQueue;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * kronos 执行器自动配置
 * @author shanchong
 * @date 2019-12-23
 **/
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "kronos.execute",name = "enable",havingValue = "true",matchIfMissing = true)
public class ExecuteAutoConfiguration implements ApplicationContextAware {

    @Bean
    @ConditionalOnMissingBean
    public AlertHandler alertHandler(){
        return new DefaultAlertHandler();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        final AlertHandler alertHandler = applicationContext.getBean(AlertHandler.class);
        final String execId = System.getenv(ExecuteConstant.KRONOS_EXECUTE_ID);
        int index = 0;
        int shareTotal = 0;
        Object bean = null;
        try {
            final JobInfo jobInfo = applicationContext.getBean(JobInfoQueue.class).lpop();
            index = jobInfo.getIndex();
            shareTotal = jobInfo.getShareTotal();
            final String clazz = jobInfo.getClazz();
            bean = applicationContext.getBean(Class.forName(clazz));
        }catch (Exception e){
            alertHandler.handle(e);
        }
        try {
            ((IsolatedJavaJob)bean).execute(index, shareTotal, execId);
        }catch (Exception e){
            if (bean instanceof ExecuteErrorHandle){
                final ExecuteErrorHandle errorHandle = (ExecuteErrorHandle) bean;
                errorHandle.errorHandle(index,shareTotal,execId,e);
            }
            alertHandler.handle(e);
        }
        System.exit(0);
    }
}
