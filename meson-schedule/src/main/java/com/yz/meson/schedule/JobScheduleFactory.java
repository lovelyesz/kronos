package com.yz.meson.schedule;

import com.yz.meson.ExecuteConstant;
import com.yz.meson.schedule.config.KubernetesConfig;
import com.yz.meson.schedule.config.MesonJobExecutor;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.batch.*;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.dsl.FilterWatchListDeletable;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shanchong
 * @date 2019-11-12
 **/
@Slf4j
public class JobScheduleFactory implements ScheduleFactory {

    KubernetesConfig kubernetesConfig;
    MesonJobExecutor mesonJobExecutor;

    public JobScheduleFactory(KubernetesConfig kubernetesConfig,
                              MesonJobExecutor mesonJobExecutor){
        this.kubernetesConfig = kubernetesConfig;
        this.mesonJobExecutor = mesonJobExecutor;
    }

    @Override
    public void execute(){
        build();
    }

    /**
     * 构建kubernetes JOB
     */
    private void build(){
        Config config = new ConfigBuilder().withMasterUrl(kubernetesConfig.getServiceApi()).build();
        DefaultKubernetesClient client = new DefaultKubernetesClient(config);
        Long execLogId = mesonJobExecutor.getExecLogId();
        Long jobId = mesonJobExecutor.getJobId();
        Long flowId = mesonJobExecutor.getFlowId();
        String execId = mesonJobExecutor.getExecId();
        Integer shareTotal = mesonJobExecutor.getShareCount();
        String namespace = mesonJobExecutor.getNamespace();
        Map<String, String> labels = mesonJobExecutor.getLabels();
        String jobName = "meson-job-" + execId;
        labels.put(ExecuteConstant.MESON_EXECUTE_ID, execId);
        ObjectMeta objectMeta = new ObjectMetaBuilder()
                .withNamespace(namespace)
                .withLabels(labels)
                .withName(jobName)
                .build();
        Map<String, Quantity> requests = mesonJobExecutor.getResources().entrySet().parallelStream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new QuantityBuilder()
                        .withAmount(e.getValue()).build()));
        Map<String, String> env = new HashMap<>(4);
        env.put(ExecuteConstant.MESON_EXECUTOR_ENV_NAME,ExecuteConstant.MESON_EXECUTOR_QUEUE_NAME_PRE + mesonJobExecutor.getExecId());
        List<EnvVar> envVarList = env.entrySet().parallelStream().map(e ->
                new EnvVarBuilder().withName(e.getKey())
                        .withValue(e.getValue()).build()).collect(Collectors.toList());
        Container container = new ContainerBuilder()
                .withSecurityContext(new SecurityContextBuilder()
//                        .withPrivileged(true)
                        .withRunAsNonRoot(false)
                        .withAllowPrivilegeEscalation(true)
                        .build())
                .withName("meson-container-"+ execId)
                .withResources(new ResourceRequirementsBuilder()
                        .withRequests(requests).build())
                .withImage(mesonJobExecutor.getImage())
                .withEnv(envVarList)
                .withArgs(mesonJobExecutor.getCmd())
                .withImagePullPolicy(kubernetesConfig.getImagePullPolicy())
                .withVolumeMounts(new VolumeMountBuilder()
                        .withName(ExecuteConstant.MESON_VOLUME_NAME).withMountPath(kubernetesConfig.getLogPath())
                        .build())
                .build();
        final String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
        JobSpec jobSpec = new JobSpecBuilder()
                .withCompletions(shareTotal)
                .withParallelism(shareTotal)
                .withTemplate(new PodTemplateSpecBuilder()
                        .withSpec(new PodSpecBuilder()
                                .withContainers(container)
                                .withVolumes(new VolumeBuilder()
                                        .withName(ExecuteConstant.MESON_VOLUME_NAME)
                                        .withHostPath(new HostPathVolumeSourceBuilder()
                                                .withPath(kubernetesConfig.getLogPath()+today+"/"+execId)
                                                .build())
                                        .build())
                                .withRestartPolicy(kubernetesConfig.getRestartPolicy())
                                .build())
                        .withMetadata(objectMeta)
                        .build())
                .build();
        Job job = new JobBuilder()
                .withKind("Job")
                .withApiVersion("batch/v1")
                .withMetadata(objectMeta)
                .withSpec(jobSpec)
                .build();
        client.batch().jobs().create(job);

    }

    @Override
    public List<Job> shutdown() {
        String execId = mesonJobExecutor.getExecId();
        Config config = new ConfigBuilder().withMasterUrl(kubernetesConfig.getServiceApi()).build();
        KubernetesClient client = new DefaultKubernetesClient(config);
        log.info("delete jobs which labels in {}={}",ExecuteConstant.MESON_EXECUTE_ID,execId);
        FilterWatchListDeletable<Job, JobList, Boolean, Watch, Watcher<Job>> watchListDeletable = client.batch().jobs()
                .withLabel(ExecuteConstant.MESON_EXECUTE_ID, execId);
        List<Job> jobList = watchListDeletable.list().getItems();
        if (jobList.isEmpty()){
            return jobList;
        }
//        client.pods().withLabel(ExecuteConstant.MESON_EXECUTE_ID, execId).delete();
//        watchListDeletable.delete();
        return jobList;
    }

}
