package com.yz.kronos.dao;


import com.yz.kronos.schedule.model.JobInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * @author shanchong
 * @date 2019-11-07
 **/
public interface JobInfoRepository extends JpaRepository<JobInfoModel,Long> {

    List<JobInfoModel> findByIdIn(Collection<Long> ids);

}
