package com.yz.meson.controller;

import com.yz.meson.CallResultConstant;
import com.yz.meson.model.CallResult;
import com.yz.meson.model.JobRelationModel;
import com.yz.meson.service.JobRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shanchong
 * @date 2019-11-22
 **/
@RestController
@RequestMapping(value = "/relation")
public class JobRelationController {

    @Autowired
    JobRelationService jobRelationService;

    @GetMapping(value = "/list")
    public List<JobRelationModel> list(JobRelationModel jobRelationModel){
        return jobRelationService.list(jobRelationModel);
    }

    @PostMapping(value = "/save")
    public CallResult save(JobRelationModel jobRelationModel){
        jobRelationService.save(jobRelationModel);
        return CallResult.builder()
                .code(CallResultConstant.SUCCESS_CODE)
                .message(CallResultConstant.SUCCESS_MESSAGE)
                .build();
    }

    @DeleteMapping(value = "/delete/{id}")
    public CallResult delete(@PathVariable Long id){
        jobRelationService.delete(id);
        return CallResult.builder()
                .code(CallResultConstant.SUCCESS_CODE)
                .message(CallResultConstant.SUCCESS_MESSAGE)
                .build();
    }

    @GetMapping(value = "/get/{id}")
    public JobRelationModel get(@PathVariable Long id){
        return jobRelationService.get(id);
    }
}
