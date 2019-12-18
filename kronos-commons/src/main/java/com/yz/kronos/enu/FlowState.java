package com.yz.kronos.enu;

/**
 * @author shanchong
 * @date 2019-11-13
 **/
public enum FlowState {

    INIT(0),
    RUNNABLE(1),
    RUNNING(2),
    CLOSE(3);

    int code;

    FlowState(int code){
        this.code = code;
    }

    public Integer code(){
        return this.code;
    }

}
