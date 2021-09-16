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
                    <h3 class="box-title">资源信息</h3>
                </div>
                <div class="box-body">
                    <div class="callout callout-info">
                        <div>
                            <strong>1.资源类型说明：</strong>系统资源分为菜单资源和操作资源，菜单资源通常是模块资源，如用户管理，而操作资源通常对应菜单资源下面操作按钮，如添加，修改，删除用户操作等
                        </div>
                        <div>
                            <strong>2.资源权限说明：</strong>资源权限对应系统访问权限，如果为空说明该资源访问公开，对应写法为Shiro权限写法如：模块:菜单：资源（如用户管理为system:user:view
                            添加用户为system:user:add）
                        </div>
                    </div>
                    <form id="inputForm" class="form-horizontal" action="${ctx}/system/resource/save/" method="post">
                        <div class="form-group">
                            <label for="name" class="col-sm-2 control-label">资源名称</label>
                            <div class="col-sm-10">
                                <input type="text" id="name" name="name" class="form-control" maxlength="20" required value="${resource.name!}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="resourceType" class="col-sm-2 control-label">资源类型</label>
                            <div class="col-sm-10">
                                <#list resourceTypes as rt>
                                    <#if resource.resourceType == rt>
                                        <label class="radio-inline"><input type="radio" data-bind="checked:resType"
                                                                           name="resourceType" value="${rt}"
                                                                           checked="checked"/>${rt.typeName}</label>
                                    <#else>
                                        <label class="radio-inline"><input type="radio" data-bind="checked:resType"
                                                                           name="resourceType"
                                                                           value="${rt}"/>${rt.typeName}</label>
                                    </#if>
                                </#list>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="permission" class="col-sm-2 control-label">资源权限</label>
                            <div class="col-sm-10">
                                <input type="text" id="permission" name="permission" class="form-control"
                                       maxlength="20" value="${resource.permission!}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="url" class="col-sm-2 control-label">访问路径</label>
                            <div class="col-sm-10">
                                <input type="text" id="url" name="url" class="form-control" maxlength="20" data-bind="value: url" value="${resource.url!}"/>
                                <select class="form-control" data-bind="value: url">
                                    <option></option>
                                    <#list urlList as rt>
                                        <option value="${rt}">${rt}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="pname" class="col-sm-2 control-label">上级资源:</label>
                            <div class="col-sm-10">
                                <#if resource.parent??>
                                    <input type="text" id="pname" name="pname" class="form-control"
                                           value="${resource.parent.name}" disabled/>
                                    <input type="hidden" name="parent.id" class="input-large"
                                           value="${resource.parent.id}"/>
                                    <input type="hidden" id="parentID" name="parent.id" value="${parent.id}"/>
                                <#else>
                                    <input type="text" id="pname" name="pname" class="form-control" value="" disabled/>
                                    <input type="hidden" id="parentID" name="parent.id" value="" disabled/>
                                </#if>
                            </div>
                        </div>
                        <div class="form-group" data-bind="visible:show">
                            <label for="icon" class="col-sm-2 control-label">图标:</label>
                            <div class="col-xs-4">
                                <input type="text" name="icon" placeholder="请选择图标" class="form-control"
                                       data-bind="value: icon, click: showIcon" value="${resource.icon!}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="sn" class="col-sm-2 control-label">显示顺序:</label>
                            <div class="col-xs-4">
                                <input type="number" name="sn" placeholder="请输入显示顺序" class="form-control"
                                       value="${resource.sn!1}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="description" class="col-sm-2 control-label">说明:</label>
                            <div class="col-xs-10">
                                <textarea name="description" class="form-control" maxlength="200">${resource.description}</textarea>
                            </div>
                        </div>

                        <div class="box-footer">
                            <button type="submit" class="btn btn-primary"><i class="fa fa-check"></i> 提交</button>
                            <button type="reset" class="btn btn-danger">重置</button>
                            <input type="hidden" name="id" value="${resource.id}"/>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</section>
<script>
    $('#inputForm').ajaxForm({
        dataType: 'json',
        success: function (data) {
            if (data.messageType === "SUCCESS") {
                layer.alert("操作成功", function() {
                    parent.hideMyModal();
                });
            } else {
                layer.alert("操作失败【" + data.content + "】");
            }
        }
    });

    $(function() {
        let viewModel = {
            resType: ko.observable("MENU"),
            icon: ko.observable('${resource.icon!}'),
            url: ko.observable('')
        };
        viewModel.show = ko.dependentObservable(function () {
            if (viewModel.resType() === "MENU") {
                icon: viewModel.icon('${resource.icon!}');
                return true;
            } else {
                viewModel.icon('');
                return false;
            }
        }, viewModel);

        ko.applyBindings(viewModel);
    });
</script>
</body>
</html>
