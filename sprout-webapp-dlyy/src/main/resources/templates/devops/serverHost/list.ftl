<!DOCTYPE html>
<html lang="zh">
<head>
<title>主机管理</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">运维管理</a></li>
				<li class="active">主机管理</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">主机列表</h3>
							<div class="box-tools">
							</div>
						</div>
						<div class="box-body">
							<table id="contentTable" class="table table-bordered table-striped table-hover">
								<thead>
									<tr>
										<th>编号</th>
										<th>主机名称</th>
										<th>操作系统</th>
										<th>主机IP</th>
										<th>管理端口</th>
										<th>用户名</th>
										<th>密码</th>
										<th>服务器状态</th>
										<th>描述</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
					<button class="btn btn-primary" data-bind='click: add'><i class="fa fa-plus"></i>  添加主机</button>
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
					url : "${ctx}/devops/serverHost/search",
					columns:[{
						'data':'id'
					},{
						'data':'name',
						'orderable': false
					},{
						'data':'os',
						'orderable': false
					},{
						'data':'ip',
						'orderable': false
					},{
						'data':'port',
						'orderable': false
					},{
						'data':'userName',
						'orderable': false
					},{
						'data':'password',
						'orderable': false
					},{
						'data':function(row, type, val, meta) {
							return "<i class='fa fa-spin fa-spinner'></i>";
						},
						'orderable': false
					},{
						'data':function(row, type, val, meta) {
							let html = "";
							html += "<button class='btn btn-default btn-xs' onclick='viewModel.edit(" + row.id + ")' title='编辑'> <i class='fa fa-edit fa-lg'></i> </button> ";
							html += "<button class='btn btn-default btn-xs' onclick='viewModel.delete(" + row.id + ")' title='删除'> <i class='fa fa-trash-o fa-lg'></i> </button> ";
							html += "<button class='btn btn-default btn-xs' onclick='viewModel.manager(" + row.id + ")' title='管理'> <i class='fa fa-gear fa-lg'></i> </button> ";
							html += "<button class='btn btn-default btn-xs' onclick='viewModel.execServer(" + row.id + ")' title=''> <i class='fa fa-spin fa-spinner fa-lg'></i> </button> ";
							return html;
						},
						'orderable': false
					}]
				};
				createTable(options);
				setTimeout(viewModel.checkServer, 1000);
			},
			checkServer: function() {
				$('tbody').find('tr').each(function(idx, el) {
					let ip = $(el).find('td').eq(3).html();
					$.get('${ctx}/devops/serverHost/checkServerState/' + ip, function(data) {
						if (data) {
							$(el).find('td').eq(7).html('<span class="label label-success">运行中</span>');
							$(el).find('td').eq(8).find('button').last().find('i').removeClass('fa-spin fa-spinner');
							$(el).find('td').eq(8).find('button').last().find('i').addClass('fa-pause');
							$(el).find('td').eq(8).find('button').last().attr({'title': '关闭'});
						} else {
							$(el).find('td').eq(7).html('<span class="label label-danger">无法访问</span>');
							$(el).find('td').eq(8).find('button').last().find('i').removeClass('fa-spin fa-spinner');
							$(el).find('td').eq(8).find('button').last().find('i').addClass('fa-play');
							$(el).find('td').eq(8).find('button').last().attr({'title': '启动'});
						}
					});
				});
			},
			execServer: function(id) {
				let url = "${ctx}/devops/serverHost/execServer/" + id;
				$.get(url, function(data) {
					console.log(data);
					refreshTable();
					setTimeout(viewModel.checkServer, 1000);
				});
			},
			add: function() {
				let url = "${ctx}/devops/serverHost/add";
				top.showMyModel(url,'添加主机', '70%', '70%', callBackAction);
			},
			manager: function(id) {
				let url = "${ctx}/devops/serverHost/manager/" + id;
				window.location.href = url;
			},
			reset: function() {
				$(".datatable_query").val('');
			},
			query: function() {
				refreshTable();
			},
			edit: function(id) {
				let url = "${ctx}/devops/serverHost/edit/" + id;
				top.showMyModel(url,'编辑主机', '70%', '70%', callBackAction);
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
							url:'${ctx}/devops/serverHost/delete/'+ids,
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
		setTimeout(viewModel.checkServer, 1000);
	}
</script>
</html>
