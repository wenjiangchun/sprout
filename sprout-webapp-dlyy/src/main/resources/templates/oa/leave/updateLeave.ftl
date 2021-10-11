<!DOCTYPE html>
<html lang="zh">
<head>
    <title>修改申请单</title>
    <#include "../../common/head.ftl"/>
    <#include "../../common/form.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content">
    <div class="row">
        <div class="col-xs-12">
                    <div class="nav-tabs-custom">
                        <ul class="nav nav-tabs">
                            <li class="active"><a href="#activity" data-toggle="tab" aria-expanded="true">申请信息</a></li>
                            <li><a href="#timeline" data-toggle="tab" aria-expanded="true">流程信息</a></li>
                        </ul>
                        <div class="tab-content ">
                            <div class="tab-pane active" id="activity">
                                <form id="inputForm" class="form-horizontal" action="${ctx}/oa/leave/handleLeave/" method="post">
                                    <div class="box-body">
                                        <div class="form-group">
                                            <label for="name" class="col-sm-2 control-label">申请人姓名</label>
                                            <div class="col-sm-4">
                                                <p class="form-control-static">【${taskLeave.applier.group.name!}】-- ${taskLeave.applier.name!}</p>
                                            </div>
                                            <label for="leaveType.id" class="col-sm-2 control-label">请假类别</label>
                                            <div class="col-sm-4">
                                                <p class="form-control-static">${taskLeave.leaveType.name!}</p>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="leave['planStartTime']" class="col-sm-2 control-label">计划开始时间</label>
                                            <div class="col-sm-4">
                                                <input type="text" class="form-control" name="leave['planStartTime']" id="planStartTime" value="${taskLeave.planStartTime!}">
                                            </div>
                                            <label for="leave['planEndTime']" class="col-sm-2 control-label">计划结束时间</label>
                                            <div class="col-sm-4">
                                                <input type="text" class="form-control" name="leave['planEndTime']" id="planEndTime" value="${taskLeave.planEndTime!}">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="leave['content']" class="col-sm-2 control-label">请假事由</label>
                                            <div class="col-sm-4">
                                                <textarea class="form-control" name="leave['content']">${taskLeave.content!}</textarea>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="replayState" class="col-sm-2 control-label">审核结果</label>
                                            <div class="col-sm-6">
                                                <label class="radio-inline">
                                                    <input type="radio" name="replayState" id="inlineRadio1" value="1" checked> 重新申请
                                                </label>
                                                <label class="radio-inline">
                                                    <input type="radio" name="replayState" id="inlineRadio2" value="0"> 放弃申请
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="box-footer">
                                        <button type="submit" class="btn btn-primary pull-right"><i class="fa fa-check"></i> 提交</button>
                                        <input type="hidden" name="taskId" value="${taskLeave.currentTask.id}"/>
                                        <input type="hidden" name="firstApprovalId" value='${taskLeave.runtimeVariables["firstApprovalId"]}'/>
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
    let viewModel = {
    }
    ko.applyBindings(viewModel);
    $('#inputForm').ajaxForm({
        dataType : 'json',
        beforeSubmit:function(formData, jqForm, options){
            //$("#dairyStartDay").val($("#dairyStartDay").val() + " 00:00:00")
        },
        success : function(data) {
            if (data.flag) {
                layer.confirm(data.content, {
                    btn: ['关闭','取消'] //按钮
                }, function(){
                    top.hideMyModal();
                }, function(){
                });
            } else {
                layer.alert(data.content);
            }
        }
    });

    $('#planStartTime').datepicker({
        autoclose: true,
        language: 'zh-CN',
        calendarWeeks:true,
        todayHighlight: true,
        todayBtn: "linked"
    }).on('hide', function(e) {
        e.date;
    })
    $('#planEndTime').datepicker({
        autoclose: true,
        language: 'zh-CN',
        calendarWeeks:true,
        todayHighlight: true,
        todayBtn: "linked"
    }).on('hide', function(e) {

    })
</script>
</body>
</html>
