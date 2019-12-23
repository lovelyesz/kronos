package com.yz.kronos.execute;

import com.yz.kronos.exception.JobException;
import com.yz.kronos.model.JobInfo;

/**
 * 工作队列
 * @author shanchong
 */
public interface JobInfoQueue {

    /**
     * 获取队列中的一个元素
     * @return
     * @throws JobException
     */
    JobInfo lpop() throws JobException;

}
