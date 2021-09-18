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
                        <h3 class="box-title">字典信息</h3>
                    </div>
                    <form id="inputForm" class="form-horizontal" action="${ctx}/system/dict/save/" method="post">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="name" class="col-sm-2 control-label">字典名称</label>
                                <div class="col-sm-10">
                                    <input type="text" id="name" name="name" class="form-control" maxlength="20" required value="${dict.name!}"/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="code" class="col-sm-2 control-label">字典代码</label>
                                <div class="col-sm-10">
                                    <input type="text" id="code" name="code" class="form-control" maxlength="20" required value="${dict.code!}" readonly/>
                                </div>
                            </div>
                            <div class="form-group generic">
                                <label for="pname" class="col-sm-2 control-label">字典分类:</label>
                                <div class="col-sm-10">
                                    <#if dict.parent??>
                                        <input type="text" id="pname" name="pname" class="form-control" value="${dict.parent.name!}" disabled/>
                                        <input type="hidden" id="parentID" name="parent.id" value="${dict.parent.id!}"/>
                                    <#else>
                                        <input type="text" id="pname" name="pname" class="form-control" value="" disabled/>
                                        <input type="hidden" id="parentID" name="parent.id" value="" disabled/>
                                    </#if>
                                </div>
                            </div>
                            <div class="form-group generic">
                                <label for="tel" class="col-sm-2 control-label">状态:</label>
                                <div class="col-sm-10">
                                    <#if dict.enabled>
                                        <label class="radio-inline"><input type="radio" name="enabled" value="true" checked="checked"/>启用</label>
                                        <label class="radio-inline"><input type="radio" name="enabled" value="false"/>禁用</label>
                                    <#else>
                                        <label class="radio-inline"><input type="radio" name="enabled" value="true"/>启用</label>
                                        <label class="radio-inline"><input type="radio" name="enabled" value="false" checked="checked"/>禁用</label>
                                    </#if>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="description" class="col-sm-2 control-label">描述:</label>
                                <div class="col-sm-10">
                                    <textarea rows="3" id="description" name="description" class="form-control" maxlength="200"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="remark" class="col-sm-2 control-label">备注:</label>
                                <div class="col-xs-10">
                                    <textarea id="remark" name="remark" class="form-control" maxlength="200">${dict.description!}</textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="sn" class="col-sm-2 control-label">显示顺序:</label>
                                <div class="col-xs-4">
                                    <input type="number" name="sn" placeholder="请输入显示顺序" class="form-control" value="${dict.sn!}"/>
                                </div>
                            </div>
                        </div>
                        <div class="box-footer">
                            <button type="submit" class="btn btn-primary"><i class="fa fa-check"></i> 保存</button>
                            <button type="reset" class="btn btn-danger">重置</button>
                            <input type="hidden" id="id" name="id" value="${dict.id!}"/>
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
</script>
</body>
</html>
