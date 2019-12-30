package com.yz.kronos.dao;

import com.yz.kronos.model.ExecuteLogModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ExecuteLogRepository extends JpaRepository<ExecuteLogModel,Long> {

    List<ExecuteLogModel> findByFlowIdAndStatusIn(Long flowId,List<Integer> state);

    Page<ExecuteLogModel> findByFlowIdIn(Set<Long> flowIds, Pageable pageable);

}
