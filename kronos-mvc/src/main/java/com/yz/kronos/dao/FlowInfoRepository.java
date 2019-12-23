package com.yz.kronos.dao;

import com.yz.kronos.model.FlowInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

}
