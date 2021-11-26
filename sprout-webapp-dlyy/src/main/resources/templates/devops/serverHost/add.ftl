<!DOCTYPE html>
<html lang="zh">
<head>
    <#include "../../common/head.ftl"/>
    <#include "../../common/form.ftl"/>
    <script>
    </script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content">
    <div class="row">
        <div class="col-xs-12">
                <div class="box box-info">
                   <#-- <div class="box-header with-border">
                        <h3 class="box-title">日志信息</h3>
                    </div>-->
                    <form id="inputForm" class="form-horizontal" action="${ctx}/devops/serverHost/save/" method="post">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="os" class="col-sm-2 control-label">操作系统</label>
                                <div class="col-sm-10">
                                    <select id="os" name="os" class="form-control">
                                        <#list osTypeList as os>
                                            <option value="${os}">${os}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="name" class="col-sm-2 control-label">主机名称</label>
                                <div class="col-sm-10">
                                    <input type="text" id="name" name="name" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="ip" class="col-sm-2 control-label">IP</label>
                                <div class="col-sm-10">
                                    <input type="text" id="ip" name="ip" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="port" class="col-sm-2 control-label">端口</label>
                                <div class="col-sm-10">
                                    <input type="text" id="port" name="port" class="form-control" maxlength="20" value="22"/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="userName" class="col-sm-2 control-label">用户名</label>
                                <div class="col-sm-10">
                                    <input type="text" id="userName" name="userName" class="form-control" maxlength="20"/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="password" class="col-sm-2 control-label">密码</label>
                                <div class="col-sm-10">
                                    <input type="text" id="password" name="password" class="form-control" maxlength="20"/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="description" class="col-sm-2 control-label">备注</label>
                                <div class="col-sm-10">
                                    <textarea rows="3" id="description" name="description" class="form-control" maxlength="200" ></textarea>
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
    let viewModel;
    $(function() {
         viewModel = {
        }
        ko.applyBindings(viewModel);
        $('#inputForm').ajaxForm({
            dataType : 'json',
            success : function(data) {
                if (data.flag) {
                    let index = layer.alert("操作成功", function() {
                        top.hideMyModal();
                        layer.close(index);
                    });
                } else {
                    layer.alert("操作失败【"+data.content+"】");
                }
            }
        });
    });
</script>
</body>
</html>
