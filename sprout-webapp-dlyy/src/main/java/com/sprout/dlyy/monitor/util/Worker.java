package com.sprout.dlyy.monitor.util;

import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

public class Worker implements Serializable {

    private String id;
    private String phone;
    private String email;
    private String name;
    private String eName;
    private String postNo;

    @Field("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Field("cell_phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Field("mailbox")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Field("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Field("name_pinyi")
    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    @Field("postNo")
    public String getPostNo() {
        return postNo;
    }

    public void setPostNo(String postNo) {
        this.postNo = postNo;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", eName='" + eName + '\'' +
                ", postNo='" + postNo + '\'' +
                '}';
    }
}
