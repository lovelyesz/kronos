package com.yz.kronos.controller;

import com.yz.kronos.CallResultConstant;
import com.yz.kronos.from.FlowForm;
import com.yz.kronos.model.CallResult;
import com.yz.kronos.model.CallResultBuilder;
import com.yz.kronos.model.FlowInfoModel;
import com.yz.kronos.model.PageResult;
import com.yz.kronos.service.FlowInfoService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;


/**
 * @author shanchong
 * @date 2019-11-07
 **/
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/flow")
public class FlowController {

    @Autowired
    FlowInfoService flowInfoService;
    @Autowired
    RedissonClient redissonClient;

    @RequestMapping(value = "/list",method = {RequestMethod.POST, RequestMethod.GET})
    public PageResult<FlowInfoModel> list(FlowForm form){
        final PageResult<FlowInfoModel> page = flowInfoService.page(form.getNamespaceId(),
                form.getPage()-1,form.getLimit());
        page.setCondition(form);
        return page;
    }

    @PostMapping(value = "/schedule")
    public CallResult schedule(@RequestParam(value = "flowId")Long flowId) throws InterruptedException {
        final RLock lock = redissonClient.getLock("flowInfoService:schedule:" + flowId);
        final boolean tryLock = lock.tryLock(60, TimeUnit.SECONDS);
        if (!tryLock){
            log.error("kronos flow schedule fail , get lock fail {}",flowId);
            return CallResultBuilder.fail("任务启动重复提交");
        }
        flowInfoService.schedule(flowId);
        lock.unlock();
        return CallResultBuilder.success();
    }

    @PostMapping(value = "/shutdown")
    public CallResult shutdown(@RequestParam(value = "flowId")Long flowId) throws InterruptedException {
        final RLock lock = redissonClient.getLock("flowInfoService:shutdown:" + flowId);
        final boolean tryLock = lock.tryLock(60, TimeUnit.SECONDS);
        if (!tryLock){
            log.error("kronos flow shutdown fail , get lock fail {}",flowId);
            return CallResultBuilder.fail("任务关闭重复提交");
        }
        flowInfoService.shutdown(flowId);
        lock.unlock();
        return CallResultBuilder.success();
    }

}
