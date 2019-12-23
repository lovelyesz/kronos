package com.yz.kronos.demo.config;

import com.alibaba.fastjson.JSONObject;
import com.yz.kronos.ExecuteConstant;
import com.yz.kronos.exception.JobException;
import com.yz.kronos.execute.JobInfoQueue;
import com.yz.kronos.model.JobInfo;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * redis实现的job信息队列
 * @author shanchong
 * @date 2019-12-23
 **/
@Component
public class RedisJobInfoQueue implements JobInfoQueue {

    @Autowired
    private RedissonClient redissonClient;
    /**
     * 获取队列中的一个元素
     *
     * @return
     * @throws JobException
     */
    @Override
    public JobInfo lpop() throws JobException {
        final String env = System.getenv(ExecuteConstant.KRONOS_EXECUTOR_ENV_NAME);
        final Object cache = redissonClient.getBlockingQueue(env).poll();
        if (cache==null){
            throw new JobException("not find cache form queue");
        }
        return JSONObject.parseObject(cache.toString(),JobInfo.class);
    }
}
