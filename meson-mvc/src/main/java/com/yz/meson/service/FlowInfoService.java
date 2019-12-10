package com.yz.meson.service;

import com.yz.meson.dao.FlowInfoRepository;
import com.yz.meson.enums.YesNoEnum;
import com.yz.meson.model.FlowInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
