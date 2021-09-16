package com.sprout.river.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sprout.core.jpa.entity.AbstractBaseEntity;
import com.sprout.system.entity.Group;
import com.sprout.system.entity.Role;
import com.sprout.system.entity.User;
import com.sprout.system.utils.Sex;
import com.sprout.system.utils.Status;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "r_project")
public class Project extends AbstractBaseEntity<Long> {

    private String name;

    private String x;

    private String y;

    private String desp;

    private String department;

    private String monitorArea;

    private String tel;

    private String landType;

    private String fixType;

    private Set<ProjectFile> projectFiles = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="project_id")
    public Set<ProjectFile> getProjectFiles() {
        return projectFiles;
    }

    public void setProjectFiles(Set<ProjectFile> projectFiles) {
        this.projectFiles = projectFiles;
    }


    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMonitorArea() {
        return monitorArea;
    }

    public void setMonitorArea(String monitorArea) {
        this.monitorArea = monitorArea;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLandType() {
        return landType;
    }

    public void setLandType(String landType) {
        this.landType = landType;
    }

    public String getFixType() {
        return fixType;
    }

    public void setFixType(String fixType) {
        this.fixType = fixType;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                ", desp='" + desp + '\'' +
                '}';
    }
}