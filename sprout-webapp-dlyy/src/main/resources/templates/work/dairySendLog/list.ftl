<!DOCTYPE html>
<html>
<head>
<title>日志发送记录</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">工作管理</a></li>
				<li class="active">日志发送记录</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">发送记录列表</h3>
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
									<th sName="smtp">接收人</th>
									<th sName="smtp">抄送人</th>
									<th sName="subject">发送主题</th>
									<th sName="dairyStartDay">发送日期</th>
									<th sName="">发送状态</th>
									<th sName="">结果</th>
								</tr>
								</tr>
								</thead>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
					<!-- /.box -->
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
					url : "${ctx}/work/dairySendLog/search",
					columns:[{
						'data':'id',
						'orderable': false
					},{
						'data':'user.name'
					},{
						'data':'source'
					},{
						'data':'destination',
						'orderable': false
					},{
						'data':'copyDestinations',
						'orderable': false
					},{
						'data':'subject',
						'orderable': false
					},{
						'data':'sendTime',
						'orderable': false
					},{
						'data':function(row, type, val, meta) {
							if (row.sendFlag) {
								return "<span class='label label-success'>成功</span>";
							}
							return "<span class='label label-danger'>失败</span>";
						}
					},{
						'data':'sendResult',
						'orderable': false
					}]
				};
				createTable(options);
				this.checkConfig();
			}
		};
		ko.applyBindings(viewModel);
		viewModel.initTable();
	});

</script>
</html>
