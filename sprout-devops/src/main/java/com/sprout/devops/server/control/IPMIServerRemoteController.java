package com.sprout.devops.server.control;

import com.sprout.common.os.SystemUtils;
import com.sprout.devops.server.entity.ServerHost;
import com.sprout.devops.util.ControlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ipmi远程服务器控制器，通过调用本机绑定ipmitool实现对远程服务器的启动和关闭
 */
public class IPMIServerRemoteController implements ServerRemoteController {

    private static final Logger logger = LoggerFactory.getLogger(IPMIServerRemoteController.class);

    /**
     * IPMI命令模板
     */
    private static final String cmdTemplate = "cmd /c ipmitool -H %s -I lanplus -U %s -P %s power %s";

    /**
     * 控制器操作服务器对象,该对象初始化后不可改变
     */
    private final ServerHost serverHost;

    public IPMIServerRemoteController(ServerHost serverHost) {
        this.serverHost = serverHost;
    }

    @Override
    public boolean getRemoteServerState() throws Exception {
        String cmdKey = "status";
        if (this.serverHost.getControlType() != ControlType.IPMI) {
            throw new Exception("不支持非IPMI类型服务器");
        }
        String cmd = String.format(cmdTemplate, serverHost.getIp(), serverHost.getUserName(), serverHost.getPassword(), cmdKey);
        String result = SystemUtils.execCommand(cmd);
        return result.toLowerCase().contains("on");
    }

    @Override
    public void startRemoteServer() throws Exception {
        String cmdKey = "on";
        if (!getRemoteServerState()) {
            String cmd = String.format(cmdTemplate, serverHost.getIp(), serverHost.getUserName(), serverHost.getPassword(), cmdKey);
            String result = SystemUtils.execCommand(cmd);
            if (!result.toLowerCase().contains(cmdKey)) {
                throw new Exception("ip=" + serverHost.getIp() + "开机失败:" + result);
            }
        } else {
            logger.warn("【ip={}】服务器已启动,不需要开机", this.serverHost.getIp());
        }
    }

    @Override
    public void stopRemoteServer() throws Exception {
        String cmdKey = "off";
        if (!getRemoteServerState()) {
            String cmd = String.format(cmdTemplate, serverHost.getIp(), serverHost.getUserName(), serverHost.getPassword(), cmdKey);
            String result = SystemUtils.execCommand(cmd);
            if (!result.toLowerCase().contains(cmdKey)) {
                throw new Exception("ip=" + serverHost.getIp() + "关机失败:" + result);
            }
        } else {
            logger.warn("【ip={}】服务器未启动,不需要关机", this.serverHost.getIp());
        }
    }

    @Override
    public ServerHost getServerHost() {
        return serverHost;
    }

}
