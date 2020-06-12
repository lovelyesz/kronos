package com.yz.kronos.controller;

import com.yz.kronos.schedule.model.JobFlowModel;
import com.yz.kronos.service.JobRelationService;
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
    public List<JobFlowModel> list(JobFlowModel jobFlowModel){
        return jobRelationService.list(jobFlowModel);
    }

    @GetMapping(value = "/get/{id}")
    public JobFlowModel get(@PathVariable Long id){
        return jobRelationService.get(id);
    }
}
