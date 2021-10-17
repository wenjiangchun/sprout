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
                        <h3 class="box-title">配置信息</h3>
                    </div>
                    <form id="inputForm" class="form-horizontal" action="${ctx}/system/config/save/" method="post">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="name" class="col-sm-2 control-label">配置名称</label>
                                <div class="col-sm-10">
                                    <input type="text" id="name" name="name" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="code" class="col-sm-2 control-label">配置代码</label>
                                <div class="col-sm-10">
                                    <input type="text" id="code" name="code" class="form-control" maxlength="20" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="value" class="col-sm-2 control-label">配置值</label>
                                <div class="col-sm-10">
                                    <input type="text" id="value" name="value" class="form-control" maxlength="200" required/>
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
            if (data.flag) {
                layer.alert(data.content, function() {
                    top.hideMyModal();
                });
            } else {
                layer.alert(data.content);
            }
        }
    });
</script>
</body>
</html>
