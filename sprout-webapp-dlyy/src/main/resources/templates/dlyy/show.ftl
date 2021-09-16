<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <#include "../common/head.ftl"/>
    <#include "../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box">
                <div class="box-header">
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
                            <#list meta as m>
                                <th>${m.name}</th>
                            </#list>
                        </tr>
                        </thead>
                        <tbody>
                        <#list data as d >
                            <tr>
                                <#list meta as m>
                                    <#assign mv=d[m.name]!''/>
                                    <#if mv?length gt 200>
                                        <td title="${mv}">${mv?substring(0,200)}</td>
                                    <#else >
                                        <td>${mv}</td>
                                    </#if>
                                </#list>
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
        parent.showMyModel('${ctx}/dlyy/kudu/showData?table=${table!}');
    }
</script>
</html>