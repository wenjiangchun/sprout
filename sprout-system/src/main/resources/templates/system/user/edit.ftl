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
        <li><a href="${ctx}/system/user/view">用户管理</a></li>
        <li class="active">编辑用户</li>
    </ol>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box box-default">
                <div class="box-header with-border">
                    <h3 class="box-title">用户信息</h3>
                    <div class="pull-right">
                        <button class="btn btn-box-tool" onclick="window.history.go(-1)"><i class="fa fa-reply"></i> </button>
                    </div>
                </div>
                <form id="inputForm" class="form-horizontal" action="${ctx}/system/user/saveUser" method="post">
                    <div class="box-body">
                        <div class="form-group generic">
                            <label for="pname" class="col-sm-2 control-label">所在机构</label>
                            <div class="col-sm-10">
                                <#if user.group??>
                                    <input type="text" id="pname" name="pname" class="form-control"
                                           value="${user.group.fullName}" disabled/>
                                    <input type="hidden" id="parentID" name="group.id" value="${user.group.id}"/>
                                <#else>
                                    <input type="text" name="pname" class="form-control" value="" disabled/>
                                    <input type="hidden" name="parent.id" value="" disabled/>
                                </#if>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="loginName" class="col-sm-2 control-label">登陆名</label>
                            <div class="col-sm-4">
                                <input type="text" id="loginName" name="loginName" class="form-control"
                                       placeholder="请输入登录名" value="${user.loginName!}" required readonly/>
                            </div>
                            <label for="name" class="col-sm-2 control-label">用户名</label>
                            <div class="col-sm-4">
                                <input type="text" id="name" name="name" class="form-control" placeholder="请输入用户名"
                                       value="${user.name!}" required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="email" class="col-sm-2 control-label">邮箱</label>
                            <div class="col-sm-4">
                                <input type="text" id="email" name="email" placeholder="请输入邮箱" class="form-control"
                                       value="${user.email!}"/>
                            </div>
                            <label for="mobile" class="col-sm-2 control-label">手机号</label>
                            <div class="col-sm-4">
                                <input type="text" id="mobile" name="mobile" placeholder="请输入手机号" class="form-control"
                                       value="${user.mobile!}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="status" class="col-sm-2 control-label">状态</label>
                            <div class="col-sm-4">
                                <#list statuss as status>
                                    <#if user.status == status>
                                        <label class="radio-inline"><input type="radio" class="minimal" name="status" value="${status}" checked="checked"/> ${status.statusName}
                                        </label>
                                    <#else>
                                        <label class="radio-inline"><input type="radio" class="minimal" name="status" value="${status}"/> ${status.statusName}
                                        </label>
                                    </#if>
                                </#list>
                            </div>
                            <label for="sex" class="col-sm-2 control-label">性别</label>
                            <div class="col-sm-4">
                                <#list sexs as sex>
                                    <#if sex_index == 0>
                                        <label class="radio-inline"><input type="radio" class="minimal" name="sex" value="${sex}"
                                                                           checked="checked"/> ${sex.sexName}</label>
                                    <#else>
                                        <label class="radio-inline"><input type="radio" class="minimal" name="sex"
                                                                           value="${sex}"/> ${sex.sexName}</label>
                                    </#if>
                                </#list>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="entryDay" class="col-sm-2 control-label">入职日期</label>
                            <div class="col-sm-4">
                                <input type="text" id="entryDay" name="entryDay" placeholder="请选择入职日期" class="form-control" value="${user.entryDay!}" required/>
                            </div>
                            <label for="workStartYear" class="col-sm-2 control-label">参加工作年份</label>
                            <div class="col-sm-4">
                                <input type="number" id="workStartYear" name="workStartYear" placeholder="请选择参加工作年份" class="form-control" value="{user.workStartYear!}" max="2021" min="1950" required/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="workNum" class="col-sm-2 control-label">工号</label>
                            <div class="col-sm-4">
                                <input type="text" id="workNum" name="workNum" placeholder="请输入工号" class="form-control" value="${user.workNum!}" required/>
                            </div>
                            <label for="tel" class="col-sm-2 control-label">固定电话</label>
                            <div class="col-sm-4">
                                <input type="text" id="tel" name="tel" placeholder="请输入电话" class="form-control"
                                       value="${user.tel!}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="roleIds" class="col-sm-2 control-label">所属角色</label>
                            <div class="col-sm-10">
                                <#list roleList as role>
                                    <#if user.roles? seq_contains(role)>
                                        <label class="checkbox-inline"><input type="checkbox" class="minimal" name="roleIds" value="${role.id}" checked/> ${role.name}</label>
                                    <#else>
                                        <label class="checkbox-inline"><input type="checkbox" class="minimal" name="roleIds" value="${role.id}"/> ${role.name}</label>
                                    </#if>
                                </#list>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="description" class="col-sm-2 control-label">描述:</label>
                            <div class="col-xs-10">
                                <textarea id="description" name="description"
                                          class="form-control">${user.description!}</textarea>
                            </div>
                        </div>
                    </div>
                    <div class="box-footer">
                        <button type="submit" class="btn btn-primary pull-right"><i class="fa fa-check"></i> 提交</button>
                        <button type="reset" class="btn btn-default">重置</button>
                        <input type="hidden" name="id" value="${user.id }">
                    </div>
                </form>
            </div>
        </div>
    </div>
</section>
<script>
    $('.select2').select2();
    $('#inputForm').ajaxForm({
        dataType: 'json',
        success: function (data) {
            if (data.flag) {
                layer.alert(data.content, function(index) {
                    if ($('input[name=pname]').val() !== '') {
                        url += '?groupId=' + $('#parentID').val();
                    }
                    window.location.href = url;
                });
            } else {
                layer.alert(data.content);
            }
        }
    });

    $('#entryDay').datepicker({
        autoclose: true,
        language: 'zh-CN',
        calendarWeeks:true,
        todayHighlight: true,
        todayBtn: "linked"
    }).on('hide', function(e) {
        console.log(e.date);
    })
</script>
</body>
</html>
