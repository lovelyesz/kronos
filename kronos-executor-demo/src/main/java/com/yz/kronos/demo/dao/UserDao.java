package com.yz.kronos.demo.dao;

import com.yz.kronos.demo.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author shanchong
 * @date 2019-11-13
 **/
public interface UserDao extends JpaRepository<UserInfo,Long> {

}
