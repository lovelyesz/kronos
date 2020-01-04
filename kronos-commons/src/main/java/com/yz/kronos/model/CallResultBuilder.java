package com.yz.kronos.model;

import com.yz.kronos.CallResultConstant;

/**
 * @author shanchong
 * @date 2019-12-24
 **/
public class CallResultBuilder {

    private static int successCode = CallResultConstant.SUCCESS_CODE;
    private static String successMsg = CallResultConstant.SUCCESS_MESSAGE;

    public static <T> CallResult of(T data){
        final CallResult<T> callResult = new CallResult<T>();
        callResult.setData(data);
        return callResult;
    }

    public static CallResult success(){
        final CallResult callResult = new CallResult();
        callResult.setCode(successCode);
        callResult.setMsg(successMsg);
        return callResult;
    }

    public static <T> CallResult<T> success(T data){
        final CallResult<T> callResult = new CallResult<T>();
        callResult.setCode(successCode);
        callResult.setMsg(successMsg);
        callResult.setData(data);
        return callResult;
    }

    public static CallResult fail(int code, String msg){
        final CallResult callResult = new CallResult();
        callResult.setCode(code);
        callResult.setMsg(msg);
        return callResult;
    }

    public static CallResult fail(String msg){
        final CallResult callResult = new CallResult();
        callResult.setCode(-1);
        callResult.setMsg(msg);
        return callResult;
    }
}
