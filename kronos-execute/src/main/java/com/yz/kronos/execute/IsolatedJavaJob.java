package com.yz.kronos.execute;


/**
 * 独立运行的javajob
 * @see
 * @author lihonghong
 *
 */
public interface IsolatedJavaJob {

    /**
     * 执行业务
     * @param index
     * @param shareTotal
     * @param execId
     * @throws Exception
     */
    void execute(Integer index, Integer shareTotal, String execId) throws Exception ;


}
