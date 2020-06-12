package com.yz.kronos.schedule.enu;

/**
 * @author shanchong
 * @date 2019-11-13
 **/
public enum FlowStatus {

    INIT(0),
    RUNNABLE(1),
    RUNNING(2);

    int code;

    FlowStatus(int code){
        this.code = code;
    }

    public Integer code(){
        return this.code;
    }

}
