package com.yz.kronos.dao;

import com.yz.kronos.model.ExecuteLogModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExecuteLogRepository extends JpaRepository<ExecuteLogModel,Long> {

    List<ExecuteLogModel> findByFlowIdAndStateIn(Long flowId,List<Integer> state);

}
