<!DOCTYPE html>
<html>
<head>
  <#include "../common/head.ftl"/>
    <link rel="stylesheet" href="${ctx}/resources/adminLTE/bower_components/datatables.net-bs/css/dataTables.bootstrap.min.css">
    <link rel="stylesheet" href="${ctx}/resources/adminLTE/bower_components/Ionicons/css/ionicons.min.css">
    <link href="${ctx}/resources/zTree/css/bootstrapStyle/bootstrapStyle.css" type="text/css" rel="stylesheet" />
</head>
<body class="hold-transition skin-blue sidebar-mini">
    <!-- Content Wrapper. Contains page content -->
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                在线用户
                <small><i class="fa fa-exchange"></i></small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> 主页</a></li>
                <li><a href="#">系统监控</a></li>
                <li class="active">在线用户</li>
            </ol>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">在线用户列表</h3>
                            <div class="box-tools">
                                <a href="#" class="btn btn-default"><i class="fa fa-repeat"></i>  刷新</a>
                            </div>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <table id="example1" class="table table-bordered table-striped table-hover">
                                <thead>
                                <tr>
                                    <th>登陆名</th>
                                    <th>姓名</th>
                                    <th>登陆IP</th>
                                    <th>登陆时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <#list onlineList as user>
                                <tr>
                                    <td>${user.loginName!}</td>
                                    <td>${user.name!}</td>
                                    <td>${user.ip!}</td>
                                    <td>${user.startLoginTime?datetime}</td>
                                    <td>
                                        <a href="javascript:void(0)"
                                           onclick=""
                                           class="btn btn-danger btn-sm" title="强制退出"><i class="fa fa-trash"></i></a>
                                    </td>
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
        <!-- /.content -->
    <!-- /.content-wrapper -->
<script src="${ctx}/resources/adminLTE/bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${ctx}/resources/adminLTE/bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script>
    $(function () {
        initMenu('monitor_online_menu');
        $('#example1').DataTable({
            'paging': true,
            'lengthChange': true,
            'searching': true,
            'ordering': false,
            'info': true,
            'autoWidth': true,
            "oLanguage": {
                "sLengthMenu": "每页显示 _MENU_条记录",
                "sZeroRecords": "没有检索到数据",
                "sInfo": "显示第 _START_ - _END_ 条记录；共 _TOTAL_ 条记录",
                "sInfoEmpty": "",
                "sProcessing": "正在加载数据...",
                "sSearch": "检索：",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "上一页",
                    "sNext": "下一页",
                    "sLast": '尾页'
                }
            },
            "columnDefs": [
                     { "width": "20%", "targets": [ 3 ] },
                     { "ordering": "true", "targets": [ 2 ] }
                   ]
            });
    });
</script>
</body>
</html>
