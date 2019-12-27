package com.yz.kronos.controller;

import com.yz.kronos.model.ExecuteLogModel;
import com.yz.kronos.model.PageResult;
import com.yz.kronos.service.ExecuteLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author shanchong
 * @date 2019-11-22
 **/
@RestController
@RequestMapping(value = "/exec/log")
public class ExecuteLogController {

    @Autowired
    ExecuteLogService executeLogService;

    @GetMapping(value = "/list")
    public PageResult<ExecuteLogModel> list(ExecuteLogModel model,
                                            @RequestParam(value = "pageNum",defaultValue = "0") Integer pageNum,
                                            @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        final Page<ExecuteLogModel> page = executeLogService.page(model,pageNum,pageSize);
        return PageResult.<ExecuteLogModel>builder()
                .pageNum(pageNum).pageSize(pageSize)
                .condition(model).totalSize(page.getTotalElements())
                .list(page.getContent())
                .build();
    }

}
