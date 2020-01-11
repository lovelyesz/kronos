package com.yz.kronos.schedule.listener;

import com.yz.kronos.KubernetesConfig;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.batch.JobList;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import io.fabric8.kubernetes.client.informers.SharedInformerFactory;
import io.fabric8.kubernetes.client.utils.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * kubernetes job listener
 * @author shanchong
 * @date 2019-11-16
 **/
@Slf4j
public class JobProcessListener {

    KubernetesConfig kubernetesConfig;
    EventHandlerManage eventHandlerManage;

    public JobProcessListener(KubernetesConfig kubernetesConfig,
                              EventHandlerManage eventHandlerManage) {
        this.kubernetesConfig = kubernetesConfig;
        this.eventHandlerManage = eventHandlerManage;
    }


    /**
     * 启动后开启一个守护进程触发
     */
    public void run() {
        for (String namespace : eventHandlerManage.namespaceList()){
            Config config = new ConfigBuilder().withMasterUrl(kubernetesConfig.getServiceApi()).build();
            DefaultKubernetesClient client = new DefaultKubernetesClient(config);
            SharedInformerFactory sharedInformerFactory = client.inNamespace(namespace).informers();
            CustomResourceDefinitionContext customResourceDefinitionContext = new CustomResourceDefinitionContext.Builder()
                    .withGroup(kubernetesConfig.getGroupName()).withPlural(Utils.getPluralFromKind(Job.class.getSimpleName()))
                    .withVersion(kubernetesConfig.getApiVersion())
                    .build();
            SharedIndexInformer<Job> sharedIndexInformer = sharedInformerFactory
                    .sharedIndexInformerForCustomResource(customResourceDefinitionContext,Job.class, JobList.class,
                            kubernetesConfig.getResyncPeriodInMillis());
            sharedIndexInformer.run();
            for (ResourceEventHandler reh : eventHandlerManage.resourceEventHandlerList()) {
                sharedIndexInformer.addEventHandler(reh);
            }
        }
    }

}
