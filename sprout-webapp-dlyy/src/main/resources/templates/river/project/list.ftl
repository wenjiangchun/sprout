<!DOCTYPE html>
<html lang="zh">
<head>
<title>项目管理</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
	<#include "../../common/ztree.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">系统管理</a></li>
				<li class="active">项目管理</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">项目列表</h3>
							<div class="box-tools">
							</div>
								<form class="form-inline">
									<input type="hidden" class="datatable_query" name="group.id" data-bind="value: projectName"/>
									<div class="box-body">
										<div class="form-group">
											<label for="projectName">项目名称</label>
											<input type="text" id="projectName" name="projectName_like" class="datatable_query form-control">
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
									<th>编号</th>
									<th>项目名称</th>
									<th>用地单位</th>
									<th>联系方式</th>
									<th>经度/维度</th>
									<th>项目描述</th>
									<th>操作</th>
								</tr>
								</thead>
							</table>
						</div>
					</div>
					<#--<a href="#" class="btn btn-primary" data-bind='click: add'><i class="fa fa-plus-circle"></i>  添加项目</a>-->
				</div>
			</div>
		</section>
</body>
<script type="text/javascript">
	let viewModel;
	$(document).ready(function() {
		viewModel = {
			projectName: ko.observable(''),
			initTable: function() {
				const options = {
					divId : "contentTable",
					url : "${ctx}/river/project/search",
					columns:[{
						'data':'id'
					},{
						'data':'name',
						'sortable':false
					},{
						'data':function(row, type, val, meta) {
							if (row != null) {
								return row.x + '/' + row.y;
							}
							return '';
						},
						'sortable':false
					},{
						'data':'department',
						'sortable':false
					},{
						'data':'tel',
						'sortable':false
					},{
						'data':'desp',
						'sortable':false
					},{
						'data':function(row, type, val, meta) {
							let html = "";
							html += "<a href='javascript:void(0)' onclick='viewModel.edit(" + row.id + ")' title='编辑'> <i class='fa fa-edit fa-lg'></i> </a> | ";
							html += "<a href='javascript:void(0)' onclick='viewModel.delete(" + row.id + ")' title='删除'> <i class='fa fa-trash-o fa-lg'></i> </a>";
							return html;
						},
						'sortable':false
					}]
				};
				createTable(options);
			},
			add: function() {
				let url = "${ctx}/river/project/add";
				showMyModel(url,'添加用户', '800px', '60%', callBackAction);
			},
			reset: function() {
				$(".datatable_query").val('');
			},
			query: function() {
				refreshTable();
			},
			edit: function(id) {
				let url = "${ctx}/river/project/edit/" + id;
				showMyModel(url,'编辑项目', '800px', '60%', callBackAction);
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
							url:'${ctx}/river/project/delete/'+ids,
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
	});
	function callBackAction(data) {
		refreshTable();
	}
</script>
</html>
