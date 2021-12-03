package com.sprout.devops.server.control;

import com.sprout.devops.server.entity.ServerHost;

public class ServerRemoteControlContext {

    public static ServerRemoteController getServerController(ServerHost serverHost) {
        switch (serverHost.getControlType()) {
            case SSH:
                return new SSHServerRemoteController(serverHost);
            case IPMI:
            default:
                return new IPMIServerRemoteController(serverHost);
        }
    }
}
