package com.yz.kronos.service;

import com.yz.kronos.model.FlowInfoModel;
import com.yz.kronos.model.PageResult;

import java.util.List;
import java.util.Set;

/**
 * 工作流管理
 * @author shanchong
 */
public interface FlowInfoService {

    FlowInfoModel get(Long id);

    FlowInfoModel save(FlowInfoModel flowInfoModel);

    void delete(Long id);

    void schedule(Long flowId);

    void shutdown(Long flowId);

    List<FlowInfoModel> selectByIds(Set<Long> flowIds);

    List<FlowInfoModel> selectByFlowName(String flowName);

    PageResult<FlowInfoModel> page(Long namespaceId, Integer page, Integer limit);
}
