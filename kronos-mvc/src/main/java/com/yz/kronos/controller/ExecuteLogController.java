package com.yz.kronos.controller;

import com.yz.kronos.from.ExecuteLogForm;
import com.yz.kronos.schedule.model.CallResult;
import com.yz.kronos.schedule.model.CallResultBuilder;
import com.yz.kronos.schedule.model.ExecuteLogModel;
import com.yz.kronos.schedule.model.PageResult;
import com.yz.kronos.service.ExecuteLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author shanchong
 * @date 2019-11-22
 **/
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/exec/log")
public class ExecuteLogController {

    @Autowired
    ExecuteLogService executeLogService;

    @RequestMapping(value = "/page",method = {RequestMethod.GET,RequestMethod.POST})
    public PageResult<ExecuteLogModel> page(ExecuteLogForm form){
        log.info("{}",form);
        final PageResult<ExecuteLogModel> page = executeLogService.page(form.getFlowName(),form.getPage()-1,form.getLimit());
        page.setCondition(form);
        return page;
    }

    @GetMapping(value = "/list")
    public CallResult<List<ExecuteLogModel>> list(@RequestParam(value = "batchNo")String batchNo){
        final List<ExecuteLogModel> executeLogModels = executeLogService.findByBatchNo(batchNo);
        return CallResultBuilder.success(executeLogModels);
    }

}
