package com.yz.kronos.controller;

import com.yz.kronos.CallResultConstant;
import com.yz.kronos.model.CallResult;
import com.yz.kronos.model.FlowInfoModel;
import com.yz.kronos.service.FlowInfoService;
import com.yz.kronos.service.ScheduleService;
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
