package com.sprout.dlyy.devops.host.entity;

import com.sprout.common.os.SystemUtils;
import com.sprout.core.jpa.entity.AbstractBaseEntity;
import com.sprout.dlyy.devops.util.ControlType;
import com.sprout.dlyy.devops.util.OSType;

import javax.persistence.*;
import java.io.IOException;
import java.net.InetAddress;

@Entity
@Table(name = "ops_server_host")
public class ServerHost extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private String name;

    private String ip;

    private String port;

    private String description;

    private OSType os;

    private String userName;

    private String password;

    private ControlType controlType = ControlType.IPMI;

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

    @Enumerated
    @Column(length = 20)
    public OSType getOs() {
        return os;
    }

    public void setOs(OSType os) {
        this.os = os;
    }

    @Column(length = 20)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(length = 20)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   /* @Transient
    public boolean getServerState() {
        try {
            InetAddress inetAddress =  InetAddress.getByName(this.ip);
            return inetAddress.isReachable(3000);
        } catch (IOException e) {
            return false;
        }
    }*/

    @Enumerated
    @Column(length = 10)
    public ControlType getControlType() {
        return controlType;
    }

    public void setControlType(ControlType controlType) {
        this.controlType = controlType;
    }

    @Override
    public String toString() {
        return "ServerHost{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", description='" + description + '\'' +
                ", os='" + os + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
