package com.yz.meson.schedule.config;

import lombok.Data;

import java.util.Map;

@Data
public class MesonJobExecutor {

    /**
     * 工作空间
     */
    String namespace;

    /**
     * 执行ID
     */
    Long execLogId;

    Long jobId;

    Long flowId;

    public String getExecId(){
        return execLogId+"-"+flowId+"-"+jobId;
    }

    /**
     * 申请资源
     */
    Map<String,String> resources;

    /**
     * 镜像
     */
    String image;

    /**
     * 执行脚本
     */
    String cmd;

    /**
     * 环境变量
     */
    Map<String,String> labels;

    /**
     * 分片数量
     */
    Integer shareCount;

    /**
     * 触发类
     */
    String clazz;

    /**
     * 排序
     */
    Integer sort;

}
