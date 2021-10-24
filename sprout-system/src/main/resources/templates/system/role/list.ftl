<!DOCTYPE html>
<html lang="zh">
<head>
<title>角色管理</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">系统管理</a></li>
				<li class="active">角色管理</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">角色列表</h3>
							<div class="box-tools">
							</div>
								<form class="form-inline">
									<div class="box-body">
										<div class="form-group">
											<label for="name_like" class="control-label">角色名称 </label>
											<input type="text" name="name_like" class="datatable_query form-control">
										</div>
										<button type="button" class="btn btn-default" data-bind='click: query'>
											<i class="fa fa-search"></i> 查询
										</button>
										<button type="button" class="btn btn-default" data-bind='click: reset'><i class="fa fa-circle-o-notch"></i> 重置</button>
									</div>
								</form>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table id="contentTable" class="table table-bordered table-striped table-hover">
								<thead>
								<tr>
								<tr>
									<th>编号</th>
									<th>角色名称</th>
									<th>角色代码</th>
									<th>状态</th>
									<th>角色说明</th>
									<th>操作</th>
								</tr>
								</tr>
								</thead>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
					<!-- /.box -->
					<a href="#" class="btn btn-primary" data-bind='click: add'><i class="fa fa-plus-circle"></i>  添加角色</a>
				</div>
				<!-- /.col -->
			</div>
			<!-- /.row -->
		</section>
</body>
<script type="text/javascript">
	let viewModel;
	$(document).ready(function() {
		viewModel = {
			initTable: function() {
				const options = {
					divId : "contentTable",
					url : "${ctx}/system/role/search",
					columns:[{
						'data':'id'
					},{
						'data':'name',
						'orderable': false
					},{
						'data':'code',
						'orderable': false
					},{
						'data':function(row, type, val, meta) {
							if (row.status === "D") {
								return "<span class='label label-danger'>禁用</span>";
							}
							return "<span class='label label-success'>启用</span>";
						},
						'orderable': false
					},{
						'data':'description',
						'orderable': false
					},{
						'data':function(row, type, val, meta) {
							var html = "";
							html += "<button class='btn btn-default btn-xs' onclick='viewModel.edit(" + row.id + ")' title='编辑'> <i class='fa fa-edit fa-lg'></i> </button> ";
							html += "<button class='btn btn-default btn-xs' onclick='viewModel.delete(\"" + row.id + "\")' title='删除'> <i class='fa fa-trash-o fa-lg'></i> </button> ";
							html += "<button class='btn btn-default btn-xs' onclick='viewModel.addResource(" + row.id + ")' title='资源授权'> <i class='fa fa-database'></i> </button>";
							return html;
						},
						'orderable': false
					}]
				};
				createTable(options);
			},
			add: function() {
				window.location.href = "${ctx}/system/role/add";
				//top.showMyModel(url,'添加角色', '900px', '50%', callBackAction);
			},
			reset: function() {
				$(".datatable_query").val('');
			},
			query: function() {
				refreshTable();
			},
			edit: function(id) {
				let url = "${ctx}/system/role/edit/" + id;
				window.location.href = url;
				//top.showMyModel(url,'编辑角色', '900px', '50%', callBackAction);
			},
			delete: function(id) {
				if (id == null || id === "") {
					alert("ID不能为空");
				} else {
					layer.confirm('确认删除?', {
						btn: ['确认','取消'] //按钮
					}, function(){
						const ids = [id];
						$.post({
							url:'${ctx}/system/role/delete/'+ids,
							success:function(data) {
								if (data.flag) {
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
			},
			addResource: function(id) {
				window.location.href = "${ctx}/system/role/addResources/" + id;
				//top.showMyModel(url,'角色授权', '800px', '60%', callBackAction);
			}
		};
		ko.applyBindings(viewModel);
		viewModel.initTable();
	});

	function callBackAction(data) {
		refreshTable();
	}
</script>
</html>
