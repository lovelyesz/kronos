package com.yz.kronos.schedule.handle;

import com.google.common.collect.Maps;
import com.yz.kronos.ExecuteConstant;
import com.yz.kronos.model.KubernetesConfig;
import com.yz.kronos.schedule.job.JobInfo;
import io.fabric8.kubernetes.api.model.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shanchong
 * @date 2019-12-20
 **/
public class StartJobHandle extends AbstractStartJobHandle {

    KubernetesConfig kubernetesConfig;
    JobInfo jobInfo;

    public StartJobHandle(KubernetesConfig kubernetesConfig, JobInfo jobInfo) {
        this.kubernetesConfig = kubernetesConfig;
        this.jobInfo = jobInfo;
    }

    @Override
    public Map<String, String> labels(String execId) {
        Map<String, String> labels = Maps.newHashMap();
        labels.put(ExecuteConstant.KRONOS_EXECUTE_ID, execId);
        labels.put(ExecuteConstant.KRONOS_EXECUTE_SYNCHRONIZER_LABEL_NAME, jobInfo.getSynchronizerKey());
        return labels;
    }

    @Override
    protected Map<String, Quantity> requests() {
        return jobInfo.getResources().entrySet().parallelStream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new QuantityBuilder()
                        .withAmount(e.getValue().toString()).build()));
    }

    @Override
    protected List<EnvVar> env(String execId) {
        Map<String, String> env = new HashMap<>(4);
        env.put(ExecuteConstant.KRONOS_EXECUTOR_ENV_NAME,ExecuteConstant.KRONOS_EXECUTOR_QUEUE_NAME_PRE + execId);
        return env.entrySet().parallelStream().map(e ->
                new EnvVarBuilder().withName(e.getKey())
                        .withValue(e.getValue()).build()).collect(Collectors.toList());
    }

    @Override
    protected Volume volumes(String execId){
        final String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
        return new VolumeBuilder()
                .withName(ExecuteConstant.KRONOS_VOLUME_NAME)
                .withHostPath(new HostPathVolumeSourceBuilder()
                        .withPath(kubernetesConfig.getLogPath()+today+"/"+execId)
                        .build())
                .build();
    }

    @Override
    protected KubernetesConfig kubernetesConfig() {
        return this.kubernetesConfig;
    }

    @Override
    protected JobInfo jobInfo() {
        return this.jobInfo;
    }


}
