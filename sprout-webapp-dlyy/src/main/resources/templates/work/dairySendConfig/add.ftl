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
                    <form id="inputForm" class="form-horizontal" action="${ctx}/work/dairySendConfig/save/" method="post">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="name" class="col-sm-2 control-label">人员姓名</label>
                                <div class="col-sm-10">
                                    <input type="text" id="name"  class="form-control" maxlength="20" value="${worker.name!}" required readonly/>
                                    <input type="hidden" name="worker.id" class="form-control" maxlength="20" value="${worker.id!}"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="source" class="col-sm-2 control-label">发送邮箱</label>
                                <div class="col-sm-10">
                                    <input type="email" id="source" name="source" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="smtp" class="col-sm-2 control-label">邮箱服务器</label>
                                <div class="col-sm-10">
                                    <input type="text" id="smtp" name="smtp" class="form-control" maxlength="20" required />
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="protocol" class="col-sm-2 control-label">邮箱协议</label>
                                <div class="col-sm-10">
                                    <select class="form-control" name="protocol">
                                        <option value="smtp" selected>SMTP</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="token" class="col-sm-2 control-label">Token</label>
                                <div class="col-sm-10">
                                    <input type="text" id="token" name="token" class="form-control" maxlength="20" required />
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="destination" class="col-sm-2 control-label">接收邮箱</label>
                                <div class="col-sm-10">
                                    <input type="email" id="destination" name="destination" class="form-control" maxlength="20" required />
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="copyDestinations" class="col-sm-2 control-label">抄送邮箱</label>
                                <div class="col-sm-10">
                                    <input type="text" id="copyDestinations" name="copyDestinations" class="form-control" maxlength="50" required />
                                    <span class="help-block">多个邮箱以";"隔开</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="dairyStartDay" class="col-sm-2 control-label">日志开始时间</label>
                                <div class="col-sm-10">
                                    <input type="text" id="dairyStartDay" name="dairyStartDay" class="form-control" maxlength="50" required />
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="weekStartNum" class="col-sm-2 control-label">日志开始周数</label>
                                <div class="col-sm-10">
                                    <input type="number" id="weekStartNum" name="weekStartNum" class="form-control" maxlength="50" value="1" required min="1"/>
                                </div>
                            </div>
                        </div>
                        <div class="box-footer">
                            <button type="submit" class="btn btn-primary pull-right"><i class="fa fa-check"></i> 提交</button>
                            <button type="reset" class="btn btn-default">重置</button>
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
        beforeSubmit:function(formData, jqForm, options){
            //$("#dairyStartDay").val($("#dairyStartDay").val() + " 00:00:00")
        },
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

    $('#dairyStartDay').datepicker({
        autoclose: true,
        language: 'zh-CN',
        calendarWeeks:true,
        todayHighlight: true,
        todayBtn: "linked"
    }).on('hide', function(e) {
        $.post('${ctx}/work/dairySendConfig/getWorkDairyByWorkDay',{'workDay': e.date}, function(data) {
            viewModel.weekDay(data.weekDay);
            viewModel.weekNum(data.weekNum);
        });
    })
</script>
</body>
</html>
