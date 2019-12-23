package com.yz.kronos.util;

import com.yz.kronos.ExecuteConstant;

/**
 * @author shanchong
 * @date 2019-11-12
 **/
public class ExecuteUtil {

    /**
     * 执行id+工作流id+任务id
     * @param execId
     * @param flowId
     * @param jobId
     * @return
     */
    public static String getExecId(Long execId,Long flowId,Long jobId){
        return execId+"-"+flowId+"-"+jobId;
    }

    public static Long getExecLogId(String execId){
        return Long.valueOf(execId.split("-")[0]);
    }

    public static Long getFlowId(String execId){
        return Long.valueOf(execId.split("-")[1]);
    }

    public static Long getJobId(String execId){
        return Long.valueOf(execId.split("-")[2]);
    }

}
