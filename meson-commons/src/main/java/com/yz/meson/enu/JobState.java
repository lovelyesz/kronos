package com.yz.meson.enu;

/**
 * @author shanchong
 * @date 2019-11-12
 **/
public enum JobState {

    INIT(0,"任务初始化"),
    SCHEDULED(1,"调度完成"),
//    EXECUTING(2,"正在执行"),
    SUCCESS(3,"执行成功"),
    FAIL(4,"执行失败"),
    SHUTDOWN(5,"手动关闭"),
    UNKNOW(9,"未知状态");

    int code;
    String desc;

    JobState(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public int code(){
        return this.code;
    }

    public String desc(){
        return this.desc;
    }

    public static void main(String[] args) {
        final JobState success = valueOf("SUCCESS");
        System.out.println(success);
    }
}
