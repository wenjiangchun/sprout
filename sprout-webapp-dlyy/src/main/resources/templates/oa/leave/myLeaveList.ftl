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
										<th>流程信息</th>
										<th>计划开始时间</th>
										<th>计划结束时间</th>
										<th>请假类型</th>
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
										 <td><a href="javascript:void(0)" onclick="viewModel.showDiagram('${leave.processInstanceId!}')">查看流程图</a></td>
										 <td>${leave.planStartTime!}</td>
										 <td>${leave.planEndTime!}</td>
										 <td>${leave.leaveType.name!}</td>
										 <td>${leave.backTime!}</td>
										 <td>${leave.realStartTime!}</td>
										 <td>${leave.realStartTime!}</td>
										 <td>操作</td>
									 </tr>
								  </#list>
								  <#if leaveList?size==0>
								  	 <tr>
										 <td>暂无相关数据</td>
									 </tr>
								  </#if>
								</tbody>
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
			showDiagram: function (processInstanceId) {
				let url = "${ctx}/flowable/processInstance/genProcessDiagram/" + processInstanceId;
				showMyModel(url,'查看流程图', '70%', '70%');
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
			},
			testSend: function(id) {
				$.get('${ctx}/work/dairySendConfig/testSendEmail/'+id, function(data) {
					layer.alert(data.content);
				});
			}
		};
		ko.applyBindings(viewModel);
	});

</script>
</html>
