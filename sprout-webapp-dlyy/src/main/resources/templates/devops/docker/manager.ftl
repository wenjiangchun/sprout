<!DOCTYPE html>
<html lang="zh">
<head>
    <title>镜像容器管理</title>
    <#include "../../common/head.ftl"/>
    <#include "../../common/form.ftl"/>
    <#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content-header">
    <ol class="breadcrumb">
        <li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
        <li><a href="#">运维管理</a></li>
        <li><a href="${ctx}/devops/docker/view">Docker主机管理</a></li>
        <li class="active">镜像容器管理</li>
    </ol>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="nav-tabs-custom">
                <ul class="nav nav-tabs">
                    <li class="active"><a href="#container" data-toggle="tab" aria-expanded="true">容器信息</a></li>
                    <li><a href="#image" data-toggle="tab" aria-expanded="true">镜像信息</a></li>
                    <li class="pull-right"><a href="#" style="font-size: 16px;font-weight: bold;color: red"><span class="label label-success">[主机信息/${dockerHost.ip}:${dockerHost.port}]</span></a></li>
                </ul>
                <div class="tab-content ">
                    <div class="tab-pane active" id="container">
                        <table class="table table-bordered">
                            <thead>
                               <tr>
                                   <th>容器ID</th>
                                   <th>容器名称</th>
                                   <th>启动命令</th>
                                   <th>端口映射</th>
                                   <th>镜像名称</th>
                                   <th>当前状态</th>
                                   <th>运行状态</th>
                                   <th>创建时间</th>
                                   <th>操作</th>
                               </tr>
                            </thead>
                            <tbody data-bind="foreach:containers">
                              <tr>
                                  <td><a href="#" data-bind="text:$data.brevId,click: $parent.showContainerInfo" title="点击查看"></a></td>
                                  <td><a href="#" data-bind="text:$data.containerName,click: $parent.renameContainer" title="点击修改"></a></td>
                                  <td data-bind="text:$data.Command"></td>
                                  <td data-bind="html:$data.portBidding"></td>
                                  <td data-bind="text:$data.Image"></td>
                                  <td>
                                      <div data-bind="if: $data.State == 'running'">
                                          <span class="label label-success" data-bind="text:$data.State" ></span>
                                      </div>
                                      <div data-bind="if: $data.State != 'running'">
                                          <span class="label label-danger" data-bind="text:$data.State" ></span>
                                      </div>
                                  </td>
                                  <td data-bind="text:$data.Status"></td>
                                  <td data-bind="text:$data.createTime"></td>
                                  <td>
                                      <button class="btn btn-default btn-xs" data-bind="click: $parent.deleteContainer" title="删除"><i class="fa fa-trash-o"></i> </button>
                                      <button class="btn btn-default btn-xs" data-bind="click: $parent.showContainerProcess" title="查看进程"><i class="fa fa-product-hunt"></i> </button>
                                      <div data-bind="if: $data.State == 'running'">
                                          <button class="btn btn-default btn-xs" data-bind="click: $parent.controlContainer" title="停止"><i class="fa fa-stop"></i> </button>
                                          <button class="btn btn-default btn-xs" data-bind="click: $parent.downloadContainerLogs" title="下载日志"><i class="fa fa-file"></i> </button>
                                      </div>
                                      <div data-bind="if: $data.State != 'running'">
                                          <button class="btn btn-default btn-xs" data-bind="click: $parent.controlContainer" title="启动"><i class="fa fa-play"></i> </button>
                                      </div>
                                  </td>
                              </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="tab-pane " id="image">
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th>镜像ID</th>
                                <th>资源名称</th>
                                <th>标签</th>
                                <th>镜像大小</th>
                                <th>创建时间</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody data-bind="foreach:images">
                            <tr>
                                <td data-bind="text:$data.brevId"></td>
                                <td data-bind="text:$data.repoName"></td>
                                <td data-bind="text:$data.tag"></td>
                                <td data-bind="text:$data.imageSize"></td>
                                <td data-bind="text:$data.createTime"></td>
                                <td>
                                    <button class="btn btn-default btn-xs" data-bind="click: $parent.deleteImage"><i class="fa fa-trash-o"></i> </button>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<script>
    let viewModel = {
        weekDay: ko.observable(''),
        images: ko.observableArray([]),
        containers:ko.observableArray([]),
        getRemoteImages: function() {
            viewModel.images([]);
            $.get('${ctx}/devops/docker/manager/getImages', {'ip': '${dockerHost.ip}', 'port': '${dockerHost.port}'},function(dts) {
                _.each(dts, function(dt) {
                    dt.brevId = dt.Id.replaceAll("sha256:","").substr(0, 12);
                    dt.createTime = timestampToTime(dt.Created);
                    let size = dt.Size;
                    dt.imageSize = Math.round(size/(1000*1000)) + 'MB';
                    let repoName = '';
                    let tag = '';
                    if (dt.RepoTags != null && dt.RepoTags.length > 0) {
                        const repoTag = dt.RepoTags[0];
                        const repoTagArr = repoTag.split(':');
                        repoName = repoTagArr[0];
                        tag = repoTagArr[1];
                    } else {
                        if (dt.RepoDigests != null && dt.RepoDigests.length > 0) {
                            const repo = dt.RepoDigests[0];
                            //从@截取
                            repoName = repo.substr(0, repo.indexOf("@"));
                        }
                    }
                    dt.repoName = repoName;
                    dt.tag = tag;

                });
                viewModel.images(dts);
                console.log(dts);
            });
        },
        getRemoteContainers: function() {
            viewModel.containers([]);
            $.get('${ctx}/devops/docker/manager/getContainers', {'ip': '${dockerHost.ip}', 'port': '${dockerHost.port}'},function(dts) {
                _.each(dts, function(dt) {
                    dt.brevId = dt.Id.substr(0, 12);
                    dt.createTime = timestampToTime(dt.Created);
                    dt.containerName = dt.Names[0].replaceAll("/","");
                    let portBidding = '';
                    if (dt.Ports != null && dt.Ports.length > 0) {
                        _.each(dt.Ports, function(p, idx) {
                            if (p.PublicPort !== undefined && p.PublicPort !== "" ) {

                                const bidding = p.Type + ':' + p.PrivatePort + '->' + p.PublicPort;
                                if (portBidding.indexOf(bidding) == -1) {
                                    portBidding += p.Type + ':' + p.PrivatePort + '->' + p.PublicPort;
                                    if (idx != 0) {
                                        portBidding += '<br>';
                                    }
                                }
                            }
                        });
                    }
                    dt.portBidding = portBidding;
                });
                viewModel.containers(dts);
                console.log(dts);
            });
        },
        renameContainer: function() {
            let that = this;
            layer.prompt({title: '请输入容器名称',value: this.containerName},function(value, index, elem){
                if (value != null && value !== '') {
                    $.post('${ctx}/devops/docker/renameContainer/'+that.Id, {'ip': '${dockerHost.ip}', 'port': '${dockerHost.port}', 'containerName': value}, function (data) {
                        if (data.flag) {
                            layer.close(index);
                            viewModel.getRemoteContainers();
                        } else {
                            layer.alert(data.content);
                        }
                    });
                } else {
                    layer.alert('容器名称不能为空');
                }
            });
        },
        deleteContainer: function() {
            $.post('${ctx}/devops/docker/deleteContainer/'+this.brevId, {'ip': '${dockerHost.ip}', 'port': '${dockerHost.port}'}, function (data) {
                if (data.flag) {
                    window.location.reload();
                }
            });
        },
        controlContainer: function() {
            let command = this.State === 'running'? 'stop':'start';
            $.post('${ctx}/devops/docker/controlContainer/' + this.brevId + '/' + command, {'ip': '${dockerHost.ip}', 'port': '${dockerHost.port}'}, function (data) {
                if (data.flag) {
                    if (command === 'start') {
                        layer.alert('容器已启动', function(index) {
                            viewModel.getRemoteContainers();
                            layer.close(index);
                        });
                    } else {
                        layer.alert('容器已停止', function(index) {
                            viewModel.getRemoteContainers();
                            layer.close(index);
                        });
                    }
                } else {
                    layer.alert(data.content);
                }
            });
        },
        showContainerProcess: function() {
            let url = "${ctx}/devops/docker/showContainer/${dockerHost.id}/" + this.brevId + "/top";
            top.showMyModel(url,'查看进程信息[' + this.containerName + ']', '70%', '70%');
        },
        showContainerInfo: function() { //查看容器基本信息
            let url = "${ctx}/devops/docker/showContainer/${dockerHost.id}/" + this.brevId + "/json";
            top.showMyModel(url,'查看容器信息[' + this.containerName + ']', '70%', '70%');
        },
        downloadContainerLogs: function() {
            window.location.href= "${ctx}/devops/docker/getContainerLogs/" + this.brevId + "/logs?ip=${dockerHost.ip}&port=${dockerHost.port}";
        },
        deleteImage: function() {
            $.post('${ctx}/devops/docker/deleteImage/'+this.brevId, {'ip': '${dockerHost.ip}', 'port': '${dockerHost.port}'}, function (data) {
                if (data.flag) {
                    window.location.reload();
                } else {
                    layer.alert(data.content);
                }
            });
        }
    }

    $(function() {
        viewModel.getRemoteImages();
        viewModel.getRemoteContainers();
        $("#image").find('table').DataTable({
            "ordering": false,
            "language": {
                "lengthMenu": "每页显示 _MENU_条记录",
                "zeroRecords": "没有检索到数据",
                "info": "显示第 _START_ - _END_ 条记录；共 _TOTAL_ 条记录",
                "infoEmpty": "",
                "processing": "正在加载数据...",
                "infoFiltered": "",
                "search":"检索：",
                "paginate": {
                    "first": "首页",
                    "previous": "上一页",
                    "next": "下一页",
                    "last": '尾页'
                }
            }
        });
        $("#container").find('table').DataTable({
            "ordering": false,
            "language": {
                "lengthMenu": "每页显示 _MENU_条记录",
                "zeroRecords": "没有检索到数据",
                "info": "显示第 _START_ - _END_ 条记录；共 _TOTAL_ 条记录",
                "infoEmpty": "",
                "processing": "正在加载数据...",
                "infoFiltered": "",
                "search":"检索：",
                "paginate": {
                    "first": "首页",
                    "previous": "上一页",
                    "next": "下一页",
                    "last": '尾页'
                }
            }
        });
        ko.applyBindings(viewModel);
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
