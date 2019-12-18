package com.yz.kronos.dao;

import com.yz.kronos.model.FlowInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowInfoRepository extends JpaRepository<FlowInfoModel,Long> {

    List<FlowInfoModel> findByState(Integer state);

}
