package com.yz.meson.dao;

import com.yz.meson.model.JobRelationModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRelationRepository extends JpaRepository<JobRelationModel,Long> {

    List<JobRelationModel> findByFlowId(Long flowId);


}
