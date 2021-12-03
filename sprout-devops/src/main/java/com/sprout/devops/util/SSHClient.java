package com.sprout.devops.util;

import com.jcraft.jsch.*;
import com.sprout.devops.server.entity.ServerHost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * SSH客户端工具类
 */
public class SSHClient {

    /**
     * 服务器主机信息，初始化后不可变
     */
    private final ServerHost serverHost;

    /**
     * session通道
     */
    private Session session;

    public SSHClient(ServerHost serverHost) throws Exception {
        this.serverHost = serverHost;
        connect();
    }

    /**
     * 连接服务器
     * @throws JSchException 连接失败抛出该异常
     */
    public void connect() throws Exception {
        JSch jsch = new JSch();
        session = jsch.getSession(serverHost.getUserName(), serverHost.getIp(), Integer.parseInt(serverHost.getPort()));
        session.setPassword(serverHost.getPassword());
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
    }

    /**
     * 执行ssh命令 并返回命令输出结果
     * @param cmd ssh命令
     * @return 命令执行结果
     * @throws Exception 执行失败抛出该异常
     */
    public String execCmd(String cmd) throws Exception {
        BufferedReader reader = null;
        Channel channel = null;
        try {
            //打开channel
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
            throw new Exception(e);
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

    /**
     * 关闭SSH通道
     */
    public void close() {
        if (session.isConnected()) {
            session.disconnect();
        }
    }
}
