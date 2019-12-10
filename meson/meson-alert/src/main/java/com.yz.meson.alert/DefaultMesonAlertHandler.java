package com.yz.meson.alert;

import com.yz.meson.exception.MesonException;
import com.yz.meson.model.ExecuteJobInfo;

/**
 * 默认的告警处理类
 * @author shanchong
 * @date 2019-11-14
 **/
public class DefaultMesonAlertHandler implements MesonAlertHandler {

    @Override
    public void handle(ExecuteJobInfo executeJobInfo, MesonException e) {
        //do nothing
    }

    @Override
    public void handle(MesonException e) {
        handle(null,e);
    }
}
