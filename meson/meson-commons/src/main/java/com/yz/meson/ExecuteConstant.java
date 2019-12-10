package com.yz.meson;

import java.util.concurrent.TimeUnit;

/**
 * @author shanchong
 * @date 2019-10-29
 **/
public class ExecuteConstant {

    public static String MESON_EXECUTOR_QUEUE_NAME_PRE = "MESON_EXECUTOR_QUEUE_NAME:";

    public static String MESON_EXECUTOR_ENV_NAME = "MESON_EXECUTOR_ENV_NAME";

    public static String MESON_EXECUTOR_SYNCHRONIZER = "MESON_EXECUTOR_SYNCHRONIZER_";


    /**
     * 任务执行的有效时间
     */
    public static long MESON_EXECUTOR_EXPIRE_TIME = 60*60*1;
    public static TimeUnit MESON_EXECUTOR_EXPIRE_TIME_UNIT = TimeUnit.SECONDS;

    public static String MESON_EXECUTE_SYNCHRONIZER_LABEL_NAME = "SYNCHRONIZER";

    public static String MESON_EXECUTE_ID = "exec-id";

    public static String MESON_VOLUME_NAME = "meson-logfile";
}
