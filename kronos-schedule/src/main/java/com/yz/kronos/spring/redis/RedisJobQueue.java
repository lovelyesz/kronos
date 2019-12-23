package com.yz.kronos.spring.redis;

import com.yz.kronos.ExecuteConstant;
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

    /**
     * 拉取一个
     *
     * @return
     */
    @Override
    public String lpop(String keyName) {
        final RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue(keyName);
        return blockingQueue.poll();
    }

    /**
     * 存放任务信息
     *
     * @param s    任务信息
     * @param size 数量
     */
    @Override
    public void add(String keyName,String s, int size) {
        final RBatch batch = redissonClient.createBatch();
        final RBlockingQueueAsync<Object> blockingQueue = batch.getBlockingQueue(keyName);
        for (int i = 0; i < size; i++) {
            blockingQueue.addAsync(s);
            blockingQueue.expireAsync(ExecuteConstant.KRONOS_EXECUTOR_EXPIRE_TIME,ExecuteConstant.KRONOS_EXECUTOR_EXPIRE_TIME_UNIT);
        }
        batch.execute();

    }

    /**
     * 存放一个任务信息
     *
     * @param s
     */
    @Override
    public void add(String keyName,String s) {
        final RBlockingQueue<Object> blockingQueue = redissonClient.getBlockingQueue(keyName);
        blockingQueue.add(s);
        blockingQueue.expire(ExecuteConstant.KRONOS_EXECUTOR_EXPIRE_TIME,ExecuteConstant.KRONOS_EXECUTOR_EXPIRE_TIME_UNIT);
    }

    /**
     * 清空队列
     *
     * @param key
     */
    @Override
    public void clear(String key) {
        redissonClient.getBlockingQueue(key).clear();
    }


}
