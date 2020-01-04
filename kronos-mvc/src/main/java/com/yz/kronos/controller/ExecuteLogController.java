package com.yz.kronos.controller;

import com.yz.kronos.from.ExecuteLogForm;
import com.yz.kronos.model.ExecuteLogModel;
import com.yz.kronos.model.PageResult;
import com.yz.kronos.service.ExecuteLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;


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

    @RequestMapping(value = "/list",method = {RequestMethod.GET,RequestMethod.POST})
    public PageResult<ExecuteLogModel> list(ExecuteLogForm form){
        log.info("{}",form);
        final PageResult<ExecuteLogModel> page = executeLogService.page(form.getFlowName(),form.getPage(),form.getLimit());
        page.setCondition(form);
        return page;
    }

}
