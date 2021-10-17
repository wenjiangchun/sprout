<!DOCTYPE html>
<html lang="zh">
<head>
    <title>镜像容器管理</title>
    <#include "../../common/head.ftl"/>
    <#include "../../common/form.ftl"/>
    <#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content">
    <div class="row">
        <div class="col-xs-12">
                <div class="box box-info" id="container">
                    <table class="table table-bordered">
                        <tbody>
                        <tr>
                            <th>容器ID</th><td data-bind="text:id"></td>
                        </tr>
                        <tr>
                            <th>容器名称</th><td data-bind="text:name"></td>
                        </tr>
                        <tr>
                            <th>驱动</th><td data-bind="text:driver"></td>
                        </tr>
                        <tr>
                            <th>运行状态</th><td data-bind="text:state"></td>
                        </tr>
                        <tr>
                            <th>平台</th><td data-bind="text:platform"></td>
                        </tr>
                        <tr>
                            <th>hostname路径</th><td data-bind="text:hostnamePath"></td>
                        </tr>
                        <tr>
                            <th>hosts路径</th><td data-bind="text:hostsPath"></td>
                        </tr><tr>
                            <th>日志路径</th><td data-bind="text:logPath"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
        </div>
    </div>
</section>
<script>
    let viewModel = {
        id: ko.observable(),
        name: ko.observable(),
        driver: ko.observable(),
        state: ko.observable(),
        platform: ko.observable(),
        resolvConfPath: ko.observable(),
        hostnamePath: ko.observable(),
        hostsPath: ko.observable(),
        logPath: ko.observable(),
        hostConfig: ko.observable(),
        config: ko.observable(),
        networkSettings: ko.observable(),
        Status: ko.observableArray([]),
        getTops: function() {
            $.get('${ctx}/devops/docker/getContainerInfo/${containerId}/json', {'ip': '${dockerHost.ip}', 'port': '${dockerHost.port}'},function(dt) {
                viewModel.id(dt.Id);
                viewModel.state(dt.State.Status);
                viewModel.name(dt.Name);
                viewModel.platform(dt.Platform);
                viewModel.driver(dt.Driver);
                viewModel.resolvConfPath(dt.ResolvConfPath);
                viewModel.hostnamePath(dt.HostnamePath);
                viewModel.hostsPath(dt.HostsPath);
                viewModel.logPath(dt.LogPath);
                viewModel.hostConfig(dt.HostConfig);
                viewModel.config(dt.Config);
                viewModel.networkSettings(dt.NetworkSettings);
            });
        },

    }
    ko.applyBindings(viewModel);
    $(function() {
        viewModel.getTops();
    });
    function timestampToTime(timestamp) {
        var date = new Date(timestamp * 1000);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
        var Y = date.getFullYear() + '-';
        var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        var D = date.getDate() + ' ';
        var h = date.getHours() + ':';
        var m = date.getMinutes() + ':';
        var s = date.getSeconds();
        return Y+M+D+h+m+s;
    }
</script>
</body>
</html>
