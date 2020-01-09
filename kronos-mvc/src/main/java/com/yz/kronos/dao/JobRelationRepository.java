package com.yz.kronos.dao;

import com.yz.kronos.model.JobRelationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface JobRelationRepository extends JpaRepository<JobRelationModel,Long> {

    List<JobRelationModel> findByFlowId(Long flowId);

    List<JobRelationModel> findByFlowIdIn(Collection<Long> flowIds);

    @Modifying
    @Transactional
    @Query("update JobRelationModel set shareTotal=:shareTotal where jobId=:jobId")
    void updateShareTotalByJobId(@Param(value = "shareTotal") Integer shareTotal,
                                 @Param(value = "jobId") Long jobId);
}
