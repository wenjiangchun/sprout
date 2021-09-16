<!DOCTYPE html>
<html>
<head>
    <#include "../../common/head.ftl"/>
    <#include "../../common/form.ftl"/>
    <#include "../../common/ztree.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content">
    <div class="row">
        <div class="col-xs-12">
                <div class="box box-info">
                    <div class="box-header with-border">
                        <h3 class="box-title">角色授权</h3>
                    </div>
                    <form id="inputForm" class="form-horizontal" action="" method="post">
                        <div class="box-body">
                            <ul id="resTree" class="ztree"></ul>
                            <div>
                                <#if role.resources??>
                                    <#list role.resources as res>
                                         <input type="hidden" class='res-id' value="${res.id}">
                                   </#list>
                                </#if>
                            </div>
                        </div>
                        <div class="box-footer">
                            <button type="button" class="btn btn-primary" id="submit_btn"><i class="fa fa-check"></i> 提交</button>
                            <button type="reset" class="btn btn-danger">重置</button>
                        </div>
                    </form>
                </div>
        </div>
    </div>
</section>
<script>
    let tree;
    function initResourceTree() {
        $.ajax({
            method : "post",
            url : "${ctx}/system/resource/getResourcesTree",
            dataType : "json",
            success : function(data) {
                let setting = {data:{
                        simpleData:{
                            enable:true,
                            idKey:"id",
                            pIdKey:"parentId",
                            rootPId:null
                        },
                        key:{
                            name:"name"
                        }
                    }, callback: {
                        onClick:onClick
                    }, check: {
                        enable: true
                    }
                };
                $.fn.zTree.init($("#resTree"), setting, data);
                tree = $.fn.zTree.getZTreeObj("resTree");
                tree.expandAll(true);
                $('.res-id').each(function(i, el) {
                    const node = tree.getNodeByParam("id", $(el).val(), null);
                    tree.checkNode(node, true, true);
                });
            }
        });
    }

    function onClick(event, treeId, treeNode, clickFlag) {
        tree.expandNode(treeNode, true, false, true);
    }

    $(function() {
        initResourceTree();
        $('#submit_btn').click(function() {
            const nodes = tree.getCheckedNodes(true);
            let resourceIds = [];
            for (let i = 0; i < nodes.length; i++) {
                if (nodes[i].id !== 0) {
                    resourceIds.push(nodes[i].id);
                }
            }
            $.post({
                url: '${ctx}/system/role/saveResources/',
                data: {
                    'roleId' : '${role.id}',
                    'resourceIds': resourceIds
                },
                dataType:"json",
                success: function(data) {
                    if (data.messageType === "SUCCESS") {
                        layer.alert("操作成功", function() {
                            parent.hideMyModal();
                        });
                    } else {
                        layer.alert("操作失败【"+data.content+"】");
                    }
                }
            });
        });
    });
</script>
</body>
</html>
