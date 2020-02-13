package com.yz.kronos.execute.spring;

import com.yz.kronos.ExecuteConstant;
import com.yz.kronos.JobInfo;
import com.yz.kronos.execute.ExecuteErrorHandle;
import com.yz.kronos.execute.IsolatedJavaJob;
import com.yz.kronos.execute.JobInfoQueue;
import com.yz.kronos.message.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * kronos 执行器自动配置
 * @author shanchong
 * @date 2019-12-23
 **/
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "kronos.execute",name = "enable",havingValue = "true",matchIfMissing = true)
public class ExecuteAutoConfiguration implements ApplicationContextAware {

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) {
        MessageHandler alertHandler = null;
        String execId = System.getenv(ExecuteConstant.KRONOS_EXECUTE_ID);
        log.info("kronos executor start... execId:{}",execId);
        int index = 0;
        int shareTotal = 0;
        Object bean = null;
        try {
            alertHandler = applicationContext.getBean(MessageHandler.class);
            JobInfo jobInfo = applicationContext.getBean(JobInfoQueue.class).lpop();
            log.info("kronos executor job info {}",jobInfo);
            //分片索引
            index = jobInfo.getIndex();
            //总的分片数量
            shareTotal = jobInfo.getShareTotal();
            //触发类
            String clazz = jobInfo.getClazz();
            bean = applicationContext.getBean(Class.forName(clazz));
        }catch (Exception e){
            e.printStackTrace();
            log.error("kronos executor job info is error",e);
            if (alertHandler!=null){
                alertHandler.handle("[Kronos系统异常告警]","kronos executor job info is error");
            }
        }
        try {
            if (bean==null){
                throw new RuntimeException("kronos executor not find execute bean");
            }
            ((IsolatedJavaJob)bean).execute(index, shareTotal, execId);
        }catch (Exception e){
            e.printStackTrace();
            log.error("kronos executor job execute fail",e);
            if (bean instanceof ExecuteErrorHandle){
                log.warn("kronos executor job execute fail start error handler");
                ExecuteErrorHandle errorHandle = (ExecuteErrorHandle) bean;
                errorHandle.errorHandle(index,shareTotal,execId,e);
            }
            if (alertHandler!=null){
                alertHandler.handle("[Kronos系统异常告警]","kronos executor job execute fail");
            }
        }
        System.exit(0);
    }
}
