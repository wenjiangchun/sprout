<!DOCTYPE html>
<html lang="zh">
<head>
    <title>日志发送配置</title>
    <#include "../../common/head.ftl"/>
    <#include "../../common/form.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content-header">
    <ol class="breadcrumb">
        <li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
        <li><a href="#">请假管理</a></li>
        <li class="active">请假申请</li>
    </ol>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
                <div class="box box-info">
                   <div class="box-header with-border">
                        <h3 class="box-title">请假申请信息</h3>
                    </div>
                    <form id="inputForm" class="form-horizontal" action="${ctx}/oa/leave/saveAndStartWorkflow/" method="post">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="name" class="col-sm-2 control-label">申请人姓名</label>
                                <div class="col-sm-10">
                                    <input type="text" id="name"  class="form-control" maxlength="20" value="${applier.name!}" required readonly/>
                                    <input type="hidden" name="applier.id" class="form-control" maxlength="20" value="${applier.userId!}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="leaveType.id" class="col-sm-2 control-label">请假类别</label>
                                <div class="col-sm-10">
                                    <select class="form-control" name="leaveType.id" required>
                                        <#list applyTypeList as applyType>
                                            <option value="${applyType.id}">${applyType.name}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="planStartTime" class="col-sm-2 control-label">计划请假开始时间</label>
                                <div class="col-sm-10">
                                    <input type="text" id="planStartTime" name="planStartTime" class="form-control" maxlength="50" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="planEndTime" class="col-sm-2 control-label">计划请假结束时间</label>
                                <div class="col-sm-10">
                                    <input type="text" id="planEndTime" name="planEndTime" class="form-control" maxlength="50" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="content" class="col-sm-2 control-label">请假原因</label>
                                <div class="col-sm-10">
                                    <textarea rows="3" id="personalSign" name="content" class="form-control" maxlength="200" required></textarea>
                                </div>
                            </div>
                        </div>
                        <div class="box-footer">
                            <div class="btn-group pull-right">
                                <button type="button" class="btn btn-primary ">提交至审核人</button>
                                <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                                    <span class="caret"></span>
                                    <span class="sr-only">Toggle Dropdown</span>
                                </button>
                                <ul class="dropdown-menu" role="menu">
                                    <#list userList as u>
                                        <li><a href="javascript:void(0)" onclick="viewModel.startWorkFlow(${u.id})">${u.name}</a></li>
                                    </#list>
                                </ul>
                            </div>
                            <button type="reset" class="btn btn-default">重置</button>
                            <input type = hidden name="firstApprovalId" data-bind="value:firstApprovalId">
                        </div>
                    </form>
                </div>
        </div>
    </div>
</section>
<script>

    let viewModel = {
        weekDay: ko.observable(''),
        firstApprovalId: ko.observable(),
        startWorkFlow: function(userId) {
            this.firstApprovalId(userId);
            $('#inputForm').submit();
        }
    }
    ko.applyBindings(viewModel);
    $('#inputForm').ajaxForm({
        dataType : 'json',
        beforeSubmit:function(formData, jqForm, options){
            //$("#dairyStartDay").val($("#dairyStartDay").val() + " 00:00:00")
        },
        success : function(data) {
            layer.alert(data.content);
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
