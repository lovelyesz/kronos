package com.yz.meson.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author shanchong
 * @date 2019-10-28
 **/
@Component
@ConfigurationProperties(
        prefix = "kubernetes"
)
public class KubernetesConfig implements com.yz.meson.schedule.config.KubernetesConfig{

    private String serviceApi;

    private String restartPolicy;

    private String imagePullPolicy;

    private String logPath;

    @Override
    public String getServiceApi() {
        return serviceApi;
    }

    @Override
    public String getImagePullPolicy() {
        return this.imagePullPolicy;
    }

    public void setImagePullPolicy(String imagePullPolicy) {
        this.imagePullPolicy = imagePullPolicy;
    }

    public void setServiceApi(String serviceApi) {
        this.serviceApi = serviceApi;
    }

    @Override
    public String getRestartPolicy() {
        return restartPolicy;
    }

    public void setRestartPolicy(String restartPolicy) {
        this.restartPolicy = restartPolicy;
    }

    @Override
    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }
}
