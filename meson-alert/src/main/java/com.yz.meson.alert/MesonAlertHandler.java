package com.yz.meson.alert;

import com.yz.meson.exception.MesonException;
import com.yz.meson.model.ExecuteJobInfo;

/**
 * 告警处理器
 * @author shanchong
 */
public interface MesonAlertHandler {

    void handle(ExecuteJobInfo executeJobInfo, MesonException e);

    void handle(MesonException e);

}
