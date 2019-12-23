package com.yz.kronos.api;

import com.yz.kronos.model.FlowInfoModel;
import com.yz.kronos.service.FlowInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author shanchong
 * @date 2019-12-19
 **/
@RestController
@RequestMapping(value = "/api/flow")
public class FlowApi {

    @Autowired
    private FlowInfoService flowInfoService;

    @GetMapping(value = "/list")
    public List<FlowInfoModel> list(Long namespaceId){
        return flowInfoService.list(namespaceId);
    }



}
