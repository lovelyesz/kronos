package com.yz.kronos.service;

import com.yz.kronos.dao.JobInfoRepository;
import com.yz.kronos.enums.YesNoEnum;
import com.yz.kronos.model.JobInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shanchong
 * @date 2019-11-07
 **/
@Service
public class JobInfoService {

    @Autowired
    JobInfoRepository jobInfoRepository;

    public JobInfoModel save(JobInfoModel jobInfoModel){
        return jobInfoRepository.save(jobInfoModel);
    }

    public List<JobInfoModel> list(JobInfoModel jobInfoModel){
        jobInfoModel.setIsDelete(YesNoEnum.NO.code());
        return jobInfoRepository.findAll(Example.of(jobInfoModel));
    }

    public void delete(Long id){
        final JobInfoModel jobInfoModel = get(id);
        jobInfoModel.setIsDelete(YesNoEnum.YES.code());
        jobInfoRepository.save(jobInfoModel);
    }

    public JobInfoModel get(Long id){
        return jobInfoRepository.findById(id).get();
    }

}
