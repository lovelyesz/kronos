package com.yz.kronos.model;

import com.yz.kronos.enu.JobState;
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
