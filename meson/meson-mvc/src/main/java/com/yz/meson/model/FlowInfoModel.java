package com.yz.meson.model;

import lombok.Data;

import javax.persistence.*;

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

    private Integer state;

    private Integer isDelete;

}
