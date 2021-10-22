<!DOCTYPE html>
<html>
<head>
<title>资产管理</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">办公资产</a></li>
				<li class="active">资产管理</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">资产列表</h3>
							<div class="box-tools">
							</div>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table id="contentTable" class="table table-bordered table-striped table-hover">
								<thead>
									<tr>
										<th>编号</th>
										<th>资产编码</th>
										<th>品牌</th>
										<th>种类</th>
										<th>型号</th>
										<th>单位</th>
										<th>数量</th>
										<th>资产类型</th>
										<th>操作</th>
									</tr>
								</thead>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
						<a href="#" class="btn btn-primary" data-bind='click: add'><i class="fa fa-edit"></i>  发起申请</a>
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
					url : "${ctx}/oa/asset/search",
					columns:[{
						'data':'id'
					},{
						'data':'assetNum',
						'orderable': false
					},{
						'data':'brand.name',
						'orderable': false
					},{
						'data':'classify.name',
						'orderable': false
					},{
						'data':'model.name',
						'orderable': false
					},{
						'data':'unit.name',
						'orderable': false
					},{
						'data':'count',
						'orderable': false
					},{
						'data':function(row, type, val, meta) {

							return row;
						},
						'width': '25%',
						'orderable': false
					},{
						'data':function(row, type, val, meta) {
							let html = "";
							html += "<a href='javascript:void(0)' onclick='viewModel.edit(" + row.id + ")' title='编辑'> <i class='fa fa-edit fa-lg'></i> </a> | ";
							html += "<a href='javascript:void(0)' onclick='viewModel.delete(" + row.id + ")' title='删除'> <i class='fa fa-trash-o fa-lg'></i> </a>";
							return html;
						},
						'orderable': false
					}]
				};
				createTable(options);
			},
			add: function() {
				let url = "${ctx}/oa/leave/applyLeave/";
				top.showMyModel(url,'填写请假申请', '900px', '60%', callBackAction);
			},
			edit: function(id) {
				let url = "${ctx}/oa/leave/editLeave/" + id;
				top.showMyModel(url,'编辑申请', '900px', '60%', callBackAction);
			},
			showLeave: function(id) {
				let url = "${ctx}/oa/leave/showLeave/" + id;
				top.showMyModel(url,'查看信息', '900px', '60%');
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

	function callBackAction() {
		window.location.reload();
	}
</script>
</html>
