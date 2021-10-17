<!DOCTYPE html>
<html lang="zh">
<head>
<title>机构管理</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
	<#include "../../common/ztree.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">系统管理</a></li>
				<li class="active">机构管理</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-3">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">组织机构树</h3>
						</div>
						<div class="box-body">
							<ul id="groupTree" class="ztree"></ul>
						</div>
						<!-- /.box-body -->
					</div>
				</div>
				<div class="col-xs-9">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">机构列表 <span data-bind="text: groupName"></span></h3>
							<div class="box-tools">
							</div>
								<form class="form-inline">
									<input type="hidden" class="datatable_query" name="parent.id" data-bind="value: groupId"/>
									<div class="box-body">
										<div class="form-group">
											<label for="fullName">机构名称</label>
											<input type="text" id="fullName" name="fullName_like" class="datatable_query form-control">
										</div>
										<button type="button" class="btn btn-sm btn-primary" onclick="refreshTable();" style="margin-left:5px;">
											<i class="fa fa-search"></i> 查询
										</button>
										<button type="button" class="btn btn-sm btn-danger" onclick="" style="margin-left:10px;">清空</button>
									</div>
								</form>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table id="contentTable" class="table table-bordered table-striped table-hover">
								<thead>
								<tr>
									<th>编号</th>
									<th>机构全称</th>
									<th>机构简称</th>
									<th>机构类型</th>
									<th>机构代码</th>
									<th>固定电话</th>
									<th>操作</th>
								</tr>
								</thead>
							</table>
						</div>
					</div>
					<a href="#" class="btn btn-primary" data-bind='click: add'><i class="fa fa-plus-circle"></i>  添加机构</a>
				</div>
			</div>
		</section>
</body>
<script type="text/javascript">
	let viewModel;
	let tree;
	$(document).ready(function() {
		viewModel = {
			groupName: ko.observable(''),
			groupId: ko.observable('${parentId!}'),
			initTable: function() {
				const options = {
					divId : "contentTable",
					url : "${ctx}/system/group/search",
					columns:[{
						'data':'id'
					},{
						'data':'fullName'
					},{
						'data':'name'
					},{
						'data':'groupType.name',
						'orderable': false
					},{
						'data':'code'
					},{
						'data':'tel',
						'orderable': false
					},{
						'data':function(row, type, val, meta) {
							var html = "";
							html += "<a href='javascript:void(0)' onclick='viewModel.edit(" + row.id + ")' title='编辑'> <i class='fa fa-edit fa-lg'></i> </a> | ";
							html += "<a href='javascript:void(0)' onclick='viewModel.delete(\"" + row.id + "\")' title='删除'> <i class='fa fa-trash-o fa-lg'></i> </a> ";
							/*	html += "<a href='javascript:void(0)' onclick='addRole(\"" + data.id + "\")' title=''> <i class='fa fa-tag fa-lg'></i> </a>";*/
							return html;
						},
						'orderable': false
					}]
				};
				createTable(options);
			},
			add: function() {
				let url = "${ctx}/system/group/add";
				if (this.groupId() != null && this.groupId() !== "") {
					url += "?parentId=" + this.groupId();
				}
				showMyModel(url,'添加机构', '800px', '60%', callBackAction);
		    },
			edit: function(id) {
				let url = "${ctx}/system/group/edit/" + id;
				showMyModel(url,'编辑机构', '800px', '60%', callBackAction);
			},
			delete: function(id) {
				if (id == null || id === "") {
					console.log("ID不能为空");
				} else {
					layer.confirm('确认删除?', {
						btn: ['确认','取消'] //按钮
					}, function(){
						const ids = [id];
						$.post({
							url:'${ctx}/system/group/delete/'+ids,
							success:function(data) {
								if (data.messageType === 'SUCCESS') {
									layer.alert('删除成功');
									callBackAction(data);
								} else {
									layer.alert('删除失败:' + data.content);
								}
							}
						});
					}, function(){
					});
				}
			}
		};
		ko.applyBindings(viewModel);
		viewModel.initTable();
		initGroupTree();
	});

	function initGroupTree() {
		$.ajax({
			method : "post",
			url : "${ctx}/system/group/getTopGroups",
			dataType : "json",
			success : function(data) {
				var setting = {data:{
						simpleData:{
							enable:true,
							idKey:"id",
							pIdKey:"pid",
							rootPId:null
						},
						key:{
							name:"name"
						}
					}, callback: {
						onClick:onClick
					}};
				$.fn.zTree.init($("#groupTree"), setting, data);

				tree = $.fn.zTree.getZTreeObj("groupTree");
				var parentId = viewModel.groupId();
				if (parentId != null && parentId != "") {
					var node = tree.getNodeByParam("id",parentId);
					if(!node.isParent){
						node = node.getParentNode();
					}
					tree.selectNode(node,false);
					tree.expandNode(node, true, false, true);
				} else {
					var node = tree.getNodeByParam("name","组织机构树");
					tree.expandNode(node, true, false, true);
				}
			}
		});
	}

	function onClick(event, treeId, treeNode, clickFlag) {
		//currentViewModel.id = treeNode.id;
		tree.expandNode(treeNode, true, false, true);
		let name = treeNode.name!= null && treeNode.name !== '组织机构树' ? '(' + treeNode.name + ')': '';
		viewModel.groupId(treeNode.id);
		viewModel.groupName(name);
		refreshTable();
	}

	function removeTreeNodeByNodeId(id){
		var treeObj = $.fn.zTree.getZTreeObj("groupTree");
		if (id != null && id != "") {
			var node = treeObj.getNodeByParam("id", id);
			if(node != null){
				treeObj.removeNode(node);
			}
		}
	}

	function callBackAction(data) {
		refreshTable();
		initGroupTree();
	}

</script>
</html>
