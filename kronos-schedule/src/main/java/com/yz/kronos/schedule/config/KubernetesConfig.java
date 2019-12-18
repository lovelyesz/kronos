package com.yz.kronos.schedule.config;

/**
 * @author shanchong
 * @date 2019-11-12
 **/
public interface KubernetesConfig {

    /**
     * 重试策略
     * @return
     */
    String getRestartPolicy();

    /**
     * kubernetes的地址
     * @return
     */
    String getServiceApi();

    /**
     * 镜像拉取策略
     * @return
     */
    String getImagePullPolicy();

    /**
     * 日志输出路径
     * @return
     */
    String getLogPath();

}
