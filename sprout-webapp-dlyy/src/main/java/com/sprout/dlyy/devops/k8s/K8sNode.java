package com.sprout.dlyy.devops.k8s;

import io.kubernetes.client.openapi.models.V1Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class K8sNode {

    private String apiVersion;

    private String host;

    private String kind;

    private String namespace;

    private String clusterName;

    private Map<String, String> labels = new HashMap<>();

    public K8sNode(V1Node v1Node) {
        this.apiVersion = v1Node.getApiVersion();
        this.host = null;
        this.kind = v1Node.getKind();
        this.namespace = v1Node.getMetadata().getNamespace();
        this.clusterName = v1Node.getMetadata().getClusterName();
        this.labels = v1Node.getMetadata().getLabels();
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public static List<K8sNode> getK8sNodeList(List<V1Node> v1NodeList) {
        List<K8sNode> k8sNodeList = new ArrayList<>();
        for (V1Node v1Node : v1NodeList) {
            k8sNodeList.add(new K8sNode(v1Node));
        }
        return k8sNodeList;
    }
}
