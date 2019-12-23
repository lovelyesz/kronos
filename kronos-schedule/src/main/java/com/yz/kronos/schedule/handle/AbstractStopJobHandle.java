package com.yz.kronos.schedule.handle;

import com.yz.kronos.model.JobInfo;
import org.redisson.api.RedissonClient;

/**
 * @author shanchong
 * @date 2019-12-23
 **/
public abstract class AbstractStopJobHandle implements JobHandle {

    JobInfo jobInfo;
    RedissonClient redissonClient;

    /**
     * 开始任务
     *
     * @param execId
     */
    @Override
    public void startJob(String execId) {

    }

    /**
     * 停止任务
     */
    @Override
    public void stopJob() {
        final String synchronizerKey = jobInfo.getSynchronizerKey();

    }
}
