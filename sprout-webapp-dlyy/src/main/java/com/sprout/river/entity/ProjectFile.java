package com.sprout.river.entity;

import com.sprout.core.jpa.entity.AbstractBaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "r_project_file")
public class ProjectFile extends AbstractBaseEntity<Long> {

    private String name;

    private String path;

    private String type;

    private Long size;

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ProjectFile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                '}';
    }
}