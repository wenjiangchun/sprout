<!DOCTYPE html>
<html lang="zh">
<head>
<title>UPS监控日志</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">运维管理</a></li>
				<li class="active">UPS监控日志</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">UPS监控日志</h3>
							<div class="box-tools">
							</div>
						</div>
						<div class="box-body">
							<table id="contentTable" class="table table-bordered table-striped table-hover">
								<thead>
									<tr>
										<th>编号</th>
										<th>执行时间</th>
										<th>监测结果</th>
										<th>执行结果</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</section>
</body>
<script type="text/javascript">
	let viewModel;
	$(document).ready(function() {
		viewModel = {
			initTable: function() {
				const options = {
					divId : "contentTable",
					url : "${ctx}/devops/serverLog/search",
					columns:[{
						'data':'id'
					},{
						'data':'execTime',
						'orderable': true
					},{
						'data':'monitorResult',
						'orderable': false
					},{
						'data':'execResult',
						'orderable': false
					}]
				};
				createTable(options);
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
