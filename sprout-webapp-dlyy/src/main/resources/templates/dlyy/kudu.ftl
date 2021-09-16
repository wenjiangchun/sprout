<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <#include "../common/head.ftl"/>
    <#include "../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content-header">
    <ol class="breadcrumb">
        <li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
        <li><a href="#">龙湖相关</a></li>
        <li class="active">Kudu信息</li>
    </ol>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">KUDU列表</h3>
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
                            <th>tableId</th><th>表名</th><th>注释</th><th>复制份数</th><th>数据量(条)</th><th>存储空间(MB)</th><th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list tableList as table >
                            <tr>
                                <td>${table.tableId!}</td>
                                <td>${table.tableName!}</td>
                                <td>${table.comment!}</td>
                                <td>${table.numReplicate!}</td>
                                <td>-</td>
                                <td>${table.tableSize!}</td>
                                <td><a href="#" onclick="showData('${table.tableName!}')">预览数据</a></td>
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
    function showData(tableName) {
        showMyModel('${ctx}/dlyy/kudu/showData?tableName=' + tableName, '预览数据【'+tableName+'】', '100%', '80%');
    }
</script>
</html>