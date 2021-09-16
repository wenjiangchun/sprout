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
                jvm
                <small><i class="fa fa-exchange"></i></small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> 主页</a></li>
                <li><a href="#">系统监控</a></li>
                <li class="active">jvm</li>
            </ol>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">jvm信息列表</h3>
                            <div class="box-tools">
                                <a href="#" class="btn btn-default"><i class="fa fa-repeat"></i>  刷新</a>
                            </div>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <table id="example1" class="table table-bordered table-striped table-hover">
                                <thead>
                                <tr>
                                    <th>参数</th>
                                    <th>值</th>
                                    <th>说明</th>
                                </tr>
                                </thead>
                                <tbody>
                                <#list infoList as info>
                                <tr>
                                    <td>${info.key!}</td>
                                    <td>${info.value!}</td>
                                    <td>${info.description!}</td>
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
        initMenu('monitor_jvm_menu');
    });
</script>
</body>
</html>
