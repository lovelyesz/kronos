package com.yz.kronos.config;

import com.yz.kronos.schedule.ScheduleSynchronizer;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author shanchong
 * @date 2019-11-13
 **/
@Component
public class RedisScheduleSynchronizer implements ScheduleSynchronizer {

    @Autowired
    RedissonClient redissonClient;

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

}
