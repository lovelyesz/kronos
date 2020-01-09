package com.yz.kronos.schedule.handle;

import com.yz.kronos.ExecuteConstant;
import com.yz.kronos.JobInfo;
import com.yz.kronos.KubernetesConfig;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.batch.JobBuilder;
import io.fabric8.kubernetes.api.model.batch.JobSpec;
import io.fabric8.kubernetes.api.model.batch.JobSpecBuilder;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author shanchong
 * @date 2019-12-20
 **/
@Slf4j
public abstract class AbstractStartJobHandle implements JobHandle {

    /**
     * 停止任务
     */
    @Override
    public void stopJob() {

    }

    /**
     * 标签
     * @param execId
     * @return
     */
    protected abstract Map<String,String> labels(String execId);

    /**
     * 资源
     * @return
     */
    protected abstract Map<String, Quantity> requests();

    /**
     * 环境变量
     * @param execId
     * @return
     */
    protected abstract List<EnvVar> env(String execId);

    /**
     * 存储卷
     * @param execId
     * @return
     */
    protected abstract Volume volumes(String execId);

    /**
     * k8s配置
     * @return
     */
    protected abstract KubernetesConfig kubernetesConfig();

    /**
     * 任务信息
     * @return
     */
    protected abstract JobInfo jobInfo();

    /**
     * 开始任务
     */
    @Override
    public void startJob(String execId) {
        final KubernetesConfig kubernetesConfig = kubernetesConfig();
        final JobInfo jobInfo = jobInfo();
        io.fabric8.kubernetes.client.Config config = new ConfigBuilder().withMasterUrl(kubernetesConfig.getServiceApi()).build();
        DefaultKubernetesClient client = new DefaultKubernetesClient(config);
        String namespace = jobInfo.getNamespace().getName();

        ObjectMeta objectMeta = new ObjectMetaBuilder()
                .withNamespace(namespace)
                .withLabels(labels(execId))
                .withName("kronos-job-" + execId)
                .build();

        Container container = new ContainerBuilder()
                .withSecurityContext(new SecurityContextBuilder()
//                        .withPrivileged(true)
                        .withRunAsNonRoot(false)
                        .withAllowPrivilegeEscalation(true)
                        .build())
                .withName("kronos-container-"+ execId)
                .withResources(new ResourceRequirementsBuilder()
                        .withRequests(requests()).build())
                .withImage(jobInfo.getNamespace().getImage())
                .withEnv(env(execId))
                .withArgs(jobInfo.getNamespace().getCmd())
                .withImagePullPolicy(kubernetesConfig.getImagePullPolicy())
                .withVolumeMounts(new VolumeMountBuilder()
                        .withName(ExecuteConstant.KRONOS_VOLUME_NAME).withMountPath(kubernetesConfig.getLogPath()+"/"+namespace)
                        .build())
                .build();
        JobSpec jobSpec = new JobSpecBuilder()
                .withCompletions(jobInfo.getShareTotal())
                .withParallelism(jobInfo.getShareTotal())
                .withTemplate(new PodTemplateSpecBuilder()
                        .withSpec(new PodSpecBuilder()
                                .withContainers(container)
                                .withVolumes(volumes(execId))
                                .withRestartPolicy(kubernetesConfig.getRestartPolicy())
                                .build())
                        .withMetadata(objectMeta)
                        .build())
                .build();
        Job job = new JobBuilder()
                .withKind("Job")
                .withApiVersion(kubernetesConfig.getGroupName()+"/"+kubernetesConfig.getApiVersion())
                .withMetadata(objectMeta)
                .withSpec(jobSpec)
                .build();
        log.info("request kubernetes api : {}",job);
        client.batch().jobs().create(job);
    }
}
