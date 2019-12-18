package com.yz.kronos.enums;

/**
 * @author shanchong
 * @date 2019-11-22
 **/
public enum YesNoEnum {

    YES(1),
    NO(0);

    int code;

    YesNoEnum(int code){
        this.code = code;
    }

    public int code(){
        return this.code;
    }

}
