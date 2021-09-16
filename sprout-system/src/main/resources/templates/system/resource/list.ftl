<!DOCTYPE html>
<html>
<head>
<title>资源管理</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
	<#include "../../common/ztree.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">系统管理</a></li>
				<li class="active">资源管理</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-3">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">资源树</h3>
						</div>
						<div class="box-body">
							<ul id="resTree" class="ztree"></ul>
						</div>
					</div>
				</div>
				<div class="col-xs-9">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">资源列表<span data-bind="text: resName"></span></h3>
							<div class="box-tools">
								<#--<a href="#" id="refreshRepository" class="btn btn-default"><i class="fa fa-repeat"></i>  刷新</a>
								<a href="#" id="add_btn" class="btn btn-info"><i class="fa fa-plus-circle"></i>  添加</a>-->
							</div>
								<form class="form-inline">
									<input type="hidden" class="datatable_query" name="parent.id" data-bind="value: parentId"/>
									<div class="box-body">
										<div class="form-group">
											<label for="name_like">字典名称</label>
											<input type="text" name="name_like" class="datatable_query form-control">
										</div>
										<button type="button" class="btn btn-sm btn-primary" data-bind='click: query' style="margin-left:5px;">
											<i class="fa fa-search"></i> 查询
										</button>
										<button type="button" class="btn btn-sm btn-danger" data-bind='click: reset' style="margin-left:10px;">清空</button>
									</div>
								</form>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table id="contentTable" class="table table-bordered table-striped table-hover">
								<thead>
								<tr>
									<th sName="id">编号</th>
									<th sName="name">资源名称</th>
									<th sName="permission">权限定义</th>
									<th sName="url">资源路径</th>
									<th sName="icon" columnRender="formatIcon">图标</th>
									<th sName="resourceType" columnRender="formatType">资源类型</th>
									<th sName="sn">顺序号</th>
									<th sName="operate" columnRender="formatOperator">操作</th>
								</tr>
								</thead>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
					<!-- /.box -->
					<a href="#" class="btn btn-primary" data-bind='click: add'><i class="fa fa-plus-circle"></i>  添加资源</a>
				</div>
				<!-- /.col -->
			</div>
			<!-- /.row -->
		</section>
</body>
<script type="text/javascript">
	let viewModel;
	let tree;
	$(document).ready(function() {
		viewModel = {
			resName: ko.observable(''),
			parentId: ko.observable('${parentId!}'),
			add: function() {
				let url = "${ctx}/system/resource/add";
				if (this.parentId() != null && this.parentId() !== "") {
					url += "?parentId=" + this.parentId();
				}
				showMyModel(url,'添加资源', '900px', '60%', callBackAction);
			},
			reset: function() {
				$(".datatable_query").val('');
			},
			query: function() {
				refreshTable();
			},
			edit: function(id) {
				let url = "${ctx}/system/resource/edit/" + id;
				showMyModel(url,'编辑资源', '900px', '60%', callBackAction);
			},
			delete: function(id) {
				if (id == null || id === "") {
					alert("ID不能为空");
				} else {
					layer.confirm('删除资源会同时删除该资源分类下的资源,确认删除?', {
						btn: ['确认','取消'] //按钮
					}, function(){
						const ids = [id];
						$.post({
							url:'${ctx}/system/resource/delete/'+ids,
							success:function(data) {
								if (data.messageType == 'SUCCESS') {
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
		initGroupTree();
		initDataTable();
	});

	function initGroupTree() {
		$.ajax({
			method : "post",
			url : "${ctx}/system/resource/getResourcesTree",
			dataType : "json",
			success : function(data) {
				var setting = {data:{
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
					}};
				$.fn.zTree.init($("#resTree"), setting, data);

				tree = $.fn.zTree.getZTreeObj("resTree");
				let parentId = viewModel.parentId();
				if (parentId != null && parentId !== "") {
					let node = tree.getNodeByParam("id",parentId);
					if(!node.isParent){
						node = node.getParentNode();
					}
					tree.selectNode(node,false);
					tree.expandNode(node, true, false, true);
				} else {
					let node = tree.getNodeByParam("name","系统资源树");
					tree.expandNode(node, true, false, true);
				}
			}
		});
	}

	function onClick(event, treeId, treeNode, clickFlag) {
		tree.expandNode(treeNode, true, false, true);
		let dictName = treeNode.name != null && treeNode.name !== '系统资源树' ? '(所属资源: ' + treeNode.name + ')': '';
		viewModel.parentId(treeNode.id !==0 ? treeNode.id : '');
		viewModel.resName(dictName);
		refreshTable();
	}

	function initDataTable() {
		const options = {
			divId : "contentTable",
			url : "${ctx}/system/resource/search"
		};
		createTable(options);
	}

	function formatParentCode(data) {
		if (data.parent != null) {
			return data.parent.name;
		}
		return "";
	}

	function formatOperator(data) {
		let html = "";
		html += "<a href='javascript:void(0)' onclick='viewModel.edit(" + data.id + ")' title='编辑'> <i class='fa fa-edit fa-lg'></i> </a> | ";
		html += "<a href='javascript:void(0)' onclick='viewModel.delete(" + data.id + ")' title='删除'> <i class='fa fa-trash-o fa-lg'></i> </a>";
		return html;
	}

	function formatType(data) {
		if (data.resourceType === "MENU") {
			return "菜单资源";
		} else {
			return "操作资源";
		}
	}
	function formatIcon(data) {
		return '<i class="'+data.icon+'"></i>';
	}
	function callBackAction(data) {
		refreshTable();
		initGroupTree();
	}
</script>
</html>
