package com.yz.kronos.controller;

import com.yz.kronos.from.ExecuteLogForm;
import com.yz.kronos.model.ExecuteLogModel;
import com.yz.kronos.model.PageResult;
import com.yz.kronos.service.ExecuteLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author shanchong
 * @date 2019-11-22
 **/
@CrossOrigin
@RestController
@RequestMapping(value = "/exec/log")
public class ExecuteLogController {

    @Autowired
    ExecuteLogService executeLogService;

    @GetMapping(value = "/list")
    public PageResult<ExecuteLogModel> list(ExecuteLogForm form){
        final PageResult<ExecuteLogModel> page = executeLogService.page(form.getFlowName(),form.getPage(),form.getLimit());
        page.setCondition(form);
        return page;
    }

}
