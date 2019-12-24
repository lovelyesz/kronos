package com.yz.kronos.schedule.job;

import com.yz.kronos.KubernetesConfig;
import com.yz.kronos.schedule.queue.JobQueue;

/**
 * 任务关停
 * @author shanchong
 */
public interface JobShutdown {

    /**
     * 任务信息队列
     * @return
     */
    JobQueue queue();

    /**
     * 任务关停
     * @param execId
     * @param config
     */
    void shutdown(String execId, KubernetesConfig config);


    abstract class BaseJobShutdown implements JobShutdown {

        /**
         * 任务关停
         *
         * @param execId
         */
        @Override
        public void shutdown(String execId,KubernetesConfig config) {
            queue().clear(config.getExecutorQueueNamePre()+execId);
        }
    }

}
