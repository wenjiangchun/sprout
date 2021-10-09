<!DOCTYPE html>
<html lang="zh">
<head>
    <title>流程实例管理</title>
    <#include "../../common/head.ftl"/>
    <#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content-header">
    <ol class="breadcrumb">
        <li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a>
        </li>
        <li><a href="#">流程管理</a></li>
        <li class="active">流程实例管理</li>
    </ol>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">流程实例列表</h3>
                    <div class="box-tools">
                        <#--<a href="#" id="refreshRepository" class="btn btn-default"><i class="fa fa-repeat"></i>  刷新</a>
                        <a href="#" id="add_btn" class="btn btn-info"><i class="fa fa-plus-circle"></i>  添加</a>-->
                    </div>
                </div>
                <!-- /.box-header -->
                <div class="box-body">
                    <table id="contentTable" class="table table-bordered table-striped table-hover">
                        <thead>
                        <tr>
                            <th>执行ID</th>
                            <th>流程实例ID</th>
                            <th>流程定义ID</th>
                            <th>当前节点</th>
                            <th>是否挂起</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list processInstanceList as instance>
                            <#assign isSuspended=instance.suspended/>
                            <tr>
                                <td>${instance.id}</td>
                                <#--<td>${processDefinition.deploymentId}</td>-->
                                <td>${instance.processInstanceId}</td>
                                <td>${instance.processDefinitionId}</td>
                                <td><a class="trace" href='#' pid="${instance.id}" title="点击查看流程图">点击查看</a></td>
                                <td>
                                    <#if !isSuspended>
                                        <span class="label label-success">已激活</span>
                                    <#else>
                                        <span class="label label-danger">已挂起</span>
                                    </#if>
                                </td>
                                <td>
                                    <#if isSuspended>
                                        <button class="btn btn-default btn-xs" onclick="viewModel.updateProcessState('${instance.id}','active','激活')">激活</button>
                                    <#else >
                                        <button class="btn btn-default btn-xs" onclick="viewModel.updateProcessState('${instance.id}','suspend','挂起')">挂起</button>
                                    </#if>
                                </td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->
        </div>
        <!-- /.col -->
    </div>
    <!-- /.row -->
</section>
</body>
<script src="${ctx}/res/lib/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${ctx}/res/lib/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script type="text/javascript">
    let viewModel;
    $(document).ready(function () {
        viewModel = {
			updateProcessState: function (processId, state, action) {
                layer.confirm("确认" + action + "流程信息？", {
                    btn: ['确定','取消'] //按钮
                }, function(){
                    $.post('${ctx}/flowable/processDefinition/updateProcessDefinitionState/' + processId + "/" + state, function(data) {
                        if (data.flag) {
                            window.location.reload()
                        } else {
                            layer.alert(data.content);
                        }
                    });
                }, function(){

                });
            },
			deleteProcess: function (deploymentId) {
                layer.confirm("确认删除该流程信息？", {
                    btn: ['确定','取消'] //按钮
                }, function(){
                    $.post('${ctx}/flowable/processDefinition/deleteProcessDefination/' + deploymentId, function(data) {
                        if (data.flag) {
                            window.location.reload()
                        } else {
                            layer.alert(data.content);
                        }
                    });
                }, function(){

                });
            }
        };
        ko.applyBindings(viewModel);
    });
</script>
</html>