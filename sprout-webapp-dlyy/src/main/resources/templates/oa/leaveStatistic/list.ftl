<!DOCTYPE html>
<html lang="zh">
<head>
<title>请假汇总</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">请假管理</a></li>
				<li class="active">请假汇总</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">汇总列表</h3>
							<div class="box-tools">
								<form class="form-inline">
									<div class="form-group">
										<label for="year">统计年份</label>
										<select name="year" id="year" class="form-control" data-bind="options:years">
										</select>
									</div>
									<button type="button" class="btn btn-primary" data-bind="click:getResult"><i class="fa fa-search"></i> 统计</button>
								</form>
							</div>
						</div>
						<div class="box-body">
							<table id="contentTable" class="table table-bordered table-striped table-hover">
								<thead>
									<tr>
										<th>申请人</th>
										<th>年份</th>
										<th>月份</th>
										<th>类型</th>
										<th>天数</th>
									</tr>
								</thead>
								<tbody data-bind="foreach:dts">
								    <tr>
										<td data-bind="text:$data[4]"></td>
										<td data-bind="text:$data[1]"></td>
										<td data-bind="text:$data[2]"></td>
										<td data-bind="text:$data[3]"></td>
										<td data-bind="text:$data[0]"></td>
									</tr>
								</tbody>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
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
			years: ko.observableArray([2021,2020]),
			dts: ko.observableArray([]),
			getResult: function () {
				let that = this;
				$.get('${ctx}/oa/leaveStatistic/getLeaveStatisticList', {year:$('#year').val()}, function(dts) {
					that.dts(dts);
				});
			}
		};
		ko.applyBindings(viewModel);
		viewModel.getResult();
	});
</script>
</html>
