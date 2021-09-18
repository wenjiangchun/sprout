<!DOCTYPE html>
<html>
<head>
    <#include "../../common/head.ftl"/>
    <#include "../../common/form.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content">
    <div class="row">
        <div class="col-xs-12">
                <div class="box box-info">
                   <#-- <div class="box-header with-border">
                        <h3 class="box-title">日志信息</h3>
                    </div>-->
                    <form id="inputForm" class="form-horizontal" action="${ctx}/work/workDairy/save/" method="post">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="name" class="col-sm-2 control-label">人员姓名</label>
                                <div class="col-sm-10">
                                    <input type="text" id="name" name="name" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="workDay" class="col-sm-2 control-label">工作日</label>
                                <div class="col-sm-10">
                                    <input type="text" id="workDay" name="workDay" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="weekDay" class="col-sm-2 control-label">星期</label>
                                <div class="col-sm-10">
                                    <input type="text" id="weekDay" name="weekDay" class="form-control" maxlength="20" required data-bind="value: weekDay"/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="code" class="col-sm-2 control-label">周次</label>
                                <div class="col-sm-10">
                                    <input type="text" id="weekNum" name="weekNum" class="form-control" maxlength="20" required data-bind="value: weekNum"/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="content" class="col-sm-2 control-label">工作内容</label>
                                <div class="col-sm-10">
                                    <textarea rows="3" id="content" name="content" class="form-control" maxlength="200"></textarea>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="value" class="col-sm-2 control-label">备注</label>
                                <div class="col-sm-10">
                                    <textarea rows="3" id="remark" name="remark" class="form-control" maxlength="200"></textarea>
                                </div>
                            </div>
                        </div>
                        <div class="box-footer">
                            <button type="submit" class="btn btn-primary"><i class="fa fa-check"></i> 提交</button>
                            <button type="reset" class="btn btn-danger">重置</button>
                        </div>
                    </form>
                </div>
        </div>
    </div>
</section>
<script>

    let viewModel = {
        weekDay: ko.observable(''),
        weekNum: ko.observable(0)
    }
    ko.applyBindings(viewModel);
    $('#inputForm').ajaxForm({
        dataType : 'json',
        success : function(data) {
            if (data.messageType === "SUCCESS") {
                layer.alert("操作成功", function() {
                    parent.hideMyModal();
                });
            } else {
                layer.alert("操作失败【"+data.content+"】");
            }
        }
    });

    $('#workDay').datepicker({
        autoclose: true,
        language: 'zh-CN',
        calendarWeeks:true,
        todayHighlight: true,
        todayBtn: "linked"
    }).on('hide', function(e) {
        $.post('${ctx}/work/workDairy/getWorkDairyByWorkDay',{'workDay': e.date}, function(data) {
            viewModel.weekDay(data.weekDay);
            viewModel.weekNum(data.weekNum);
        });
    })
</script>
</body>
</html>
