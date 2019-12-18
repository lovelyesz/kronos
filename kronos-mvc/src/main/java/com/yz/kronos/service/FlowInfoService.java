package com.yz.kronos.service;

import com.yz.kronos.dao.FlowInfoRepository;
import com.yz.kronos.enums.YesNoEnum;
import com.yz.kronos.model.FlowInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shanchong
 * @date 2019-11-07
 **/
@Service
public class FlowInfoService {

    @Autowired
    FlowInfoRepository flowInfoRepository;

    public FlowInfoModel get(Long id){
        return flowInfoRepository.findById(id).get();
    }

    public FlowInfoModel save(FlowInfoModel flowInfoModel){
        return flowInfoRepository.save(flowInfoModel);
    }

    public void delete(Long id){
        final FlowInfoModel flowInfoModel = flowInfoRepository.findById(id).get();
        flowInfoModel.setIsDelete(YesNoEnum.YES.code());
        flowInfoRepository.save(flowInfoModel);
    }

    public List<FlowInfoModel> list(FlowInfoModel flowInfoModel){
        flowInfoModel.setIsDelete(YesNoEnum.NO.code());
        return flowInfoRepository.findAll(Example.of(flowInfoModel));
    }

}
