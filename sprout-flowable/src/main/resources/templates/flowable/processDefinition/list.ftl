<!DOCTYPE html>
<html lang="zh">
<head>
    <title>流程定义管理</title>
    <#include "../../common/head.ftl"/>
    <#include "../../common/datatable.ftl"/>
    <#include "../../common/upload.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content-header">
    <ol class="breadcrumb">
        <li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a>
        </li>
        <li><a href="#">流程管理</a></li>
        <li class="active">流程定义管理</li>
    </ol>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">流程定义列表</h3>
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
                            <th>流程定义ID</th>
                            <#--<th>部署ID</th>-->
                            <th>名称</th>
                            <th>KEY</th>
                            <th>版本号</th>
                            <th>XML</th>
                            <th>图片</th>
                            <th>部署时间</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list processDefinitionList as process>
                            <#assign processDefinition=process[0]/>
                            <#assign deployment=process[1]/>
                            <#assign isSuspended=processDefinition.isSuspended()/>
                            <tr>
                                <td>${processDefinition.id}</td>
                                <#--<td>${processDefinition.deploymentId}</td>-->
                                <td>${processDefinition.name }</td>
                                <td>${processDefinition.key }</td>
                                <td>${processDefinition.version }</td>
                                <td><a target="_blank"
                                       href='${ctx}/flowable/processDefinition/getProcessResource?deploymentId=${processDefinition.deploymentId}&resourceName=${processDefinition.resourceName }'>${processDefinition.resourceName }</a>
                                </td>
                                <td><a target="_blank"
                                       href='${ctx }/flowable/processDefinition/getProcessResource?deploymentId=${processDefinition.deploymentId}&resourceName=${processDefinition.diagramResourceName }'>${processDefinition.diagramResourceName }</a>
                                </td>
                                <td>${deployment.deploymentTime?string("yyyy-MM-dd hh:mm:ss")}</td>
                                <td>
                                    <#if !isSuspended>
                                        <span class="label label-success">已激活</span>
                                    <#else>
                                        <span class="label label-danger">已挂起</span>
                                    </#if>
                                </td>
                                <td>
                                    <#if isSuspended>
                                        <button class="btn btn-default btn-xs" onclick="viewModel.updateProcessState('${processDefinition.id}','active','激活')">激活</button>
                                    <#else >
                                        <button class="btn btn-default btn-xs" onclick="viewModel.updateProcessState('${processDefinition.id}','suspend','挂起')">挂起</button>
                                    </#if>
                                    <button class="btn btn-default btn-xs" onclick="viewModel.deleteProcess('${processDefinition.deploymentId}')">删除</button>
                                </td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->
            <input type="file" name="file" id="uploadExcel">
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
        $("#uploadExcel").fileinput({
            uploadUrl: '${ctx}/flowable/processDefinition/deployProcessDefinition',
            enableResumableUpload: false,
            browseLabel: '流程部署',
            browseIcon: '<i class="fa fa-upload"></i>',
            showPreview: false,
            showCaption: false,
            showUploadStats: false,
            showUploadedThumbs: false,
            progressDelay: 1000,
            dropZoneEnabled: false,
            uploadAsync: false,
            showUpload: false,
            showRemove: false,
            language: 'zh'
        }).on('fileuploaded', function(event, data, previewId, index, fileId) {
            layer.alert(data.response.content);
            window.location.reload()
        }).on("filebatchselected", function (event, files) {
            $("#uploadExcel").fileinput("upload");
            $('.kv-hidden').hide();
        });
    });

</script>
</html>
