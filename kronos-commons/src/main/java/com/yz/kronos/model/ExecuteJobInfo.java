package com.yz.kronos.model;

import lombok.Data;

/**
 * @author shanchong
 * @date 2019-11-07
 **/
@Data
public class ExecuteJobInfo {

    private String clazz;

    private Integer totalPartitionNums;

    private Integer partitions;

    private String execId;


}
