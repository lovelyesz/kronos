package com.yz.meson.config;

import com.alibaba.fastjson.JSONObject;
import com.yz.meson.ExecuteConstant;
import com.yz.meson.model.ExecuteJobInfo;
import com.yz.meson.schedule.MesonJobQueueManage;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 任务队列管理
 * @author shanchong
 * @date 2019-11-13
 **/
@Component
public class RedisJobQueueManage implements MesonJobQueueManage {

    @Autowired
    RedissonClient redissonClient;

    @Override
    public void add(String key, ExecuteJobInfo executeJobInfo) {
        final RQueue<Object> queue = redissonClient.getQueue(key);
        queue.add(JSONObject.toJSON(executeJobInfo));
        queue.expire(ExecuteConstant.MESON_EXECUTOR_EXPIRE_TIME,ExecuteConstant.MESON_EXECUTOR_EXPIRE_TIME_UNIT);
    }

    @Override
    public void remove(String key) {
        redissonClient.getBucket(key).delete();
    }

}
