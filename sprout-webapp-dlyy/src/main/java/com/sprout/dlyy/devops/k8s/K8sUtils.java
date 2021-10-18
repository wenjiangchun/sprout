package com.sprout.dlyy.devops.k8s;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public final class K8sUtils {

    private static final Logger logger = LoggerFactory.getLogger(K8sUtils.class);

    private static ApiClient client;
    private static CoreV1Api api;
    static {
        File k8sConfig = null;
        try {
            k8sConfig = ResourceUtils.getFile("classpath:config");
            client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(k8sConfig))).build();
            //将加载config的client设置为默认的client
            Configuration.setDefaultApiClient(client);
            api = new CoreV1Api();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ApiClient getClient() {
        return client;
    }

    public static List<V1Pod> getPods() throws ApiException {
        //创建一个api
        //打印所有的pod
        V1PodList list = api.listPodForAllNamespaces(null,null,null,null,null,null,null,null,null,null);
        for (V1Pod item : list.getItems()) {
            System.out.println(item);
        }
        return list.getItems();
    }

    public static List<V1Node> getNodes() throws ApiException {
         V1NodeList list = api.listNode(null, null, null,null, null, null, null, null,null,null);
        for (V1Node item : list.getItems()) {
        }
         return list.getItems();
    }

    public static List<V1Pod> getPods(String ip) throws ApiException {
        V1PodList list = api.listPodForAllNamespaces(null,null,null,null,null,null,null,null,null,null);
        for (V1Pod item : list.getItems()) {
            System.out.println(item);
        }
        return list.getItems();
    }
}
