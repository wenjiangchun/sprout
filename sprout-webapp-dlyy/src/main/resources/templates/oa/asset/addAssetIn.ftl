<!DOCTYPE html>
<html lang="zh">
<head>
    <#include "../../common/head.ftl"/>
    <#include "../../common/form.ftl"/>
    <script>
    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content-header">
    <ol class="breadcrumb">
        <li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a>
        </li>
        <li><a href="#">办公资产</a></li>
        <li class="active">资产入库</li>
    </ol>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box box-info">
                <div class="box-header with-border">
                    <h3 class="box-title">资产入库信息</h3>
                    <button type="button" class="btn btn-default pull-right" data-bind="click:addAssetIn"><i
                                class="fa fa-plus"></i></button>
                </div>
                <form id="inputForm" class="form-horizontal" action="${ctx}/oa/asset/saveAssetIn/" method="post">
                    <div class="box-body">
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th>资产编码</th>
                                <th>品牌</th>
                                <th>种类</th>
                                <th>型号</th>
                                <th>数量</th>
                                <th>单位</th>
                                <th>单价(元)</th>
                                <th>总价(元)</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
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
<script type="text/template" id="tpl">
    <tr>
        <td><input class="form-control" required/></td>
        <td>
            <select class="form-control" required>
                <option>请选择</option>
                <#list brandList as brand>
                    <option value="${brand.id}">${brand.name}</option>
                </#list>
            </select>
        </td>
        <td>
            <select class="form-control" required>
                <option>请选择</option>
                <#list classifyList as classify>
                    <option value="${classify.id}">${classify.name}</option>
                </#list>
            </select>
        </td>
        <td>
            <select class="form-control" required>
                <option>请选择</option>
                <#list modelList as model>
                    <option value="${model.id}">${model.name}</option>
                </#list>
            </select>
        </td>
        <td><input class="form-control" required/></td>
        <td>
            <select class="form-control" required>
                <option>请选择</option>
                <#list unitList as unit>
                    <option value="${unit.id}">${unit.name}</option>
                </#list>
            </select>
        </td>
        <td><input type="number" class="form-control" required/></td>
        <td><input type="number" class="form-control" required/></td>
        <td>
            <button type="button" class="btn btn-danger btn-xs" onclick="viewModel.removeAssetIn(this)"><i class="fa fa-minus"></i></button>
        </td>
    </tr>
</script>
<script>
    const $tr = _.template($("#tpl").html());
    let viewModel;
    $(function () {
        viewModel = {
            weekDay : ko.observable(),
            weekNum : ko.observable(),
            addAssetIn : function() {
             $("#inputForm").find('tbody').append($tr);
            },
            removeAssetIn: function(el) {
                console.log(el)
                $(el).parent().parent().remove();
                $("#inputForm").find('tbody').remove($(el).parent().parent());
            }
        }
        ko.applyBindings(viewModel);

        $('#inputForm').ajaxForm({
            dataType: 'json',
            success: function (data) {
                if (data.flag) {
                    layer.alert("操作成功", function () {
                        top.hideMyModal();
                    });
                } else {
                    layer.alert("操作失败【" + data.content + "】");
                }
            }
        });

        //初始化模板


    });

</script>
</body>
</html>
