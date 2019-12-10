package com.yz.meson.dao;


import com.yz.meson.model.JobInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author shanchong
 * @date 2019-11-07
 **/
public interface JobInfoRepository extends JpaRepository<JobInfoModel,Long> {

    List<JobInfoModel> findByIdIn(List<Long> ids);

}
