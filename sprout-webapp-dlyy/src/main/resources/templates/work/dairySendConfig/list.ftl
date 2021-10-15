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
									<th sName="smtp">协议</th>
									<th sName="to">接收人</th>
									<th sName="addTo">抄送人</th>
									<th sName="dairyStartDay">开始日期</th>
									<th sName="">开始周数</th>
									<th sName="operate" columnRender="formatOperator">操作</th>
								</tr>
								</tr>
								</thead>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
					<!-- /.box -->
					<a href="#" class="btn btn-primary" data-bind='click:add,visible:enableAdd'><i class="fa fa-plus-circle"></i>  添加邮件配置</a>
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
			enableAdd: ko.observable(false),
			checkConfig: function() {
				let that = this;
			    $.get('${ctx}/work/dairySendConfig/checkCurrentUserConfig', function(data){
					that.enableAdd(data.flag);
				})
			},
			initTable: function() {
				const options = {
					divId : "contentTable",
					url : "${ctx}/work/dairySendConfig/search",
					columns:[{
						'data':'id',
						'orderable': false
					},{
						'data':'worker.name'
					},{
						'data':'source'
					},{
						'data':'smtp',
						'orderable': false
					},{
						'data':'protocol',
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
						'data':'weekStartNum',
						'orderable': false
					},{
						'data':function(row, type, val, meta) {
							var html = "";
							html += "<a href='javascript:void(0)' onclick='viewModel.edit(" + row.id + ")' title='编辑'> <i class='fa fa-edit fa-lg'></i> </a> | ";
							html += "<a href='javascript:void(0)' onclick='viewModel.delete(\"" + row.id + "\")' title='删除'> <i class='fa fa-trash-o fa-lg'></i> </a> | ";
							html += "<a href='javascript:void(0)' onclick='viewModel.testSend(" + row.id + ")' title='发送测试'> <i class='fa fa-mail-forward'></i> </a>";
							return html;
						},
						'orderable': false
					}]
				};
				createTable(options);
				this.checkConfig();
			},
			add: function() {
				let url = "${ctx}/work/dairySendConfig/add";
				showMyModel(url,'添加日志发送配置', '70%', '70%', callBackAction);
			},
			reset: function() {
				$(".datatable_query").val('');
			},
			query: function() {
				refreshTable();
			},
			edit: function(id) {
				let url = "${ctx}/work/dairySendConfig/edit/" + id;
				top.showMyModel(url,'编辑发送配置信息', '70%', '70%', callBackAction);
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
								if (data.flag) {
									layer.alert(data.content, function() {
										refreshTable();
									});
								} else {
									layer.alert(data.content);
								}
							}
						});
					}, function(){
					});
				}
			},
			testSend: function(id) {
				$.get('${ctx}/work/dairySendConfig/testSendEmail/'+id, function(data) {
					layer.alert(data.content);
				});
			}
		};
		ko.applyBindings(viewModel);
		viewModel.initTable();
	});

	function callBackAction(data) {
		viewModel.checkConfig();
		refreshTable();
	}
</script>
</html>
