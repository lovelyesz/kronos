package com.yz.kronos.dao;

import com.yz.kronos.schedule.model.JobFlowModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface JobRelationRepository extends JpaRepository<JobFlowModel,Long> {

    List<JobFlowModel> findByFlowId(Long flowId);

    List<JobFlowModel> findByFlowIdIn(Collection<Long> flowIds);

    @Modifying
    @Transactional
    @Query("update JobFlowModel set shareTotal=:shareTotal where jobId=:jobId and flowId=:flowId")
    void updateShareTotalByJobIdAndFlowId(@Param(value = "shareTotal") Integer shareTotal,
                                 @Param(value = "jobId") Long jobId,
                                          @Param(value = "flowId") Long flowId);
}
