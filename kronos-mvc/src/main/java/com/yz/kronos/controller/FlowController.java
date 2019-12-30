package com.yz.kronos.controller;

import com.yz.kronos.CallResultConstant;
import com.yz.kronos.from.FlowForm;
import com.yz.kronos.model.CallResult;
import com.yz.kronos.model.FlowInfoModel;
import com.yz.kronos.model.PageResult;
import com.yz.kronos.service.FlowInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author shanchong
 * @date 2019-11-07
 **/
@CrossOrigin
@RestController
@RequestMapping(value = "/flow")
public class FlowController {

    @Autowired
    FlowInfoService flowInfoService;

    @GetMapping(value = "/list")
    public PageResult<FlowInfoModel> list(FlowForm form){
        final PageResult<FlowInfoModel> page = flowInfoService.page(form.getNamespaceId(),
                form.getPage()-1,form.getLimit());
        page.setCondition(form);
        return page;
    }

    @PostMapping(value = "/save")
    public CallResult save(FlowInfoModel flowInfoModel){
        final FlowInfoModel model = flowInfoService.save(flowInfoModel);
        return CallResult.builder()
                .code(CallResultConstant.SUCCESS_CODE)
                .msg(CallResultConstant.SUCCESS_MESSAGE)
                .data(model)
                .build();
    }


}
