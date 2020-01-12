package com.yz.kronos.spring.redis;

import com.alibaba.fastjson.JSONObject;
import com.yz.kronos.ExecuteConstant;
import com.yz.kronos.JobInfo;
import com.yz.kronos.schedule.queue.JobQueue;
import org.redisson.api.RBatch;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RBlockingQueueAsync;
import org.redisson.api.RedissonClient;

/**
 * @author shanchong
 * @date 2019-12-23
 **/
public class RedisJobQueue implements JobQueue {

    RedissonClient redissonClient;

    public RedisJobQueue(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public String key(Long execId) {
        return ExecuteConstant.KRONOS_EXECUTOR_QUEUE_NAME_PRE + execId;
    }

    /**
     * 存放任务信息
     *
     * @param jobInfo    任务信息
     * @param size 数量
     */
    @Override
    public void add(Long execId,JobInfo jobInfo, int size) {
        final RBatch batch = redissonClient.createBatch();
        final RBlockingQueueAsync<Object> blockingQueue = batch.getBlockingQueue(key(execId));
        for (int i = 0; i < size; i++) {
            jobInfo.setIndex(i);
            blockingQueue.addAsync(JSONObject.toJSONString(jobInfo));
            blockingQueue.expireAsync(ExecuteConstant.KRONOS_EXECUTOR_EXPIRE_TIME,ExecuteConstant.KRONOS_EXECUTOR_EXPIRE_TIME_UNIT);
        }
        batch.execute();

    }

    /**
     * 清空队列
     *
     * @param execId
     */
    @Override
    public void clear(Long execId) {
        redissonClient.getBlockingQueue(key(execId)).clear();
    }


}
