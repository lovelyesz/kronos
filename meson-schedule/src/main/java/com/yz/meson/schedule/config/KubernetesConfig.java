package com.yz.meson.schedule.config;

import com.yz.meson.schedule.MesonScheduleSupport;

/**
 * @author shanchong
 * @date 2019-11-12
 **/
public interface KubernetesConfig extends MesonScheduleSupport {

    String getRestartPolicy();

    String getServiceApi();

    String getImagePullPolicy();

    String getLogPath();

}
