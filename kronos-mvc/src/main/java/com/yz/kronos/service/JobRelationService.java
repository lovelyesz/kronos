package com.yz.kronos.service;

import com.yz.kronos.dao.JobRelationRepository;
import com.yz.kronos.enums.YesNoEnum;
import com.yz.kronos.model.JobRelationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shanchong
 * @date 2019-11-22
 **/
@Service
public class JobRelationService {

    @Autowired
    JobRelationRepository jobRelationRepository;

    public List<JobRelationModel> list(JobRelationModel jobRelationModel){
        jobRelationModel.setIsDelete(YesNoEnum.NO.code());
        return jobRelationRepository.findAll(Example.of(jobRelationModel));
    }

    public void save(JobRelationModel jobRelationModel){
        jobRelationRepository.save(jobRelationModel);
    }

    public void delete(Long id){
        final JobRelationModel jobRelationModel = get(id);
        jobRelationModel.setIsDelete(YesNoEnum.YES.code());
        jobRelationRepository.save(jobRelationModel);
    }

    public JobRelationModel get(Long id){
        return jobRelationRepository.findById(id).get();
    }


    public void updateShareTotalByJobIdAndFlowId(Integer shareTotal, Long jobId,Long flowId) {
        jobRelationRepository.updateShareTotalByJobIdAndFlowId(shareTotal,jobId,flowId);
    }
}
