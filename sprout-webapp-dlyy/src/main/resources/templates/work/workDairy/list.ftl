<!DOCTYPE html>
<html>
<head>
<title>工作日志</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">工作管理</a></li>
				<li class="active">工作日志</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">工作日志列表</h3>
							<div class="box-tools">
								<#--<a href="#" id="refreshRepository" class="btn btn-default"><i class="fa fa-repeat"></i>  刷新</a>
								<a href="#" id="add_btn" class="btn btn-info"><i class="fa fa-plus-circle"></i>  添加</a>-->
							</div>
								<form class="form-inline">
									<input type="hidden" class="datatable_query" name="parent.id" data-bind="value: parentId"/>
									<div class="box-body">
										<div class="form-group">
											<label for="name_like">工作内容</label>
											<input type="text" name="content_like" class="datatable_query form-control">
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
									<th>编号</th>
									<th>姓名</th>
									<th>日期</th>
									<th>星期</th>
									<th>周次</th>
									<th>工作内容</th>
									<th>备注</th>
									<th>操作</th>
								</tr>
								</thead>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
					<!-- /.box -->
					<a href="#" class="btn btn-primary" data-bind='click: add'><i class="fa fa-pencil"></i>  填写日志</a>
				</div>
				<!-- /.col -->
			</div>
			<!-- /.row -->
		</section>
</body>
<script src="${ctx}/res/lib/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${ctx}/res/lib/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script type="text/javascript">
	let viewModel;
	$(document).ready(function() {
		viewModel = {
			dictName: ko.observable(''),
			parentId: ko.observable('${parentId!}'),
			initTable: function() {
				const options = {
					divId : "contentTable",
					url : "${ctx}/system/config/search",
					columns:[{
						'data':'id'
					},{
						'data':'user.name'
					},{
						'data':'workDay'
					},{
						'data':'weekDay'
					},{
						'data':'weekNum'
					},{
						'data':'content'
					},{
						'data':'remark'
					},{
						'data':function(row, type, val, meta) {
							let html = "";
							html += "<a href='javascript:void(0)' onclick='viewModel.edit(" + row.id + ")' title='编辑'> <i class='fa fa-edit fa-lg'></i> </a> | ";
							if (row.configType === 'B') {
								html += "<a href='javascript:void(0)' onclick='viewModel.delete(" + row.id + ")' title='删除'> <i class='fa fa-trash-o fa-lg'></i> </a>";
							}
							return html;
						}
					}]
				};
				createTable(options);
			},
			add: function() {
				let url = "${ctx}/work/workDairy/add";
				showMyModel(url,'填写日志', '70%', '70%', callBackAction);
			},
			reset: function() {
				$(".datatable_query").val('');
			},
			query: function() {
				refreshTable();
			},
			edit: function(id) {
				let url = "${ctx}/system/config/edit/" + id;
				showMyModel(url,'编辑配置', '900px', '50%', callBackAction);
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
							url:'${ctx}/system/config/delete/'+ids,
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
