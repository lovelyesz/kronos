package com.yz.meson.demo.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author shanchong
 * @date 2019-11-13
 **/
@Table(name = "user_info")
@Entity
@Data
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    private String email;

    private Integer age;

    private String address;

}
