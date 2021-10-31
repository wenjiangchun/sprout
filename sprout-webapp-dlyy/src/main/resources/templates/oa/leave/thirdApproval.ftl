<!DOCTYPE html>
<html lang="zh">
<head>
    <title>请假审核</title>
    <#include "../../common/head.ftl"/>
    <#include "../../common/form.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content-header">
    <ol class="breadcrumb">
        <li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
        <li><a href="#">请假管理</a></li>
        <li><a href="${ctx}/oa/leave/todoView">待办请假</a></li>
        <li class="active">${taskLeave.currentTask.name!}</li>
    </ol>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
                    <div class="nav-tabs-custom">
                        <ul class="nav nav-tabs">
                            <li class="active"><a href="#activity" data-toggle="tab" aria-expanded="true">申请信息</a></li>
                            <li><a href="#timeline" data-toggle="tab" aria-expanded="true">流程信息</a></li>
                            <li class="pull-right"><button class="btn btn-box-tool" onclick="window.history.go(-1)"><i class="fa fa-reply"></i> </button></li>
                        </ul>
                        <div class="tab-content ">
                            <div class="tab-pane active" id="activity">
                                <form id="inputForm" class="form-horizontal" action="${ctx}/oa/leave/handleLeave/" method="post">
                                    <div class="box-body">
                                        <div class="form-group">
                                            <label for="name" class="col-sm-2 control-label">申请人姓名:</label>
                                            <div class="col-sm-4">
                                                <p class="form-control-static">【${taskLeave.applier.group.name!}】-- ${taskLeave.applier.name!}</p>
                                            </div>
                                            <label for="leaveType.id" class="col-sm-2 control-label">请假类别:</label>
                                            <div class="col-sm-4">
                                                <p class="form-control-static">${taskLeave.leaveType.name!}</p>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="name" class="col-sm-2 control-label">计划开始时间:</label>
                                            <div class="col-sm-4">
                                                <p class="form-control-static">${taskLeave.planStartTime!}</p>
                                            </div>
                                            <label for="leaveType.id" class="col-sm-2 control-label">计划结束时间:</label>
                                            <div class="col-sm-4 ">
                                                <p class="form-control-static">${taskLeave.planEndTime!}</p>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="name" class="col-sm-2 control-label">请假事由:</label>
                                            <div class="col-sm-4">
                                                <p class="form-control-static">${taskLeave.content!}</p>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="firstApprovalState" class="col-sm-2 control-label">审核结果:</label>
                                            <div class="col-sm-6">
                                                <label class="radio-inline">
                                                    <input type="radio" name="flowVariables['thirdApprovalState']" class="minimal" id="inlineRadio1" value="1" data-bind="checked:thirdApprovalStateChecked">
                                                    通过
                                                </label>
                                                <label class="radio-inline">
                                                    <input type="radio" name="flowVariables['thirdApprovalState']" class="minimal" id="inlineRadio3" value="0" data-bind="checked:thirdApprovalStateChecked"> 退回
                                                </label>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="firstApprovalContent" class="col-sm-2 control-label">审核意见:</label>
                                            <div class="col-sm-10">
                                                <textarea rows="3" id="thirdApprovalContent" name="flowVariables['thirdApprovalContent']" class="form-control" maxlength="200" required></textarea>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="box-footer">
                                        <button type="submit" class="btn btn-primary pull-right"><i class="fa fa-check"></i> 提交审核</button>
                                        <input type="hidden" name="taskId" value="${taskLeave.currentTask.id}"/>
                                        <input type="hidden" name="flowVariables['thirdApprovalId']" value="${taskLeave.runtimeVariables["thirdApprovalId"]}"/>
                                        <div class="table-responsive no-padding" style="margin-top: 60px">
                                            <h5 class="box-title">近3个月请假记录</h5>
                                            <table class="table table-bordered">
                                                <thead>
                                                <tr class="bg-light-blue">
                                                    <th>申请人</th>
                                                    <th>申请时间</th>
                                                    <th>请假开始时间</th>
                                                    <th>请假结束时间</th>
                                                    <th>请假类型</th>
                                                    <th>销假时间</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <#list recentLeaveList as r>
                                                    <tr>
                                                        <td>${r.applier.group.name!}-${r.applier.name}</td>
                                                        <td>${r.applierTime!}</td>
                                                        <td>${r.realStartTime!}</td>
                                                        <td>${r.realEndTime!}</td>
                                                        <td>${r.leaveType.name!}</td>
                                                        <td>${r.backTime!}</td>
                                                    </tr>
                                                </#list>
                                                <#if recentLeaveList?size==0>
                                                    <tr>
                                                        <td colspan="6" align="center">暂无相关记录</td>
                                                    </tr>
                                                </#if>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <!-- /.tab-pane -->
                            <div class="tab-pane " id="timeline">
                                <!-- The timeline -->
                                <ul class="timeline timeline-inverse">
                                    <!-- timeline time label -->
                                    <#list leaveTaskLogList as log>
                                        <li class="time-label">
                                            <span class="bg-red">
                                              ${log.handler.group.name}-${log.handler.name}
                                            </span>
                                        </li>
                                        <li>
                                            <i class="fa fa-pencil-square-o bg-blue"></i>
                                            <div class="timeline-item">
                                                <span class="time"><i class="fa fa-clock-o"></i> ${log.handleTime!}</span>
                                                <h3 class="timeline-header">${log.taskName}</h3>
                                                <div class="timeline-body">
                                                    ${log.result!}
                                                </div>
                                            </div>
                                        </li>
                                    </#list>
                                </ul>
                            </div>
                        </div>
                    </div>
        </div>
    </div>
</section>
<script>

    let viewModel;
    viewModel = {
        thirdApprovalId: ko.observable(),
        thirdApprovalStateChecked: ko.observable('1')
    }
    ko.applyBindings(viewModel);
    $(function(){
        $('#inputForm').ajaxForm({
            dataType : 'json',
            beforeSubmit:function(formData, jqForm, options){
                return true;
            },
            success : function(data) {
                if (data.flag) {
                    layer.alert(data.content, function(idx) {
                        layer.close(idx);
                        window.location.href='${ctx}/oa/leave/todoView';
                    });
                } else {
                    layer.alert(data.content);
                }
            }
        });
    });
</script>
</body>
</html>
