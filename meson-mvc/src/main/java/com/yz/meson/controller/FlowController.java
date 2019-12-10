package com.yz.meson.controller;

import com.yz.meson.CallResultConstant;
import com.yz.meson.model.CallResult;
import com.yz.meson.model.FlowInfoModel;
import com.yz.meson.service.FlowInfoService;
import com.yz.meson.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
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
    @Autowired
    ScheduleService scheduleService;

    @GetMapping(value = "/list")
    public List<FlowInfoModel> list(FlowInfoModel flowInfoModel){
        return flowInfoService.list(flowInfoModel);
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
        scheduleService.runFlow(id);
        return CallResult.builder()
                .code(CallResultConstant.SUCCESS_CODE)
                .message(CallResultConstant.SUCCESS_MESSAGE)
                .build();
    }

    @PostMapping(value = "/shutdown/{id}")
    public CallResult shutdown(@PathVariable Long id){
        scheduleService.stopFlow(id);
        return CallResult.builder()
                .code(CallResultConstant.SUCCESS_CODE)
                .message(CallResultConstant.SUCCESS_MESSAGE)
                .build();
    }


}
