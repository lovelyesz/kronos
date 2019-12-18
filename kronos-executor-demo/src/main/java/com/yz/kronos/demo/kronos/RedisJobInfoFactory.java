package com.yz.kronos.demo.kronos;

import com.alibaba.fastjson.JSONObject;
import com.yz.kronos.ExecuteConstant;
import com.yz.kronos.exception.JobException;

import com.yz.kronos.execute.JobInfoFactory;
import com.yz.kronos.model.ExecuteJobInfo;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shanchong
 * @date 2019-11-13
 **/
@Component
public class RedisJobInfoFactory implements JobInfoFactory {

    @Autowired
    RedissonClient redissonClient;

    @Override
    public ExecuteJobInfo executeJobInfo() throws JobException {
        Object cache = redissonClient.getQueue(ExecuteConstant.KRONOS_EXECUTOR_ENV_NAME).poll();
        if (cache==null){
            return null;
        }
        return JSONObject.parseObject(cache.toString(), ExecuteJobInfo.class);
    }

}
