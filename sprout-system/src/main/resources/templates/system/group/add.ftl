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
                        <h3 class="box-title">机构信息</h3>
                    </div>
                    <form id="inputForm" class="form-horizontal" action="${ctx}/system/group/save" method="post">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="fullName" class="col-sm-2 control-label">机构全称</label>
                                <div class="col-sm-10">
                                    <input type="text" id="fullName" name="fullName" class="form-control" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="name" class="col-sm-2 control-label">机构简称</label>
                                <div class="col-sm-10">
                                    <input type="text" id="name" name="name" class="form-control" required/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="groupType.id" class="col-sm-2 control-label">机构类型</label>
                                <div class="col-sm-10">
                                    <select class="form-control" name="groupType.id" required>
                                        <#list groupTypeList as groupType>
                                            <option value="${groupType.id}">${groupType.name}</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="code" class="col-sm-2 control-label">机构代码</label>
                                <div class="col-sm-10">
                                    <input type="text" id="code" name="code" class="form-control" required/>
                                </div>
                            </div>
                            <div class="form-group generic">
                                <label for="pname" class="col-sm-2 control-label">所在机构:</label>
                                <div class="col-sm-10">
                                    <#if parent??>
                                        <input type="text" id="pname" name="pname" class="form-control" value="${parent.fullName}" disabled/>
                                        <input type="hidden" id="parentID" name="parent.id" value="${parent.id}"/>
                                    <#else>
                                        <input type="text" id="pname" name="pname" class="form-control" value="" disabled/>
                                        <input type="hidden" id="parentID" name="parent.id" value="" disabled/>
                                    </#if>
                                </div>
                            </div>
                            <div class="form-group generic">
                                <label for="tel" class="col-sm-2 control-label">联系电话</label>

                                <div class="col-sm-10">
                                    <input type="text" id="tel" name="tel" class="form-control"/>
                                </div>
                            </div>
                            <div class="form-group db">
                                <label for="address" class="col-sm-2 control-label">机构地址</label>
                                <div class="col-sm-10">
                                    <input type="text" id="address" name="address" class="form-control"/>
                                </div>
                            </div>
                            <div class="form-group generic">
                                <label for="linker" class="col-sm-2 control-label">联系人</label>
                                <div class="col-sm-10">
                                    <input type="text" id="tel" name="linker" class="form-control"/>
                                </div>
                            </div>
                            <div class="form-group generic">
                                <label for="linkerMobile" class="col-sm-2 control-label">联系人电话</label>
                                <div class="col-sm-10">
                                    <input type="text" id="linkerMobile" name="linkerMobile" class="form-control"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="description" class="col-sm-2 control-label">描述:</label>
                                <div class="col-xs-10">
                                    <textarea id="description" name="description" class="form-control"></textarea>
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
