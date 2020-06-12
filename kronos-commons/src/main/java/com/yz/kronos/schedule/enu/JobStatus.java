package com.yz.kronos.schedule.enu;

/**
 * @author shanchong
 * @date 2019-11-12
 **/
public enum JobStatus {

    INIT(0,"任务初始化"),
    RUNNING(1,"正在执行"),
    SUCCESS(2,"执行成功"),
    FAIL(3,"执行失败"),
    SHUTDOWN(4,"手动关闭"),
    UNKNOW(9,"未知状态");

    int code;
    String desc;

    JobStatus(int code, String desc){
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
        final JobStatus success = valueOf("SUCCESS");
        System.out.println(success);
    }
}
