package com.yz.kronos.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author shanchong
 * @date 2019-11-07
 **/
@Table(name = "job_info")
@Entity
@Data
public class JobInfoModel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobName;

    private String clazz;

    /**
     * 资源
     * {"cpu":1,"memory":256}
     */
    private String resources;

    private Integer isDelete;

}
