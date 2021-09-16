<!DOCTYPE html>
<html lang="zh">
<head>
    <title>查看项目信息</title>
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
                        <div class="box-body">
                            <form id="inputForm" class="form-horizontal" action="${ctx}/river/project/saveProject" method="post">
                                <div class="form-group">
                                    <label for="landType" class="col-sm-2 control-label">占用地类</label>
                                    <div class="col-sm-10">
                                        <select class="form-control" name="landType" readonly>
                                            <option value="占用村庄">占用村庄</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="fixType" class="col-sm-2 control-label">整改方式</label>
                                    <div class="col-sm-10">
                                        <select class="form-control" name="fixType" readonly>
                                            <option value="无">无</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                <label for="name" class="col-sm-2 control-label">项目名称</label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" value="${project.name!}" readonly/>
                                </div>
                            </div>
                                <div class="form-group">
                                    <label for="department" class="col-sm-2 control-label">用地单位</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="department" name="department" class="form-control" placeholder="请输入用地单位" value="${project.department!}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="tel" class="col-sm-2 control-label">联系方式</label>
                                    <div class="col-sm-10">
                                        <input type="text" id="tel" name="tel" class="form-control" placeholder="请输入联系方式" value="${project.tel!}"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="x" class="col-sm-2 control-label">经/纬度</label>
                                    <div class="col-sm-10">
                                        <div class="col-xs-3">
                                            <input type="text" id="x" name="x" class="form-control" placeholder="请输入经度" required readonly value="${project.x!}"/>
                                        </div>
                                        <div class="col-xs-3">
                                            <input type="text" id="y" name="y" placeholder="请输入维度" class="form-control" required readonly value="${project.y!}"/>
                                        </div>

                                    </div>
                                </div>
                            <div class="form-group">
                                <label for="desp" class="col-sm-2 control-label">项目描述</label>
                                <div class="col-sm-10">
                                    <textarea type="text" class="form-control" readonly>${project.desp!}</textarea>
                                </div>
                            </div>
                                <div class="form-group">
                                    <label for="file" class="col-sm-2 control-label">图片查看</label>
                                    <div class="col-sm-10">
                                        <div class="file-loading">
                                            <input id="file" class="file"  name="file" type="file" multiple data-theme="fas"/>
                                        </div>
                                    </div>
                                </div>
                                <ul id="fileList" style="display: none">
                                    <#list project.projectFiles as f>
                                        <li name="${f.name}" size="${f.size}">${f.path!}</li>
                                    </#list>
                                </ul>
                                <div class="box-footer">
                                    <button type="button" class="btn btn-primary" id="deleteBtn"><i class="fa fa-trash"></i> 删除项目</button>
                                </div>
                            </form>
                        </div>
                </div>
        </div>
    </div>
</section>
<script>
    const pre = '${ctx}/file/previewPath?path='
    let initialPreview = [];
    let initialPreviewConfig = [];
    $('#fileList > li').each(function(idx,el){
        initialPreview.push(pre + $(el).text());
        initialPreviewConfig.push({
            caption: $(el).attr('name'),
            downloadUrl: pre + $(el).text(),
            size: parseFloat($(el).attr('size'))*1024,
            width: "120px",
            key: (idx + 1)
        });
    });
    $("#file").fileinput({
        initialPreview: initialPreview,
        showUpload : false,
        showRemove : false,
        language : 'zh',
        initialPreviewAsData: true,
        initialPreviewConfig: initialPreviewConfig,
        initialPreviewShowDelete: false,
        showBrowse: false,
        showClose: false,
        showCaption: false
    });

    $('#deleteBtn').click(function() {
        layer.confirm('确认删除项目信息？', {
            btn: ['确认','取消'] //按钮
        }, function(){
            parent.removeMarker(${project.id});
            parent.hideMyModal();
        }, function(){

        })
    });
</script>
</body>
</html>
