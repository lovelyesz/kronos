package com.yz.meson.model;

import com.yz.meson.enu.JobState;
import lombok.Data;

/**
 * 任务执行结果
 * @author shanchong
 * @date 2019-11-14
 **/
@Data
public class ExecuteJobResult {

    ExecuteJobInfo executeJobInfo;

    JobState jobState;

    String message;

}
