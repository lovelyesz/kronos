package com.yz.meson.controller;

import com.yz.meson.CallResultConstant;
import com.yz.meson.model.CallResult;
import com.yz.meson.model.JobInfoModel;
import com.yz.meson.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shanchong
 * @date 2019-10-30
 **/

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    JobInfoService jobInfoService;

    @PostMapping(value = "/save")
    public CallResult save(JobInfoModel jobInfoModel){
        final JobInfoModel model = jobInfoService.save(jobInfoModel);
        return CallResult.builder()
                .code(CallResultConstant.SUCCESS_CODE)
                .message(CallResultConstant.SUCCESS_MESSAGE)
                .data(model)
                .build();
    }

    @GetMapping(value = "/list")
    public List<JobInfoModel> list(JobInfoModel jobInfoModel){
        List<JobInfoModel> list = jobInfoService.list(jobInfoModel);
        return list;
    }

    @DeleteMapping(value = "/delete/{id}")
    public CallResult delete(@PathVariable(value = "id") Long id){
        jobInfoService.delete(id);
        return CallResult.builder()
                .code(CallResultConstant.SUCCESS_CODE)
                .message(CallResultConstant.SUCCESS_MESSAGE)
                .build();
    }

    @GetMapping(value = "/get/{id}")
    public JobInfoModel get(@PathVariable(value = "id")Long id){
        final JobInfoModel jobInfoModel = jobInfoService.get(id);
        return jobInfoModel;
    }


}
