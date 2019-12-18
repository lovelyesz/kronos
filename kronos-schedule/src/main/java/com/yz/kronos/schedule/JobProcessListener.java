package com.yz.kronos.schedule;

import com.yz.kronos.ExecuteConstant;
import com.yz.kronos.schedule.config.KubernetesConfig;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.batch.JobList;
import io.fabric8.kubernetes.api.model.batch.JobStatus;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import io.fabric8.kubernetes.client.informers.SharedInformerFactory;
import io.fabric8.kubernetes.client.utils.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * kubernetes job listener
 * @author shanchong
 * @date 2019-11-16
 **/
@Slf4j
public class JobProcessListener {

    KubernetesConfig kubernetesConfig;
    ScheduleSynchronizer scheduleSynchronizer;

    public JobProcessListener(KubernetesConfig kubernetesConfig,
                              ScheduleSynchronizer scheduleSynchronizer){
        this.kubernetesConfig = kubernetesConfig;
        this.scheduleSynchronizer = scheduleSynchronizer;
    }

    /**
     */
    public SharedIndexInformer<Job> run(){
        Config config = new ConfigBuilder().withMasterUrl(kubernetesConfig.getServiceApi()).build();
        DefaultKubernetesClient client = new DefaultKubernetesClient(config);
        SharedInformerFactory sharedInformerFactory = client.informers();
        CustomResourceDefinitionContext customResourceDefinitionContext = new CustomResourceDefinitionContext.Builder()
                .withGroup("batch").withPlural(Utils.getPluralFromKind(Job.class.getSimpleName()))
                .withVersion("v1")
                .build();
        SharedIndexInformer<Job> sharedIndexInformer = sharedInformerFactory
                .sharedIndexInformerForCustomResource(customResourceDefinitionContext,Job.class, JobList.class,
                        60*1000);
        sharedIndexInformer.addEventHandler(new ResourceEventHandler<Job>(){

            @Override
            public void onAdd(Job obj) {
                log.info("launch listener to {}",obj.getMetadata().getName());
            }

            @Override
            public void onUpdate(Job oldObj, Job newObj) {
                JobStatus status = newObj.getStatus();
                Integer succeeded = status.getSucceeded();
                Integer active = status.getActive();
                Integer failed = status.getFailed();
                Integer completions = newObj.getSpec().getCompletions();
                if (succeeded==null){
                    return;
                }
                log.info("callback job {} process is ({}/{}/{}/{})",newObj.getMetadata().getName(),
                        active,succeeded,failed,completions);
                if (succeeded.equals(completions)){
                    Map<String, String> labels = newObj.getMetadata().getLabels();
                    log.info("kubernetes callback success {}",newObj);
                    String key = labels.get(ExecuteConstant.KRONOS_EXECUTE_SYNCHRONIZER_LABEL_NAME);
                    if (key!=null){
                        scheduleSynchronizer.countDown(key);
                    }
                }
            }

            @Override
            public void onDelete(Job obj, boolean deletedFinalStateUnknown) {
                //存活的pod数量
                Integer active = obj.getStatus().getActive();
                Integer succeeded = obj.getStatus().getSucceeded();
                Integer failed = obj.getStatus().getFailed();
                log.info("callback pod {} is closed ({}/{}/{})",obj.getMetadata().getName(),active,succeeded,failed);
                Map<String, String> labels = obj.getMetadata().getLabels();
                String key = labels.get(ExecuteConstant.KRONOS_EXECUTE_SYNCHRONIZER_LABEL_NAME);
                if (key!=null){
                    for (int i = 0; i < active; i++) {
                        scheduleSynchronizer.countDown(key);
                    }
                }
            }
        });
        sharedIndexInformer.run();
        return sharedIndexInformer;
    }

}
