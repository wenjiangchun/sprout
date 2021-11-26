package com.sprout.dlyy.devops.util;

import com.jcraft.jsch.*;
import com.sprout.dlyy.devops.host.entity.ServerHost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SSHClient {

    private final ServerHost serverHost;

    private Session session;

    public SSHClient(ServerHost serverHost) throws JSchException {
        this.serverHost = serverHost;
        connect();
    }
    public void connect() throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(serverHost.getUserName(), serverHost.getIp(), Integer.parseInt(serverHost.getPort()));
        session.setPassword(serverHost.getPassword());
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
    }

    public String execCmd(String cmd) {
        BufferedReader reader = null;
        Channel channel = null;
        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(cmd);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);
            channel.connect();
            InputStream in = channel.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String buf = "";
            StringBuilder cmdResult = new StringBuilder();
            while ((buf = reader.readLine()) != null) {
                cmdResult.append(buf);
                cmdResult.append("<br>");

            }
            return cmdResult.toString();
        } catch (IOException | JSchException e) {
            //LOGGER.info(e.getMessage());
            return "";
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                //LOGGER.info(e.getMessage());
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
    }

    public void close() {
        if (session.isConnected()) {
            session.disconnect();
        }
    }
}
