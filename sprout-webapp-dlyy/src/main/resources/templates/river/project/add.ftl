<!DOCTYPE html>
<html lang="zh">
<head>
    <title>添加项目信息</title>
    <#include "../../common/head.ftl"/>
    <#include "../../common/form.ftl"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.min.css" crossorigin="anonymous">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css" crossorigin="anonymous">
    <link href="${ctx}/res/lib/bootstrap-fileinput/css/fileinput.css" media="all" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/res/lib/bootstrap-fileinput/themes/explorer-fas/theme.css" media="all" rel="stylesheet" type="text/css"/>
    <script src="${ctx}/res/lib/bootstrap-fileinput/js/fileinput.js" type="text/javascript"></script>
    <script src="${ctx}/res/lib/bootstrap-fileinput/js/locales/zh.js" type="text/javascript"></script>
    <script src="${ctx}/res/lib/bootstrap-fileinput/themes/gly/theme.js" type="text/javascript"></script>
    <script src="${ctx}/res/lib/bootstrap-fileinput/themes/fas/theme.js" type="text/javascript"></script>
    <script src="${ctx}/res/lib/bootstrap-fileinput/themes/explorer-fas/theme.js" type="text/javascript"></script>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content">
    <div class="row">
        <div class="col-xs-12">
                <div class="box box-info">
                    <div class="box-header with-border">
                        <h3 class="box-title">项目信息</h3>
                    </div>
                    <form id="inputForm" class="form-horizontal" action="${ctx}/river/project/saveProject" method="post" enctype="multipart/form-data">
                        <div class="box-body">
                            <div class="form-group">
                                <label for="landType" class="col-sm-2 control-label">占用地类</label>
                                <div class="col-sm-10">
                                    <select class="form-control" name="landType">
                                        <option value="占用村庄">占用村庄</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="fixType" class="col-sm-2 control-label">整改方式</label>
                                <div class="col-sm-10">
                                    <select class="form-control" name="fixType">
                                        <option value="无">无</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="name" class="col-sm-2 control-label">项目名称</label>
                                <div class="col-sm-10">
                                    <input type="text" id="name" name="name" class="form-control" placeholder="请输入项目名称" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="department" class="col-sm-2 control-label">用地单位</label>
                                <div class="col-sm-10">
                                    <input type="text" id="department" name="department" class="form-control" placeholder="请输入用地单位"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="tel" class="col-sm-2 control-label">联系方式</label>
                                <div class="col-sm-10">
                                    <input type="text" id="tel" name="tel" class="form-control" placeholder="请输入联系方式"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="x" class="col-sm-2 control-label">经/纬度</label>
                                <div class="col-sm-10">
                                    <div class="col-xs-3">
                                        <input type="text" id="x" name="x" class="form-control" placeholder="请输入经度" required readonly value="${x}"/>
                                    </div>
                                    <div class="col-xs-3">
                                        <input type="text" id="y" name="y" placeholder="请输入维度" class="form-control" required readonly value="${y}"/>
                                    </div>

                                </div>
                            </div>
                            <div class="form-group">
                                <label for="desp" class="col-sm-2 control-label">项目描述</label>
                                <div class="col-sm-10">
                                    <textarea type="text" id="desp" name="desp" placeholder="请输入项目描述" class="form-control"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="file" class="col-sm-2 control-label">上传图片</label>
                                <div class="col-sm-10">
                                    <div class="file-loading">
                                        <input id="file" class="file"  name="file" type="file" multiple data-theme="fas">
                                    </div>
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
    $("#file").fileinput({
        showUpload : false,
        showRemove : false,
        language : 'zh',
        allowedPreviewTypes : [ 'image' ],
        allowedFileExtensions : [ 'jpg', 'png', 'gif','jpeg' ]
    });
    $('#inputForm').ajaxForm({
        dataType : 'json',
        success : function(data) {
            if (data != null) {
                layer.alert("操作成功", function() {
                    parent.addMarker(data);
                    parent.hideMyModal();
                });
            } else {
                layer.alert("操作失败!");
            }
        }
    });
</script>
</body>
</html>
