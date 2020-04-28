package com.yz.kronos.controller;

import com.yz.kronos.schedule.model.JobRelationModel;
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
    public List<JobRelationModel> list(JobRelationModel jobRelationModel){
        return jobRelationService.list(jobRelationModel);
    }

    @GetMapping(value = "/get/{id}")
    public JobRelationModel get(@PathVariable Long id){
        return jobRelationService.get(id);
    }
}
