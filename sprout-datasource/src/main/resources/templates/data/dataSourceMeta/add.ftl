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
                    <div class="box-header with-border">
                        <h3 class="box-title">数据源信息</h3>
                    </div>
                    <form id="inputForm" class="form-horizontal" action="${ctx}/data/dataSourceMeta/save/" method="post">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="dataSourceMetaType" class="col-sm-2 control-label">数据源类型</label>
                                <div class="col-sm-10">
                                    <select class="form-control" name="dataSourceMetaType">
                                        <#list driverList as driverClass>
                                            <option value="${driverClass!}">${driverClass!}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="name" class="col-sm-2 control-label">数据源名称</label>
                                <div class="col-sm-10">
                                    <input type="text" id="name" name="name" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="ip" class="col-sm-2 control-label">IP/机器名</label>
                                <div class="col-sm-10">
                                    <input type="text" id="ip" name="ip" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="schema" class="col-sm-2 control-label">schema</label>
                                <div class="col-sm-10">
                                    <input type="text" id="schema" name="schema" class="form-control" maxlength="200"/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="port" class="col-sm-2 control-label">端口</label>
                                <div class="col-sm-10">
                                    <input type="text" id="port" name="port" class="form-control" maxlength="200"/>
                                </div>
                            </div>
                            <#--<div class="form-group db">
                                <label for="URL" class="col-sm-2 control-label">URL</label>
                                <div class="col-sm-10">
                                    <input type="text" id="URL" name="URL" class="form-control" maxlength="200" required/>
                                </div>
                            </div>-->
                            <div class="form-group db">
                                <label for="userName" class="col-sm-2 control-label">用户名</label>
                                <div class="col-sm-10">
                                    <input type="text" id="userName" name="userName" class="form-control" maxlength="200"/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="password" class="col-sm-2 control-label">密码</label>
                                <div class="col-sm-10">
                                    <input type="text" id="password" name="password" class="form-control" maxlength="200"/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="remark" class="col-sm-2 control-label">描述:</label>
                                <div class="col-sm-10">
                                    <textarea rows="3" id="remark" name="remark" class="form-control" maxlength="200"></textarea>
                                </div>
                            </div>
                        </div>
                        <div class="box-footer">
                            <button type="button" class="btn btn-default" data-bind='click: testConnect'><i class="fa fa-terminal"></i> 测试连通</button>
                            <button type="submit" class="btn btn-primary"><i class="fa fa-check"></i> 提交</button>
                            <button type="reset" class="btn btn-danger">重置</button>
                        </div>
                    </form>
                </div>
        </div>
    </div>
</section>
<script>
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

    let viewModel = {
        testConnect: function() {
            $.post('${ctx}/data/dataSourceMeta/testConnect?',$("form").serialize(), function(data) {
                layer.alert(data.content);
            });
        }
    };
    ko.applyBindings(viewModel);
</script>
</body>
</html>
