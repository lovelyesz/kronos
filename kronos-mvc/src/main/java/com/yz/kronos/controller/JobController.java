package com.yz.kronos.controller;

import com.yz.kronos.CallResultConstant;
import com.yz.kronos.model.CallResult;
import com.yz.kronos.model.JobInfoModel;
import com.yz.kronos.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
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
                .build();
    }

    @GetMapping(value = "/get/{id}")
    public JobInfoModel get(@PathVariable(value = "id")Long id){
        final JobInfoModel jobInfoModel = jobInfoService.get(id);
        return jobInfoModel;
    }


}
