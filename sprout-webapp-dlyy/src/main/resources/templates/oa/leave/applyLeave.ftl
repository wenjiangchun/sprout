<!DOCTYPE html>
<html lang="zh">
<head>
    <title>填写请假申请</title>
    <#include "../../common/head.ftl"/>
    <#include "../../common/form.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content-header">
    <ol class="breadcrumb">
        <li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
        <li><a href="#">请假管理</a></li>
        <li><a href="${ctx}/oa/leave/getMyLeave">待办请假</a></li>
        <li class="active">请假申请</li>
    </ol>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
                <div class="box box-default">
                   <div class="box-header with-border">
                        <h3 class="box-title">请假申请信息</h3>
                       <div class="pull-right">
                           <button class="btn btn-box-tool" onclick="window.history.go(-1)"><i class="fa fa-reply"></i> </button>
                       </div>
                    </div>
                    <form id="inputForm" class="form-horizontal" action="${ctx}/oa/leave/saveAndStartWorkflow/" method="post">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="name" class="col-sm-2 control-label">申请人姓名</label>
                                <div class="col-sm-10">
                                    <input type="text" id="name"  class="form-control" maxlength="20" value="${applier.group.name!}-->${applier.name!}" required readonly/>
                                    <input type="hidden" name="applier.id" class="form-control" maxlength="20" value="${applier.userId!}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="leaveType.id" class="col-sm-2 control-label">请假类别</label>
                                <div class="col-sm-4">
                                    <select class="form-control" name="leaveType.id" id="leaveTypeSelect" data-bind="event:{change: selectLeaveType}" required>
                                        <#list applyTypeList as applyType>
                                            <option value="${applyType.id}">${applyType.name}</option>
                                        </#list>
                                    </select>
                                </div>
                                <span id="helpBlock" class="help-block text-red text-bold" data-bind="visible: showUseHoliday">年休假共${holiday}天</span>
                            </div>
                            <div class="form-group">
                                <label for="planStartTime" class="col-sm-2 control-label">计划开始时间</label>
                                <div class="col-sm-4">
                                    <input type="text" id="planStartTime" name="planStartTime" class="form-control" maxlength="50" required/>
                                </div>
                                <div class="col-sm-4">
                                    <label class="radio-inline"><input type="radio" class="minimal" name="planStartFlag" checked="checked" value="0"/> 全天</label>
                                    <label class="radio-inline"><input type="radio" class="minimal" name="planStartFlag" value="1"/> 上午</label>
                                    <label class="radio-inline"><input type="radio" class="minimal" name="planStartFlag" value="2"/> 下午</label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="planEndTime" class="col-sm-2 control-label">计划结束时间</label>
                                <div class="col-sm-4">
                                    <input type="text" id="planEndTime" name="planEndTime" class="form-control" maxlength="50" required/>
                                </div>
                                <div class="col-sm-4">
                                    <label class="radio-inline"><input type="radio" class="minimal" name="planEndFlag" checked="checked" value="0"/> 全天</label>
                                    <label class="radio-inline"><input type="radio" class="minimal" name="planEndFlag" value="1"/> 上午</label>
                                    <label class="radio-inline"><input type="radio" class="minimal" name="planEndFlag" value="2"/> 下午</label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="content" class="col-sm-2 control-label">请假原因</label>
                                <div class="col-sm-10">
                                        <textarea rows="3" name="content" class="form-control" maxlength="200" required></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="leaveType.id" class="col-sm-2 control-label">下一步审核人</label>
                                <div class="col-sm-4">
                                    <#--<select class="form-control" id="nextApprovalSelect" data-bind="event:{change: selectNextApproval}" required>-->
                                    <select class="form-control" data-bind="value:nextApprovalId" required>
                                        <#list userList as u>
                                            <option value="${u.id}">${u.name}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="box-footer">
                            <button type="submit" class="btn btn-primary pull-right"><i class="fa fa-check-circle-o"></i> 提交申请</button>
                            <button type="reset" class="btn btn-default">重置</button>
                            <#if userLevel==1>
                                <input type="hidden" name="flowVariables['firstApprovalId']" data-bind="value:nextApprovalId">
                            <#else>
                                <input type="hidden" name="flowVariables['fourthApprovalId']" data-bind="value:nextApprovalId">
                            </#if>
                            <input type="hidden" name="flowVariables['userLevel']" value="${userLevel!}"/>
                        </div>
                    </form>
                </div>
        </div>
    </div>
</section>
<script>

    let viewModel = {
        weekDay: ko.observable(''),
        nextApprovalId: ko.observable(''),
        showUseHoliday: ko.observable(true),
        startWorkFlow: function() {
            this.nextApprovalId($('#nextApprovalId').val());
            $('#inputForm').submit();
        },
        selectLeaveType: function() {
            console.log($('#leaveTypeSelect').val());
            this.showUseHoliday($('#leaveTypeSelect').val() == 11);
        },
        getUseHolidayInfo: function() {
            let that = this;
            $.get('${ctx}/oa/leaveStatistic/getUserHolidayInfo/${applier.userId}', function(dts) {
                //that.useHolidayDts(dts);
                 let html = '本年度暂无请假记录';
                 if (dts.length > 0) {
                     html = '';
                     _.each(dts, function(dt) {
                         html += ' 已使用'+ dt.days + '天'
                     });
                 }
                 $('#helpBlock').html($('#helpBlock').html() + ', ' + html);
            });
        }
    }
    ko.applyBindings(viewModel);
    viewModel.getUseHolidayInfo();
    $('#inputForm').ajaxForm({
        dataType : 'json',
        beforeSubmit:function(formData, jqForm, options){
            viewModel.nextApprovalId($('#nextApprovalId').val());
        },
        success : function(data) {
            if (data.flag) {
                layer.alert(data.content, function(index) {
                    layer.close(index);
                    window.location.href = "${ctx}/oa/leave/getMyLeave";
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
        console.log(e.date);
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
