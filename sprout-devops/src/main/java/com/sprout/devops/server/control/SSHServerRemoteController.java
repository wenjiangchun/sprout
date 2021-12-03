package com.sprout.devops.server.control;

import com.sprout.devops.server.entity.ServerHost;
import com.sprout.devops.util.SSHClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * SSH远程服务器控制器，通过调用远程服务器SSH实现对远程服务器的启动和关闭
 */
public class SSHServerRemoteController implements ServerRemoteController {

    private static final Logger logger = LoggerFactory.getLogger(SSHServerRemoteController.class);

    /**
     * 控制器操作服务器对象,该对象初始化后不可改变
     */
    private final ServerHost serverHost;

    public SSHServerRemoteController(ServerHost serverHost) {
        this.serverHost = serverHost;
    }

    @Override
    public boolean getRemoteServerState() throws Exception {
        InetAddress inetAddress = InetAddress.getByName(serverHost.getIp());
        return inetAddress.isReachable(2000);
    }

    @Override
    public void startRemoteServer() throws Exception {
        if (!getRemoteServerState()) {
            throw new Exception(String.format("ip=【{%s}】不支持ssh远程开机", this.serverHost.getIp()));
        } else {
            logger.warn("【ip={}】服务器已启动,不需要开机", this.serverHost.getIp());
        }
    }

    @Override
    public void stopRemoteServer() throws Exception {
        if (getRemoteServerState()) {
            SSHClient sshClient = new SSHClient(serverHost);
            String result = sshClient.execCmd("shutdown -h now");
            sshClient.close();
            logger.debug("ip=【{}】执行ssh远程关机成功:{}", serverHost.getIp(), result);
        } else {
            logger.warn("【ip={}】服务器未启动,不需要关机", this.serverHost.getIp());
        }
    }

    @Override
    public ServerHost getServerHost() {
        return this.serverHost;
    }
}
