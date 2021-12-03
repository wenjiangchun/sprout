package com.sprout.devops.server.control;

import com.sprout.devops.server.entity.ServerHost;

/**
 * 服务器远程控制器接口,通过该接口定义了获取远程服务器状态,启动服务器和关闭服务器
 */
public interface ServerRemoteController {

    /**
     * 获取远程服务器开启状态
     * @return 返回当前服务器是否开启
     * @throws Exception 当命令失败或网络不可达抛出该异常
     */
    boolean getRemoteServerState() throws Exception;

    /**
     * 启动远程服务器
     * @throws Exception 启动失败抛出该异常
     */
    void startRemoteServer() throws Exception;

    /**
     * 关闭远程服务器
     * @throws Exception 关闭失败抛出该异常
     */
    void stopRemoteServer() throws Exception;

    /**
     * 获取控制器对应ServerHost对象
     * @return serverHost
     */
    ServerHost getServerHost();

}
