package com.yz.kronos;

import lombok.Data;

/**
 * @author shanchong
 * @date 2019-11-12
 **/
@Data
public class KubernetesConfig {

    /**
     * 重试策略
     * @return
     */
    private String restartPolicy;

    /**
     * kubernetes的地址
     * @return
     */
    private String serviceApi;

    /**
     * 镜像拉取策略
     * @return
     */
    private String imagePullPolicy;

    /**
     * 日志输出路径
     * @return
     */
    private String logPath;

    private String apiVersion;

    private String groupName;

    /**
     * 刷新任务状态的频率
     */
    private Long resyncPeriodInMillis;

}
