package com.yz.kronos.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author shanchong
 * @date 2019-11-12
 **/
@Data
@Entity
@Table(name = "execute_log")
public class ExecuteLogModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GTM+8")
    private Date createTime;

    @Column(insertable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GTM+8")
    private Date finishTime;

    @Column(updatable = false)
    private Long jobId;

    @Column(updatable = false)
    private Long flowId;

    @Column(name = "`status`")
    private Integer status;

    private String remark;

    @Column(updatable = false)
    private Integer shareTotal;

    private Integer activeCount;

    private Integer succeedCount;

    private Integer failedCount;

    private String batchNo;

    @Transient
    private JobInfoModel jobInfo;
    @Transient
    private FlowInfoModel flowInfo;

}
