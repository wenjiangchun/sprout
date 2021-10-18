<!DOCTYPE html>
<html lang="zh">
<head>
<title>DockerHost管理</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">运维管理</a></li>
				<li class="active">DockerHost管理</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">Docker主机列表</h3>
							<div class="box-tools">
							</div>
						</div>
						<div class="box-body">
							<table id="contentTable" class="table table-bordered table-striped table-hover">
								<thead>
									<tr>
										<th>编号</th>
										<th>名称</th>
										<th>IP</th>
										<th>端口</th>
										<th>描述</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
					<button class="btn btn-primary" data-bind='click: add'><i class="fa fa-plus"></i>  添加Docker主机</button>
				</div>
			</div>
		</section>
</body>
<script type="text/javascript">
	let viewModel;
	$(document).ready(function() {
		viewModel = {
			dictName: ko.observable(''),
			parentId: ko.observable('${parentId!}'),
			initTable: function() {
				const options = {
					divId : "contentTable",
					url : "${ctx}/devops/docker/search",
					columns:[{
						'data':'id'
					},{
						'data':'name',
						'orderable': false
					},{
						'data':'ip',
						'orderable': false
					},{
						'data':'port',
						'orderable': false
					},{
						'data':'description',
						'orderable': false
					},{
						'data':function(row, type, val, meta) {
							let html = "";
							html += "<button class='btn btn-default btn-xs' onclick='viewModel.edit(" + row.id + ")' title='编辑'> <i class='fa fa-edit fa-lg'></i> </button> ";
							html += "<button class='btn btn-default btn-xs' onclick='viewModel.delete(" + row.id + ")' title='删除'> <i class='fa fa-trash-o fa-lg'></i> </button> ";
							html += "<button class='btn btn-default btn-xs' onclick='viewModel.manager(" + row.id + ")' title='管理'> <i class='fa fa-gear fa-lg'></i> </button> ";
							return html;
						},
						'orderable': false
					}]
				};
				createTable(options);
			},
			add: function() {
				let url = "${ctx}/devops/docker/add";
				top.showMyModel(url,'添加Docker主机', '70%', '50%', callBackAction);
			},
			reset: function() {
				$(".datatable_query").val('');
			},
			query: function() {
				refreshTable();
			},
			edit: function(id) {
				let url = "${ctx}/devops/docker/edit/" + id;
				top.showMyModel(url,'编辑Docker主机', '70%', '70%', callBackAction);
			},
			manager: function(id) {
				let url = "${ctx}/devops/docker/manager/" + id;
				//top.showMyModel(url,'Docker主机管理', '90%', '90%', callBackAction);
				window.location.href=url;
			},
			delete: function(id) {
				if (id == null || id === "") {
					layer.alert("ID不能为空");
				} else {
					top.layer.confirm('确认删除?', {
						btn: ['确认','取消'] //按钮
					}, function(){
						const ids = [id];
						$.post({
							url:'${ctx}/devops/docker/delete/'+ids,
							success:function(data) {
								if (data.flag) {
									top.layer.alert(data.content, function(index) {
										refreshTable();
										top.layer.close(index);
									});
								} else {
									top.layer.alert(data.content);
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
