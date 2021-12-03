<!DOCTYPE html>
<html lang="zh">
<head>
    <title>主机监控</title>
    <#include "../../common/head.ftl"/>
    <#include "../../common/form.ftl"/>
    <#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content-header">
    <ol class="breadcrumb">
        <li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
        <li><a href="#">运维管理</a></li>
        <li><a href="${ctx}/devops/serverHost/view">主机管理</a></li>
        <li class="active">主机监控</li>
    </ol>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="nav-tabs-custom">
                <ul class="nav nav-tabs">
                    <li class="active"><a href="#serverInfo" data-toggle="tab" aria-expanded="true">主机信息</a></li>
                    <li><a href="#diskInfo" data-toggle="tab" aria-expanded="true">磁盘信息</a></li>
                    <li><a href="#cpuInfo" data-toggle="tab" aria-expanded="true">CPU信息</a></li>
                    <li><a href="#memoryInfo" data-toggle="tab" aria-expanded="true">内存信息</a></li>
                    <li class="pull-right"><a href="#" style="font-size: 16px;font-weight: bold;color: red"><span class="label label-success">[主机信息/${serverHost.ip}:${serverHost.port}]</span></a></li>
                </ul>
                <div class="tab-content ">
                    <div class="tab-pane active bg-black-gradient" id="serverInfo">
                    </div>
                    <div class="tab-pane bg-black-gradient" id="diskInfo">
                    </div>
                    <div class="tab-pane bg-black-gradient" id="cpuInfo">
                    </div>
                    <div class="tab-pane bg-black-gradient" id="memoryInfo">
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<script>
    let viewModel = {
        getServerInfo: function() {
            let url = "${ctx}/devops/serverHost/runCMD/${serverHost.id}/";
            let cmd = {};
            if ('${serverHost.os}' === 'centos') {
                cmd = {"cmd": "cat /etc/redhat-release"}
            } else {
                cmd = {"cmd": "lsb_release -a"}
            }
            $.get(url, cmd, function(data) {
                console.log(data);
                $('#serverInfo').html(data);
            });
        },
        getDiskInfo: function() {
            let url = "${ctx}/devops/serverHost/runCMD/${serverHost.id}/";
            $.get(url, {"cmd": "df"}, function(data) {
                console.log(data);
                $('#diskInfo').html(data);
            });
        },
        getCpuInfo: function() {
            let url = "${ctx}/devops/serverHost/runCMD/${serverHost.id}/";
            $.get(url, {"cmd": "cat /proc/cpuinfo"}, function(data) {
                console.log(data);
                $('#cpuInfo').html(data);
            });
        },
        getMemoryInfo: function() {
            let url = "${ctx}/devops/serverHost/runCMD/${serverHost.id}/";
            $.get(url, {"cmd": "free"}, function(data) {
                console.log(data);
                $('#memoryInfo').html(data);
            });
        },
        getNetInfo: function() {
            let url = "${ctx}/devops/serverHost/runCMD/${serverHost.id}/";
            $.get(url, {"cmd": "lsb_release -a"}, function(data) {
                console.log(data);
                $('#netInfo').html(data);
            });
        }
    }

    $(function() {
        viewModel.getServerInfo();
        viewModel.getDiskInfo();
        viewModel.getCpuInfo();
        viewModel.getNetInfo();
        viewModel.getMemoryInfo();
        ko.applyBindings(viewModel);
    });
</script>
</body>
</html>
