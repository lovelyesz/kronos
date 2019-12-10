package com.yz.meson.execute;

/**
 * @author shanchong
 * @date 2019-12-03
 **/
public interface MesonErrorHandle {

    /**
     * 任务执行失败
     * @param partitions
     * @param totalPartitionNums
     * @param execId
     * @param e
     */
    void errorHandle(Integer partitions, Integer totalPartitionNums, String execId, Exception e);

}
