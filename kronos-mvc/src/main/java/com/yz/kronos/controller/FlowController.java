package com.yz.kronos.controller;

import com.yz.kronos.CallResultConstant;
import com.yz.kronos.model.CallResult;
import com.yz.kronos.model.FlowInfoModel;
import com.yz.kronos.service.FlowInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author shanchong
 * @date 2019-11-07
 **/
@RestController
@RequestMapping(value = "/flow")
public class FlowController {

    @Autowired
    FlowInfoService flowInfoService;

    @GetMapping(value = "/list")
    public List<FlowInfoModel> list(Long namespaceId){
        return flowInfoService.list(namespaceId);
    }

    @PostMapping(value = "/save")
    public CallResult save(FlowInfoModel flowInfoModel){
        final FlowInfoModel model = flowInfoService.save(flowInfoModel);
        return CallResult.builder()
                .code(CallResultConstant.SUCCESS_CODE)
                .message(CallResultConstant.SUCCESS_MESSAGE)
                .data(model)
                .build();
    }

    @DeleteMapping(value = "/delete/{id}")
    public CallResult delete(@PathVariable Long id){
        flowInfoService.delete(id);
        return CallResult.builder()
                .code(CallResultConstant.SUCCESS_CODE)
                .message(CallResultConstant.SUCCESS_MESSAGE)
                .build();
    }

    @GetMapping(value = "/get/{id}")
    public FlowInfoModel get(@PathVariable Long id){
        return flowInfoService.get(id);
    }

    @PostMapping(value = "/run/{id}")
    public CallResult run(@PathVariable Long id){
        flowInfoService.schedule(id);
        return CallResult.builder()
                .code(CallResultConstant.SUCCESS_CODE)
                .message(CallResultConstant.SUCCESS_MESSAGE)
                .build();
    }

    @PostMapping(value = "/shutdown/{id}")
    public CallResult shutdown(@PathVariable Long id){
        flowInfoService.shutdown(id);
        return CallResult.builder()
                .code(CallResultConstant.SUCCESS_CODE)
                .message(CallResultConstant.SUCCESS_MESSAGE)
                .build();
    }


}
