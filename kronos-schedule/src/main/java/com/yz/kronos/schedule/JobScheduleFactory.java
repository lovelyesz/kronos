package com.yz.kronos.schedule;

import com.yz.kronos.ExecuteConstant;
import com.yz.kronos.schedule.config.KubernetesConfig;
import com.yz.kronos.schedule.config.JobExecutor;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.batch.*;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.dsl.FilterWatchListDeletable;
import lombok.extern.slf4j.Slf4j;

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
    JobExecutor jobExecutor;

    public JobScheduleFactory(KubernetesConfig kubernetesConfig,
                              JobExecutor jobExecutor){
        this.kubernetesConfig = kubernetesConfig;
        this.jobExecutor = jobExecutor;
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
        Long execLogId = jobExecutor.getExecLogId();
        Long jobId = jobExecutor.getJobId();
        Long flowId = jobExecutor.getFlowId();
        String execId = jobExecutor.getExecId();
        Integer shareTotal = jobExecutor.getShareCount();
        String namespace = jobExecutor.getNamespace();
        Map<String, String> labels = jobExecutor.getLabels();
        String jobName = "kronos-job-" + execId;
        labels.put(ExecuteConstant.KRONOS_EXECUTE_ID, execId);
        ObjectMeta objectMeta = new ObjectMetaBuilder()
                .withNamespace(namespace)
                .withLabels(labels)
                .withName(jobName)
                .build();
        Map<String, Quantity> requests = jobExecutor.getResources().entrySet().parallelStream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new QuantityBuilder()
                        .withAmount(e.getValue()).build()));
        Map<String, String> env = new HashMap<>(4);
        env.put(ExecuteConstant.KRONOS_EXECUTOR_ENV_NAME,ExecuteConstant.KRONOS_EXECUTOR_QUEUE_NAME_PRE + jobExecutor.getExecId());
        List<EnvVar> envVarList = env.entrySet().parallelStream().map(e ->
                new EnvVarBuilder().withName(e.getKey())
                        .withValue(e.getValue()).build()).collect(Collectors.toList());
        Container container = new ContainerBuilder()
                .withSecurityContext(new SecurityContextBuilder()
//                        .withPrivileged(true)
                        .withRunAsNonRoot(false)
                        .withAllowPrivilegeEscalation(true)
                        .build())
                .withName("kronos-container-"+ execId)
                .withResources(new ResourceRequirementsBuilder()
                        .withRequests(requests).build())
                .withImage(jobExecutor.getImage())
                .withEnv(envVarList)
                .withArgs(jobExecutor.getCmd())
                .withImagePullPolicy(kubernetesConfig.getImagePullPolicy())
                .withVolumeMounts(new VolumeMountBuilder()
                        .withName(ExecuteConstant.KRONOS_VOLUME_NAME).withMountPath(kubernetesConfig.getLogPath())
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
                                        .withName(ExecuteConstant.KRONOS_VOLUME_NAME)
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
        String execId = jobExecutor.getExecId();
        Config config = new ConfigBuilder().withMasterUrl(kubernetesConfig.getServiceApi()).build();
        KubernetesClient client = new DefaultKubernetesClient(config);
        log.info("delete jobs which labels in {}={}",ExecuteConstant.KRONOS_EXECUTE_ID,execId);
        FilterWatchListDeletable<Job, JobList, Boolean, Watch, Watcher<Job>> watchListDeletable = client.batch().jobs()
                .withLabel(ExecuteConstant.KRONOS_EXECUTE_ID, execId);
        List<Job> jobList = watchListDeletable.list().getItems();
        if (jobList.isEmpty()){
            return jobList;
        }
//        client.pods().withLabel(ExecuteConstant.KRONOS_EXECUTE_ID, execId).delete();
//        watchListDeletable.delete();
        return jobList;
    }

}
