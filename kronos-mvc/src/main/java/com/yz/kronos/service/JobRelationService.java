package com.yz.kronos.service;

import com.yz.kronos.dao.JobRelationRepository;
import com.yz.kronos.enums.YesNoEnum;
import com.yz.kronos.schedule.model.JobFlowModel;
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

    public List<JobFlowModel> list(JobFlowModel jobFlowModel){
        jobFlowModel.setIsDelete(YesNoEnum.NO.code());
        return jobRelationRepository.findAll(Example.of(jobFlowModel));
    }

    public void save(JobFlowModel jobFlowModel){
        jobRelationRepository.save(jobFlowModel);
    }

    public void delete(Long id){
        final JobFlowModel jobFlowModel = get(id);
        jobFlowModel.setIsDelete(YesNoEnum.YES.code());
        jobRelationRepository.save(jobFlowModel);
    }

    public JobFlowModel get(Long id){
        return jobRelationRepository.findById(id).get();
    }


    public void updateShareTotalByJobIdAndFlowId(Integer shareTotal, Long jobId,Long flowId) {
        jobRelationRepository.updateShareTotalByJobIdAndFlowId(shareTotal,jobId,flowId);
    }
}
