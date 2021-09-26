<!DOCTYPE html>
<html>
<head>
    <#include "../../common/head.ftl"/>
    <#include "../../common/form.ftl"/>
    <script>

    </script>
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
                                    <input type="text" id="name" name="name" class="form-control" maxlength="20" value="${workDairy.worker.name!}" required readonly/>
                                    <input type="hidden" name="worker.id" class="form-control" maxlength="20" value="${workDairy.worker.id!}" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="workDay" class="col-sm-2 control-label">工作日</label>
                                <div class="col-sm-10">
                                    <input type="text" id="workDay" name="workDay" class="form-control" maxlength="20" value="${workDairy.workDay?string('yyyy-MM-dd')!}" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="weekDay" class="col-sm-2 control-label">星期</label>
                                <div class="col-sm-10">
                                    <input type="text" id="weekDay" name="weekDay" class="form-control" maxlength="20" required data-bind="value: weekDay" readonly value="${workDairy.weekDay!}"/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="code" class="col-sm-2 control-label">周次</label>
                                <div class="col-sm-10">
                                    <input type="number" id="weekNum" name="weekNum" class="form-control" maxlength="20" required data-bind="value: weekNum" readonly value="${workDairy.weekNum!}"/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="content" class="col-sm-2 control-label">工作内容</label>
                                <div class="col-sm-10">
                                    <textarea rows="5" id="content" name="content" class="form-control" maxlength="300">${workDairy.content!}</textarea>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="remark" class="col-sm-2 control-label">备注</label>
                                <div class="col-sm-10">
                                    <textarea rows="3" id="remark" name="remark" class="form-control" maxlength="200" >${workDairy.remark!}</textarea>
                                </div>
                            </div>
                        </div>
                        <div class="box-footer">
                            <button type="submit" class="btn btn-primary pull-right"><i class="fa fa-check"></i> 提交</button>
                            <button type="reset" class="btn btn-default">重置</button>
                            <input type="hidden" name="id" id="id" class="form-control" maxlength="20" value="${workDairy.id!}"/>
                        </div>
                    </form>
                </div>
        </div>
    </div>
</section>
<script>
    let viewModel;
    $(function() {
         viewModel = {
            weekDay: ko.observable('${workDairy.weekDay!}'),
            weekNum: ko.observable(${workDairy.weekNum!})
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
                $("#content").val(data.content);
                $("#remark").val(data.remark);
                $("#id").val(data.id);
            });
        })
    });

</script>
</body>
</html>
