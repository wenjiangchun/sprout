<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <#include "../../common/head.ftl"/>
    <#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content-header">
    <ol class="breadcrumb">
        <li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
        <li><a href="#">考勤记录</a></li>
        <li class="active">规则设置</li>
    </ol>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">考勤规则列表</h3>
                    <div class="box-tools">
                        <#--<a href="#" id="refreshRepository" class="btn btn-default"><i class="fa fa-repeat"></i>  刷新</a>
                        <a href="#" id="add_btn" class="btn btn-info"><i class="fa fa-plus-circle"></i>  添加</a>-->
                    </div>
                </div>
                <!-- /.box-header -->
                <div class="box-body">
                    <table class="table table-bordered table-striped" id="myTable">
                        <thead>
                        <tr>
                            <th colspan="4" style="text-align: center">上午</th><th colspan="4" style="text-align: center">下午</th><th rowspan="2">缓冲时间</th><th rowspan="2">迟到缺勤时间</th><th rowspan="2">早退缺勤时间</th><th rowspan="2">操作</th>
                        </tr>
                        <tr>
                            <th>上班时间</th><th>下班时间</th><th>记录开启时间</th><th>记录结束时间</th><th>上班时间</th><th>下班时间</th><th>记录开启时间</th><th>记录结束时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list configList as config>
                            <tr>
                                <td>${config.swStartTime!}</td>
                                <td>${config.swEndTime!}</td>
                                <td>${config.swJudgeStartTime!}</td>
                                <td>${config.swJudgeEndTime!}</td>
                                <td>${config.xwStartTime!}</td>
                                <td>${config.xwEndTime!}</td>
                                <td>${config.xwJudgeStartTime!}</td>
                                <td>${config.xwJudgeEndTime!}</td>
                                <td>${config.bufferWorktime!}</td>
                                <td>${config.lateAbsenceTime!}</td>
                                <td>${config.leaveAbsenceTime!}</td>
                                <td><a href="#" onclick="showData('${config.id!}')"><i class="fa fa-pencil"></i></a></td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->
        </div>
        <!-- /.col -->
    </div>
    <!-- /.row -->
</section>

</body>
<script>
    $(document).ready(function(){
        $('#myTable').DataTable({
            "ordering":false,
            "language": {
                "lengthMenu": "每页显示 _MENU_条记录",
                "zeroRecords": "没有检索到数据",
                "info": "显示第 _START_ - _END_ 条记录；共 _TOTAL_ 条记录",
                "infoEmpty": "",
                "processing": "正在加载数据...",
                "infoFiltered": "",
                "search":"检索：",
                "paginate": {
                    "first": "首页",
                    "previous": "上一页",
                    "next": "下一页",
                    "last": '尾页'
                }
            }
        });
    });
    function showData(id) {
        showMyModel('${ctx}/dlyy/monitor/editConfig/' + id, '编辑规则', '80%', '60%', refreshData);
    }

    function refreshData() {
        window.location.href = '${ctx}/dlyy/monitor/config';
    }
</script>
</html>