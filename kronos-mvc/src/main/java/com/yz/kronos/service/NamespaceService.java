package com.yz.kronos.service;

import com.yz.kronos.dao.NamespaceRepository;
import com.yz.kronos.enums.YesNoEnum;
import com.yz.kronos.model.NamespaceInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author shanchong
 * @date 2019-11-11
 **/
@Service
public class NamespaceService {

    @Autowired
    private NamespaceRepository namespaceRepository;

    public List<NamespaceInfoModel> list(NamespaceInfoModel model){
        model.setIsDelete(YesNoEnum.NO.code());
        final Example<NamespaceInfoModel> example = Example.of(model);
        return namespaceRepository.findAll(example);
    }


    public void save(NamespaceInfoModel model) {
        model.setCreateTime(new Date());
        model.setOptUser("shanchong");
        namespaceRepository.save(model);
    }

    public void delete(Long id) {
        final NamespaceInfoModel namespaceInfoModel = get(id);
        namespaceInfoModel.setIsDelete(YesNoEnum.YES.code());
        namespaceRepository.save(namespaceInfoModel);
    }

    public NamespaceInfoModel get(Long id) {
        return namespaceRepository.findById(id).get();
    }
}
