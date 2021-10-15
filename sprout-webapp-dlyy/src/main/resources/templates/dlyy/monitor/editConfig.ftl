<!DOCTYPE html>
<html lang="zh">
<head>
    <#include "../../common/head.ftl"/>
    <#include "../../common/form.ftl"/>
    <link rel="stylesheet" href="${ctx}/res/lib/timepicker/bootstrap-timepicker.min.css">
    <script src="${ctx}/res/lib/timepicker/bootstrap-timepicker.min.js"></script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content">
    <div class="row">
        <div class="col-xs-12">
                <div class="box box-info">
                    <div class="box-header with-border">
                        <h3 class="box-title">规则配置信息</h3>
                    </div>
                    <form id="inputForm" class="form-horizontal" action="${ctx}/dlyy/monitor/saveConfig/" method="post">
                        <div class="box-body">
                            <div class="row">
                                <div class="form-group col-sm-6">
                                    <label for="swStartTime" class="col-sm-4 control-label">上午上班时间</label>
                                    <div class="col-sm-4 input-append">
                                        <input type="text" id="swStartTime" name="swStartTime" class="form-control timepicker" maxlength="20" value="${config.swStartTime!}" required/>
                                    </div>
                                </div>
                                <div class="form-group col-sm-6">
                                    <label for="swEndTime" class="col-sm-4 control-label">上午下班时间</label>
                                    <div class="col-sm-4 input-append">
                                        <input type="text" data-format="hh:mm:ss" id="swEndTime" name="swEndTime" class="form-control timepicker" maxlength="20" value="${config.swEndTime!}" required/>
                                        <span class="add-on">
                                      <i data-time-icon="icon-time" data-date-icon="icon-calendar">
                                      </i>
                                    </span>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group col-sm-6">
                                    <label for="swJudgeStartTime" class="col-sm-4 control-label">上午记录开启时间</label>
                                    <div class="col-sm-4 input-append">
                                        <input type="text" data-format="hh:mm:ss" id="swJudgeStartTime" name="swJudgeStartTime" class="form-control timepicker"  maxlength="20" value="${config.swJudgeStartTime!}" required/>
                                    </span>
                                    </div>
                                </div>
                                <div class="form-group col-sm-6">
                                    <label for="swJudgeEndTime" class="col-sm-4 control-label">上午记录结束时间</label>
                                    <div class="col-sm-4">
                                        <input type="text" data-format="hh:mm:ss" id="swJudgeEndTime" name="swJudgeEndTime" class="form-control timepicker" maxlength="20" value="${config.swJudgeEndTime!}" required/>
                                    </span>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="form-group col-sm-6">
                                    <label for="xwStartTime" class="col-sm-4 control-label">下午上班时间</label>
                                    <div class="col-sm-4">
                                        <input type="text" data-format="hh:mm:ss" id="xwStartTime" name="xwStartTime" class="form-control timepicker" maxlength="20" value="${config.xwStartTime!}" required/>
                                        <span class="add-on">
                                      <i data-time-icon="icon-time" data-date-icon="icon-calendar">
                                      </i>
                                    </span>
                                    </div>
                                </div>
                                <div class="form-group col-sm-6">
                                    <label for="xwEndTime" class="col-sm-4 control-label">下午下班时间</label>
                                    <div class="col-sm-4">
                                        <input type="text" data-format="hh:mm:ss" id="xwEndTime" name="xwEndTime" class="form-control timepicker" maxlength="20" value="${config.xwEndTime!}" required/>
                                        <span class="add-on">
                                      <i data-time-icon="icon-time" data-date-icon="icon-calendar">
                                      </i>
                                    </span>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group col-sm-6">
                                    <label for="xwJudgeStartTime" class="col-sm-4 control-label">下午记录开启时间</label>
                                    <div class="col-sm-4">
                                        <input type="text" data-format="hh:mm:ss" id="xwJudgeStartTime" name="xwJudgeStartTime" class="form-control timepicker" maxlength="20" value="${config.xwJudgeStartTime!}" required/>
                                        <span class="add-on">
                                      <i data-time-icon="icon-time" data-date-icon="icon-calendar">
                                      </i>
                                    </span>
                                    </div>
                                </div>
                                <div class="form-group col-sm-6">
                                    <label for="xwJudgeEndTime" class="col-sm-4 control-label">下午记录结束时间</label>
                                    <div class="col-sm-4">
                                        <input type="text" data-format="hh:mm:ss" id="xwJudgeEndTime" name="xwJudgeEndTime" class="form-control timepicker" maxlength="20" value="${config.xwJudgeEndTime!}" required/>
                                        <span class="add-on">
                                      <i data-time-icon="icon-time" data-date-icon="icon-calendar">
                                      </i>
                                    </span>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="form-group col-sm-6">
                                    <label for="bufferWorktime" class="col-sm-4 control-label">缓冲时间</label>
                                    <div class="col-sm-4">
                                        <input type="text" data-format="hh:mm:ss" id="bufferWorktime" name="bufferWorktime" class="form-control timepicker" maxlength="20" value="${config.bufferWorktime!}" required/>
                                        <span class="add-on">
                                      <i data-time-icon="icon-time" data-date-icon="icon-calendar">
                                      </i>
                                    </span>
                                    </div>
                                </div>
                                <div class="form-group col-sm-6">
                                    <label for="lateAbsenceTime" class="col-sm-4 control-label">迟到缺勤时间</label>
                                    <div class="col-sm-4">
                                        <input type="text" data-format="hh:mm:ss" id="lateAbsenceTime" name="lateAbsenceTime" class="form-control timepicker" maxlength="20" value="${config.lateAbsenceTime!}" required/>
                                        <span class="add-on">
                                      <i data-time-icon="icon-time" data-date-icon="icon-calendar">
                                      </i>
                                    </span>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group col-sm-6">
                                    <label for="leaveAbsenceTime" class="col-sm-4 control-label">早退缺勤时间</label>
                                    <div class="col-sm-4">
                                        <input type="text" data-format="hh:mm:ss" id="leaveAbsenceTime" name="leaveAbsenceTime" class="form-control timepicker" maxlength="20" value="${config.leaveAbsenceTime!}" required/>
                                        <span class="add-on">
                                      <i data-time-icon="icon-time" data-date-icon="icon-calendar">
                                      </i>
                                    </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="box-footer">
                            <button type="submit" class="btn btn-primary pull-right"><i class="fa fa-check"></i> 提交</button>
                            <button type="reset" class="btn btn-default">重置</button>
                            <input type="hidden" name="id" value="${config.id!}">
                        </div>
                    </form>
                </div>
        </div>
    </div>
</section>

<script>
    $(function() {
        $('.timepicker').timepicker({
            showInputs: false,
            showMeridian:false,
            showSeconds:true
        })
        /*$('.input-append').datetimepicker({
            pickDate: false
        });*/
    });
    $('#inputForm').ajaxForm({
        dataType : 'json',
        success : function(data) {
            if (data.flag) {
                layer.alert("操作成功", function() {
                    top.hideMyModal();
                });
            } else {
                layer.alert("操作失败【"+data.content+"】");
            }
        }
    });
</script>
</body>
</html>
