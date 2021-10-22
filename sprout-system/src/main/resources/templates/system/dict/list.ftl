<!DOCTYPE html>
<html>
<head>
<title>字典管理</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
	<#include "../../common/ztree.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">系统管理</a></li>
				<li class="active">字典管理</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-3">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">字典树</h3>
						</div>
						<div class="box-body">
							<ul id="dictTree" class="ztree"></ul>
						</div>
					</div>
				</div>
				<div class="col-xs-9">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">字典列表<span data-bind="text: dictName"></span></h3>
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
								<tr>
									<th sName="id">编号</th>
									<th sName="name">字典名称</th>
									<th sName="code">字典代码</th>
									<th sName="parent" columnRender="formatParentCode">字典分类</th>
									<th sName="status" columnRender="formatStatus">字典状态</th>
									<th sName="sn">顺序号</th>
									<th sName="operate" columnRender="formatOperator">操作</th>
								</tr>
								</tr>
								</thead>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
					<!-- /.box -->
					<a href="#" class="btn btn-primary" data-bind='click: add'><i class="fa fa-plus-circle"></i>  添加字典</a>
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
			dictName: ko.observable(''),
			parentId: ko.observable('${parentId!}'),
			initTable: function() {
				const options = {
					divId : "contentTable",
					url : "${ctx}/system/dict/search",
					columns:[{
						'data':'id',
						'orderable': false
					},{
						'data':'name',
						'orderable': false
					},{
						'data':'code',
						'orderable': false
					},{
						'data':function(row, type, val, meta) {
							if (row.parent != null) {
								return row.parent.name;
							}
							return "";
						},
						'orderable': false
					},{
						'data':function(row, type, val, meta) {
							if (!row.enabled) {
								return "<span class='label label-danger'>禁用</span>";
							}
							return "<span class='label label-success'>启用</span>";
						}
					},{
						'data':'sn'
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
				let url = "${ctx}/system/dict/add";
				if (this.parentId() != null && this.parentId() !== "") {
					url += "?parentId=" + this.parentId();
				}
				top.showMyModel(url,'添加字典', '900px', '60%', callBackAction);
			},
			reset: function() {
				$(".datatable_query").val('');
			},
			query: function() {
				refreshTable();
			},
			edit: function(id) {
				let url = "${ctx}/system/dict/edit/" + id;
				top.showMyModel(url,'编辑字典', '900px', '60%', callBackAction);
			},
			delete: function(id) {
				if (id == null || id === "") {
					alert("ID不能为空");
				} else {
					layer.confirm('删除字典会同时删除该字典分类下的字典,确认删除?', {
						btn: ['确认','取消'] //按钮
					}, function(){
						const ids = [id];
						$.post({
							url:'${ctx}/system/dict/delete/'+ids,
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
		initGroupTree();
		viewModel.initTable();
	});

	function initGroupTree() {
		$.ajax({
			method : "post",
			url : "${ctx}/system/dict/getDictionaryTree",
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
				$.fn.zTree.init($("#dictTree"), setting, data);

				tree = $.fn.zTree.getZTreeObj("dictTree");
				let parentId = viewModel.parentId();
				if (parentId != null && parentId !== "") {
					let node = tree.getNodeByParam("id",parentId);
					if(!node.isParent){
						node = node.getParentNode();
					}
					tree.selectNode(node,false);
					tree.expandNode(node, true, false, true);
				} else {
					let node = tree.getNodeByParam("name","字典树");
					tree.expandNode(node, true, false, true);
				}
			}
		});
	}

	function onClick(event, treeId, treeNode, clickFlag) {
		tree.expandNode(treeNode, true, false, true);
		let dictName = treeNode.name != null && treeNode.name !== '字典树' ? '(所属字典: ' + treeNode.name + ')': '';
		viewModel.parentId(treeNode.id);
		viewModel.dictName(dictName);
		refreshTable();
	}

	function callBackAction(data) {
		refreshTable();
		initGroupTree();
	}
</script>
</html>
