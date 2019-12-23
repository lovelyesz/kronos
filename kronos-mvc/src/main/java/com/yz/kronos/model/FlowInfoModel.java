package com.yz.kronos.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @author shanchong
 * @date 2019-11-07
 **/
@Table(name = "flow_info")
@Entity
@Data
public class FlowInfoModel extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String flowName;

    private Long namespaceId;

    private String cron;

    @Column(name = "`status`")
    private Integer status;

    private Integer isDelete;

    @Transient
    List<JobInfoModel> jobList;

}
