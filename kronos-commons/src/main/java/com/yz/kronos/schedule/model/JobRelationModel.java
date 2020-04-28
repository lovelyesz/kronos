package com.yz.kronos.schedule.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author shanchong
 * @date 2019-11-12
 **/
@Data
@Entity
@Table(name = "job_relation")
public class JobRelationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long flowId;

    private Long jobId;

    private Integer sort;

    private Integer shareTotal;

    private Integer isDelete;
}
