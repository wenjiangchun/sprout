<!DOCTYPE html>
<html lang="zh">
<head>
<title>待办事项</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">请假管理</a></li>
				<li class="active">待办事项</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">待办请假列表</h3>
							<div class="box-tools">
							</div>
						</div>
						<div class="box-body">
							<table id="contentTable" class="table table-bordered table-striped table-hover">
								<thead>
									<tr>
										<th>编号</th>
										<th>申请人</th>
										<th>申请时间</th>
										<th>流程信息</th>
										<th>计划开始时间</th>
										<th>计划结束时间</th>
										<th>请假类型</th>
										<th>流程状态</th>
										<th>当前节点</th>
										<th>任务时间</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
								  <#list todoList as leave>
								  	 <tr>
										 <td>${leave.id!}</td>
										 <td>${leave.applier.name!}</td>
										 <td>${leave.applyTime!}</td>
										 <td><a href="javascript:void(0)" onclick="viewModel.showDiagram('${leave.processInstanceId!}')">查看流程图</a></td>
										 <td>${leave.planStartTime!}</td>
										 <td>${leave.planEndTime!}</td>
										 <td>${leave.leaveType.name!}</td>
										 <td>
											 <#if leave.processInstance.suspended>
												 <span class="label label-danger">已挂起</span>
											 <#else>
												 <span class="label label-success">已激活</span>
											 </#if>
										 </td>
										 <td>${leave.currentTask.name!}</td>
										 <td>${leave.currentTask.createTime?datetime!}</td>
										 <td>
											 <#if !leave.processInstance.suspended>
												 <button class="btn btn-default btn-xs" onclick="viewModel.showTask('${leave.currentTask.id}','${leave.currentTask.name!}')" title="办理"><i class="fa fa-pencil"></i></button>
											 </#if>
										</td>
									 </tr>
								  </#list>
								  <#if todoList?size==0>
								  	 <tr>
										 <td colspan="11" align="center">暂无相关数据</td>
									 </tr>
								  </#if>
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
			showDiagram: function (processInstanceId) {
				let url = "${ctx}/flowable/processInstance/genProcessDiagram/" + processInstanceId;
				top.showMyModel(url,'查看流程图', '70%', '70%');
			},
			showTask: function(taskId, title) {
				let url = "${ctx}/oa/leave/showTask/" + taskId;
				top.showMyModel(url,title, '900px', '70%', callBackAction);
			}
		};
		ko.applyBindings(viewModel);
	});

	function callBackAction() {
		window.location.reload();
	}

</script>
</html>
