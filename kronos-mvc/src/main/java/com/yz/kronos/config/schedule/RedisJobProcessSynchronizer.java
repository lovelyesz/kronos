package com.yz.kronos.config.schedule;

import com.yz.kronos.schedule.synchronizer.JobProcessSynchronizer;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author shanchong
 * @date 2019-12-20
 **/
public class RedisJobProcessSynchronizer implements JobProcessSynchronizer {

    RedissonClient redissonClient;

    public RedisJobProcessSynchronizer(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void init(String key, int count) {
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch(key);
        countDownLatch.trySetCount(count);
    }

    @Override
    public void countDown(String key) {
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch(key);
        countDownLatch.countDown();
    }

    @Override
    public void wait(String key, long time, TimeUnit unit) {
        try {
            RCountDownLatch countDownLatch = redissonClient.getCountDownLatch(key);
            countDownLatch.await(time, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空同步器
     *
     * @param key
     */
    @Override
    public void delete(String key) {
        redissonClient.getCountDownLatch(key).delete();
    }
}
