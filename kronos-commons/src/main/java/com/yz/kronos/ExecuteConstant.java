package com.yz.kronos;

import java.util.concurrent.TimeUnit;

/**
 * @author shanchong
 * @date 2019-10-29
 **/
public class ExecuteConstant {

    public static String KRONOS_EXECUTOR_QUEUE_NAME_PRE = "KRONOS_EXECUTOR_QUEUE_NAME:";

    public static String KRONOS_EXECUTOR_ENV_NAME = "KRONOS_EXECUTOR_ENV_NAME";

    public static String KRONOS_EXECUTOR_SYNCHRONIZER = "KRONOS_EXECUTOR_SYNCHRONIZER_";

    public static String KRONOS_EXECUTOR_FLOW_PROCESS_SET = "KRONOS_EXECUTOR_FLOW_PROCESS_SET";


    /**
     * 任务执行的有效时间
     */
    public static long KRONOS_EXECUTOR_EXPIRE_TIME = 60*60*1;
    public static TimeUnit KRONOS_EXECUTOR_EXPIRE_TIME_UNIT = TimeUnit.SECONDS;

    public static String KRONOS_EXECUTE_SYNCHRONIZER_LABEL_NAME = "SYNCHRONIZER";

    public static String KRONOS_EXECUTE_ID = "EXECID";

    public static String KRONOS_VOLUME_NAME = "kronos-logfile";
}
