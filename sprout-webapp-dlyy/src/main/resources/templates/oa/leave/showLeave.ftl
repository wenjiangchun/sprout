<!DOCTYPE html>
<html lang="zh">
<head>
    <title>请假查看</title>
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
                                <form id="inputForm" class="form-horizontal" action="#" method="post">
                                    <div class="box-body">
                                        <div class="form-group">
                                            <label for="name" class="col-sm-2 control-label">申请人姓名</label>
                                            <div class="col-sm-4">
                                                <p class="form-control-static">【${leave.applier.group.name!}】-- ${leave.applier.name!}</p>
                                            </div>
                                            <label for="leaveType.id" class="col-sm-2 control-label">请假类别</label>
                                            <div class="col-sm-4">
                                                <p class="form-control-static">${leave.leaveType.name!}</p>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="name" class="col-sm-2 control-label">计划开始时间</label>
                                            <div class="col-sm-4">
                                                <p class="form-control-static">${leave.planStartTime!}</p>
                                            </div>
                                            <label for="leaveType.id" class="col-sm-2 control-label">计划结束时间</label>
                                            <div class="col-sm-4">
                                                <p class="form-control-static">${leave.planEndTime!}</p>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="name" class="col-sm-2 control-label">请假事由</label>
                                            <div class="col-sm-4">
                                                <p class="form-control-static">${leave.content!}</p>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="leave['realStartTime']" class="col-sm-2 control-label">实际开始时间</label>
                                            <div class="col-sm-4">
                                                <p class="form-control-static">${leave.realStartTime!}</p>
                                            </div>
                                            <label for="leave['realEndTime']" class="col-sm-2 control-label">实际结束时间</label>
                                            <div class="col-sm-4">
                                                <p class="form-control-static">${leave.realEndTime!}</p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="box-footer">
                                        <button type="button" class="btn btn-primary pull-right"><i class="fa fa-close"></i> 关闭</button>
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

</script>
</body>
</html>
