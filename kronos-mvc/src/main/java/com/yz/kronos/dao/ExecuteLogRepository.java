package com.yz.kronos.dao;

import com.yz.kronos.schedule.model.ExecuteLogModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ExecuteLogRepository extends JpaRepository<ExecuteLogModel,Long> {

    List<ExecuteLogModel> findByFlowIdAndStatusIn(Long flowId,List<Integer> state);

    Page<ExecuteLogModel> findByFlowIdIn(Set<Long> flowIds, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update ExecuteLogModel set finishTime=:finishTime,status=:status,remark=:remark where id =:id")
    int updateStatus(@Param(value = "finishTime")Date finishTime,
                     @Param(value = "status")Integer status,
                     @Param(value = "remark")String remark,
                     @Param(value = "id")Long id);

    List<ExecuteLogModel> findByBatchNo(String batchNo);
}
