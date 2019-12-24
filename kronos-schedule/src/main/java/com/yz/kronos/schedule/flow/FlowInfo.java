package com.yz.kronos.schedule.flow;

import com.yz.kronos.JobInfo;
import lombok.Data;

import java.util.List;

/**
 * 任务流信息
 * @author shanchong
 * @date 2019-12-23
 **/
@Data
public class FlowInfo {

    List<FlowElement> jobInfoList;

    Long flowId;

    @Data
    public static class FlowElement {

        JobInfo jobInfo;

        Integer sort;

        Long jobId;

    }
}
