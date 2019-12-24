package com.yz.kronos.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author shanchong
 * @date 2019-11-08
 **/
@Entity
@Data
@Table(name = "namespace_info")
public class NamespaceInfoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nsName;

    private String cmd;

    private String image;

    private String optUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private Integer isDelete;
}
