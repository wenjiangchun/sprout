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
                        <h3 class="box-title">用户信息</h3>
                    </div>
                    <form id="inputForm" class="form-horizontal" action="${ctx}/system/user/saveUser" method="post">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="fullName" class="col-sm-2 control-label">登陆名</label>
                                <div class="col-sm-10">
                                    <input type="text" id="loginName" name="loginName" class="form-control" placeholder="请输入登录名" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="name" class="col-sm-2 control-label">用户名</label>
                                <div class="col-sm-10">
                                    <input type="text" id="name" name="name"  class="form-control" placeholder="请输入用户名" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="name" class="col-sm-2 control-label">邮箱</label>
                                <div class="col-sm-10">
                                    <input type="text" id="email" name="email" placeholder="请输入邮箱" class="form-control"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="mobile" class="col-sm-2 control-label">手机号</label>
                                <div class="col-sm-10">
                                    <input type="text" id="mobile" name="mobile" placeholder="请输入手机号" class="form-control"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="tel" class="col-sm-2 control-label">固定电话</label>
                                <div class="col-sm-10">
                                    <input type="text" id="tel" name="tel" placeholder="请输入电话" class="form-control"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="sex" class="col-sm-2 control-label">性别</label>
                                <div class="col-sm-10">
                                    <#list sexs as sex>
                                        <#if sex_index == 0>
                                            <label class="radio-inline"><input type="radio" name="sex" value="${sex}" checked="checked"/>${sex.sexName}</label>
                                        <#else>
                                            <label class="radio-inline"><input type="radio" name="sex" value="${sex}" />${sex.sexName}</label>
                                        </#if>
                                    </#list>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="status" class="col-sm-2 control-label">状态</label>
                                <div class="col-sm-10">
                                    <#list statuss as status>
                                        <#if status_index == 0>
                                            <label class="radio-inline"><input type="radio" name="status" value="${status}" checked="checked"/>${status.statusName}</label>
                                        <#else>
                                            <label class="radio-inline"><input type="radio" name="status" value="${status}" />${status.statusName}</label>
                                        </#if>
                                    </#list>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="roleIds" class="col-sm-2 control-label">所属角色</label>
                                <div class="col-sm-10">
                                    <#list roleList as role>
                                    <label class="checkbox-inline"><input type="checkbox" name="roleIds" value="${role.id}"/>${role.name}</label>
                                    </#list>
                                </div>
                            </div>
                            <div class="form-group generic">
                                <label for="pname" class="col-sm-2 control-label">所在机构:</label>
                                <div class="col-sm-10">
                                    <#if group??>
                                        <input type="text" id="pname" name="pname" class="form-control" value="${group.fullName}" disabled/>
                                        <input type="hidden" id="parentID" name="group.id" value="${group.id}"/>
                                    <#else>
                                        <input type="text" name="pname" class="form-control" value="" disabled/>
                                        <input type="hidden" name="parent.id" value="" disabled/>
                                    </#if>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="remark" class="col-sm-2 control-label">备注:</label>
                                <div class="col-xs-10">
                                    <textarea id="remark" name="description" class="form-control"></textarea>
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
    $('.select2').select2();
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
