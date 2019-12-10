package com.yz.meson.execute;

import com.yz.meson.exception.MesonException;
import com.yz.meson.model.ExecuteJobInfo;

/**
 * 工作队列
 * @author shanchong
 */
public interface MesonJobInfoFactory {

    /**
     * 获取队列中的一个元素
     * @return
     * @throws MesonException
     */
    ExecuteJobInfo executeJobInfo() throws MesonException;

}
