package com.yz.kronos.schedule.job;

import com.alibaba.fastjson.JSONObject;
import com.yz.kronos.JobInfo;
import com.yz.kronos.KubernetesConfig;
import com.yz.kronos.model.JobInfoModel;
import com.yz.kronos.model.NamespaceInfoModel;
import com.yz.kronos.service.JobInfoService;
import com.yz.kronos.service.NamespaceService;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultJobScheduleTest {

    @Autowired
    private JobSchedule jobSchedule;
    @Autowired
    private KubernetesConfig kubernetesConfig;
    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private NamespaceService namespaceService;

    @Test
    public void test(){
        final String batchNo = UUID.randomUUID().toString().replaceAll("-", "");
        final NamespaceInfoModel namespaceInfoModel = namespaceService.get(14L);
        final JobInfo.NamespaceInfo namespaceInfo = new JobInfo.NamespaceInfo(namespaceInfoModel);
        final JobInfoModel jobInfoModel = jobInfoService.get(12L);
        final JobInfo jobInfo = new JobInfo();
        jobInfo.setBatchNo(batchNo);
        jobInfo.setClazz(jobInfoModel.getClazz());
        jobInfo.setNamespace(namespaceInfo);
        jobInfo.setResources(JSONObject.parseObject(jobInfoModel.getResources()));
        jobInfo.setShareTotal(1);
        jobInfo.setJobId(jobInfoModel.getId());
        jobSchedule.schedule(0L,jobInfo,kubernetesConfig);
    }


}
