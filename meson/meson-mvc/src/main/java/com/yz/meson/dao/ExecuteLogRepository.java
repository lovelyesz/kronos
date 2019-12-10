package com.yz.meson.dao;

import com.yz.meson.model.ExecuteLogModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExecuteLogRepository extends JpaRepository<ExecuteLogModel,Long> {

    List<ExecuteLogModel> findByFlowIdAndStateIn(Long flowId,List<Integer> state);

}
