package com.sprout.dlyy.devops.docker.entity;

import com.sprout.core.jpa.entity.AbstractBaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "ops_docker_host")
public class DockerHost extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private String name;

    private String ip;

    private String port;

    private String description;


    @Column(length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 20)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(length = 5)
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Column(length = 200)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DockerHost{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
