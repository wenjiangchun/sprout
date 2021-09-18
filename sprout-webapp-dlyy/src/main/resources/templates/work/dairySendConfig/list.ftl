<!DOCTYPE html>
<html>
<head>
<title>日志发送配置</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">工作管理</a></li>
				<li class="active">日志发送配置</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">发送配置列表</h3>
							<div class="box-tools">
							</div>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table id="contentTable" class="table table-bordered table-striped table-hover">
								<thead>
								<tr>
								<tr>
									<th sName="id">编号</th>
									<th sName="from">员工姓名</th>
									<th sName="from">发送邮箱</th>
									<th sName="smtp">邮件服务器</th>
									<th sName="token">Token</th>
									<th sName="to">接收邮箱</th>
									<th sName="addTo">抄送邮箱</th>
									<th sName="dairyStartDay">日志开始日期</th>
									<th sName="sendTime">发送时间</th>
									<th sName="operate" columnRender="formatOperator">操作</th>
								</tr>
								</tr>
								</thead>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
					<!-- /.box -->
					<a href="#" class="btn btn-primary" data-bind='click: add'><i class="fa fa-plus-circle"></i>  添加配置</a>
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
					url : "${ctx}/work/dairySendConfig/search",
					columns:[{
						'data':'id',
						'orderable': false
					},{
						'data':'user.userName'
					},{
						'data':'source'
					},{
						'data':'smtp',
						'orderable': false
					},{
						'data':'token',
						'orderable': false
					},{
						'data':'destination',
						'orderable': false
					},{
						'data':'copyDestinations',
						'orderable': false
					},{
						'data':'dairyStartDay',
						'orderable': false
					},{
						'data':'sendTime',
						'orderable': false
					},{
						'data':function(row, type, val, meta) {
							var html = "";
							html += "<a href='javascript:void(0)' onclick='viewModel.edit(" + row.id + ")' title='编辑'> <i class='fa fa-edit fa-lg'></i> </a> | ";
							html += "<a href='javascript:void(0)' onclick='viewModel.delete(\"" + row.id + "\")' title='删除'> <i class='fa fa-trash-o fa-lg'></i> </a> | ";
							html += "<a href='javascript:void(0)' onclick='viewModel.addResource(" + row.id + ")' title='资源授权'> <i class='fa fa-database'></i> </a>";
							return html;
						},
						'orderable': false
					}]
				};
				createTable(options);
			},
			add: function() {
				let url = "${ctx}/work/dairySendConfig/add";
				showMyModel(url,'添加角色', '900px', '50%', callBackAction);
			},
			reset: function() {
				$(".datatable_query").val('');
			},
			query: function() {
				refreshTable();
			},
			edit: function(id) {
				let url = "${ctx}/work/dairySendConfig/edit/" + id;
				showMyModel(url,'编辑角色', '900px', '50%', callBackAction);
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
							url:'${ctx}/work/dairySendConfig/delete/'+ids,
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
