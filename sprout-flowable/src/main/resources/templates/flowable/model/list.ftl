<!DOCTYPE html>
<html lang="zh">
<head>
<title>流程模型管理</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">流程管理</a></li>
				<li class="active">模型管理</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">模型列表</h3>
							<div class="box-tools">
								<#--<a href="#" id="refreshRepository" class="btn btn-default"><i class="fa fa-repeat"></i>  刷新</a>
								<a href="#" id="add_btn" class="btn btn-info"><i class="fa fa-plus-circle"></i>  添加</a>-->
							</div>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table id="contentTable" class="table table-bordered table-striped table-hover">
								<thead>
								<tr>
									<th>模型ID</th>
									<th>模型名称</th>

									<th>操作</th>
								</tr>
								</thead>
								<tbody>
								<#list modelList as model>
									<tr>
										<th>${model.id}</th>
										<th>${model.name}</th>

										<th>操作</th>
									</tr>
								</#list>
								</tbody>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
					<!-- /.box -->
					<a href="#" class="btn btn-primary" data-bind='click: add'><i class="fa fa-plus-circle"></i>  流程部署</a>
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
			add: function() {
				let url = "${ctx}/system/config/add";
				if (this.parentId() != null && this.parentId() !== "") {
					url += "?parentId=" + this.parentId();
				}
				showMyModel(url,'添加配置', '900px', '50%', callBackAction);
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
	});
</script>
</html>
