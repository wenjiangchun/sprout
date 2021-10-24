<!DOCTYPE html>
<html lang="zh">
<head>
    <#include "../../common/head.ftl"/>
    <#include "../../common/form.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content-header">
    <ol class="breadcrumb">
        <li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
        <li><a href="#">系统管理</a></li>
        <li><a href="${ctx}/system/role/view">角色管理</a></li>
        <li class="active">添加角色</li>
    </ol>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
                <div class="box box-default">
                    <div class="box-header with-border">
                        <h3 class="box-title">角色信息</h3>
                        <div class="pull-right">
                            <button class="btn btn-box-tool" onclick="window.history.go(-1)"><i class="fa fa-reply"></i> </button>
                        </div>
                    </div>
                    <form id="inputForm" class="form-horizontal" action="${ctx}/system/role/save/" method="post">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="name" class="col-sm-2 control-label">角色名称</label>
                                <div class="col-sm-10">
                                    <input type="text" id="name" name="name" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="code" class="col-sm-2 control-label">角色代码</label>
                                <div class="col-sm-10">
                                    <input type="text" id="code" name="code" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="enabled" class="col-sm-2 control-label">状态</label>
                                <div class="col-sm-10">
                                    <label class="radio-inline"><input type="radio" class="minimal" name="enabled" value="true" checked="checked"/> 启用</label>
                                    <label class="radio-inline"><input type="radio" class="minimal" name="enabled" value="false"/> 禁用</label>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="description" class="col-sm-2 control-label">描述:</label>
                                <div class="col-sm-10">
                                    <textarea rows="3" id="description" name="description" class="form-control" maxlength="200"></textarea>
                                </div>
                            </div>
                        </div>
                        <div class="box-footer">
                            <button type="submit" class="btn btn-primary pull-right"><i class="fa fa-check-circle"></i> 提交</button>
                            <button type="reset" class="btn btn-default">重置</button>
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
            if (data.flag) {
                layer.alert(data.content, function(index) {
                    window.history.go(-1);
                });
            } else {
                layer.alert(data.content);
            }
        }
    });
</script>
</body>
</html>
