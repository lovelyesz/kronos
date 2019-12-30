package com.yz.kronos.model;

/**
 * @author shanchong
 * @date 2019-12-24
 **/
public class CallResultBuilder {

    private static int successCode = 1;
    private static String successMsg = "成功";

    public static <T> CallResult of(T data){
        return CallResult.<T>builder().data(data).build();
    }

    public static CallResult success(){
        return CallResult.builder().code(successCode).msg(successMsg).build();
    }

    public static <T> CallResult<T> success(T data){
        return CallResult.<T>builder().code(successCode).msg(successMsg).data(data).build();
    }

    public static CallResult fail(int code,String msg){
        return CallResult.builder().code(code).msg(msg).build();
    }

    public static CallResult fail(String msg){
        return CallResult.builder().code(-1).msg(msg).build();
    }
}
