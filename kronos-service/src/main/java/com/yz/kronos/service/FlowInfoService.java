package com.yz.kronos.service;

import com.yz.kronos.model.FlowInfoModel;

import java.util.List;

/**
 * 工作流管理
 * @author shanchong
 */
public interface FlowInfoService {

    FlowInfoModel get(Long id);

    FlowInfoModel save(FlowInfoModel flowInfoModel);

    void delete(Long id);

    /**
     * 工作流列表
     * @param namespaceId
     * @return
     */
    List<FlowInfoModel> list(Long namespaceId);

    void schedule(Long flowId);

    void shutdown(Long flowId);
}
