<!DOCTYPE html>
<html>
<head>
  <#include "../common/head.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
<#include "../common/nav.ftl"/>
    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                jvm
                <small><i class="fa fa-exchange"></i></small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> 主页</a></li>
                <li><a href="#">系统监控</a></li>
                <li class="active">sshconsole</li>
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
                            <textarea data-bind="value: command"></textarea>
                            <button data-bind="click: execCommand">执行</button>
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
    </div>
    <!-- /.content-wrapper -->
<#include "../common/foot.ftl"/>
<#include "../common/left.ftl"/>
</div>
<script>
    let viewModal = {
        command: ko.observable(''),
        execCommand: function() {
            $.post({
                url: '${ctx}/monitor/ssh/exec',
                data: {
                    'command': this.command()
                },
                success:function(data) {
                    viewModal.command(data);
                }
            });
        }
    };
    $(function () {
        initMenu('monitor_ssh_menu');
        ko.applyBindings(viewModal);
    });
</script>
</body>
</html>
