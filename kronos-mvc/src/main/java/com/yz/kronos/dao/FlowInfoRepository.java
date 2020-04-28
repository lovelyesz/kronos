package com.yz.kronos.dao;

import com.yz.kronos.schedule.model.FlowInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface FlowInfoRepository extends JpaRepository<FlowInfoModel,Long> {

    /**
     * 根据状态查询
     * @param status
     * @return
     */
    List<FlowInfoModel> findByStatus(Integer status);

    /**
     * 根据namespace_id查询
     * @param namespaceId
     * @param isDelete
     * @return
     */
    List<FlowInfoModel> findByNamespaceIdAndIsDelete(Long namespaceId,Integer isDelete);

    List<FlowInfoModel> findByIdIn(Collection<Long> ids);

    List<FlowInfoModel> findByFlowNameLike(String flowName);

}
