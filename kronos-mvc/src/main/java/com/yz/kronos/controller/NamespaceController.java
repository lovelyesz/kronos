package com.yz.kronos.controller;

import com.yz.kronos.schedule.model.CallResult;
import com.yz.kronos.schedule.model.CallResultBuilder;
import com.yz.kronos.schedule.model.NamespaceInfoModel;
import com.yz.kronos.service.NamespaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shanchong
 * @date 2019-11-11
 **/
@RestController
@RequestMapping(value = "/namespace")
public class NamespaceController {

    @Autowired
    private NamespaceService namespaceService;

    @GetMapping(value = "/list")
    public List<NamespaceInfoModel> list(NamespaceInfoModel namespaceInfoModel){
        return namespaceService.list(namespaceInfoModel);
    }

    @PostMapping(value = "/save")
    public CallResult save(NamespaceInfoModel model){
        namespaceService.save(model);
        return CallResultBuilder.success();
    }

    @GetMapping(value = "/get/{id}")
    public NamespaceInfoModel get(@PathVariable Long id){
        return namespaceService.get(id);
    }

    @DeleteMapping(value = "/delete/{id}")
    public CallResult delete(@PathVariable Long id){
        namespaceService.delete(id);
        return CallResultBuilder.success();
    }

}
