<!DOCTYPE html>
<html>
<head>
<title>我的请假</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">请假管理</a></li>
				<li class="active">我的请假</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">请假列表</h3>
							<div class="box-tools">
							</div>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table id="contentTable" class="table table-bordered table-striped table-hover">
								<thead>
									<tr>
										<th>编号</th>
										<th>申请人</th>
										<th>申请时间</th>
										<th>计划开始时间</th>
										<th>计划结束时间</th>
										<th>请假类型</th>
										<th>当前状态</th>
										<th>销假时间</th>
										<th>实际开始时间</th>
										<th>实际结束时间</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
								  <#list leaveList as leave>
								  	 <tr>
										 <td>${leave.id!}</td>
										 <td>${leave.applier.name!}</td>
										 <td>${leave.applyTime!}</td>
										 <td>${leave.planStartTime?string("yyyy-MM-dd")}
										     <#if leave.planStartFlag == 0>
												 <#elseif leave.planStartFlag == 1>
													<span class="text-bold text-red">[上午]</span>
													 <#else >
												   <span class="text-bold text-red">[下午]</span>
										     </#if>
										 </td>
										 <td>${leave.planEndTime?string("yyyy-MM-dd")}
											 <#if leave.planEndFlag == 0>
											 <#elseif leave.planEndFlag == 1>
												  <span class="text-bold text-red">[上午]</span>
											 <#else >
												   <span class="text-bold text-red">[下午]</span>
											 </#if>
										 </td>
										 <td>${leave.leaveType.name!}</td>
										 <td>
											 <#if leave.state.state==0>
												 <span class="label label-primary">已申请</span>
											   <#elseif leave.state.state==1>
												   <span class="label label-success">办理中</span>
											 <#elseif leave.state.state==-1>
												 <span class="label label-danger">已放弃</span>
											   <#else>
												 <span class="label label-default">已结束</span>
											 </#if>
										 </td>
										 <td>${leave.backTime!}</td>
										 <td><#if leave.realStartTime??>${leave.realStartTime?string("yyyy-MM-dd")}</#if>
											 <#if leave.realStartFlag == 0>
											 <#elseif leave.realStartFlag == 1>
												 <span class="text-bold text-red">[上午]</span>
											 <#else >
												 <span class="text-bold text-red">[下午]</span>
											 </#if>
										 </td>
										 <td><#if leave.realEndTime??>${leave.realEndTime!string("yyyy-MM-dd")}</#if>
											 <#if leave.realEndFlag == 0>
											 <#elseif leave.realEndFlag == 1>
												 <span class="text-bold text-red">[上午]</span>
											 <#else >
												 <span class="text-bold text-red">[下午]</span>
											 </#if>
										 </td>
										 <td>
											 <#if leave.state.state==0 || leave.state.state==1>
												 <a href="javascript:void(0)" class="btn btn-default btn-xs" onclick="viewModel.showDiagram('${leave.processInstanceId!}')" title="查看流程图"><i class="fa fa-exchange"></i></a>
											 </#if>
											 <#if leave.state.state==0>
												 <a href="javascript:void(0)" class="btn btn-default btn-xs" onclick="viewModel.edit('${leave.id!}')" title="编辑"><i class="fa fa-pencil"></i></a>
												 <a href="javascript:void(0)" class="btn btn-default btn-xs" onclick="viewModel.delete('${leave.id!}')" title="删除"><i class="fa fa-trash-o"></i></a>
											 </#if>
											 <a href="javascript:void(0)" class="btn btn-default btn-xs" onclick="viewModel.showLeave('${leave.id!}')" title="查看"><i class="fa fa-info-circle"></i></a>
										 </td>
									 </tr>
								  </#list>
								  <#if leaveList?size==0>
								  	 <tr>
										 <td colspan="11" align="center">暂无相关数据</td>
									 </tr>
								  </#if>
								</tbody>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
						<a href="#" class="btn btn-primary" data-bind='click: add'><i class="fa fa-edit"></i>  发起请假申请</a>
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
			showDiagram: function (processInstanceId) {
				let url = "${ctx}/flowable/processInstance/genProcessDiagram/" + processInstanceId;
				top.showMyModel(url,'查看流程图', '70%', '70%');
			},
			add: function() {
				let url = "${ctx}/oa/leave/applyLeave/";
				//top.showMyModel(url,'填写请假申请', '900px', '65%', callBackAction);
				window.location.href = url;
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
			},
			testSend: function(id) {
				$.get('${ctx}/work/dairySendConfig/testSendEmail/'+id, function(data) {
					layer.alert(data.content);
				});
			}
		};
		ko.applyBindings(viewModel);
	});

	function callBackAction() {
		window.location.reload();
	}
</script>
</html>
