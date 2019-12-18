package com.yz.kronos.controller;

import com.yz.kronos.model.ExecuteLogModel;
import com.yz.kronos.service.ExecuteLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author shanchong
 * @date 2019-11-22
 **/
@RestController
@RequestMapping(value = "/log")
public class ExecuteLogController {

    @Autowired
    ExecuteLogService executeLogService;

    @GetMapping(value = "/list")
    public List<ExecuteLogModel> list(ExecuteLogModel model){
        return executeLogService.list(model);
    }
}
