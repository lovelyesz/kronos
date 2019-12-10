package com.yz.meson.demo.dao;

import com.yz.meson.demo.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author shanchong
 * @date 2019-11-13
 **/
public interface UserDao extends JpaRepository<UserInfo,Long> {

}
