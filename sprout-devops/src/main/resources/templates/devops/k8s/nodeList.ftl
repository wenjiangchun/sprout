<!DOCTYPE html>
<html lang="zh">
<head>
<title>K8s集群监控</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">运维管理</a></li>
				<li class="active">K8s集群监控</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">K8s集群Node列表</h3>
							<div class="box-tools">
							</div>
						</div>
						<!-- /.box-header -->
						<div class="box-body"  data-bind="foreach:nodeList">
								<div class="col-lg-3 col-xs-4">
									<!-- small box -->
									<div class="small-box bg-olive">
										<div class="inner">
											<h3 data-bind="text:$data.metadata.name"></h3>
											<p><strong>架构:</strong> <span data-bind="text:$data.status.nodeInfo.architecture"></span></p>
											<p><strong>容器运行环境:</strong> <span data-bind="text:$data.status.nodeInfo.containerRuntimeVersion"></span></p>
											<p><strong>kubeProxy版本:</strong> <span data-bind="text:$data.status.nodeInfo.kubeProxyVersion"></span></p>
											<p><strong>kubelet版本:</strong> <span data-bind="text:$data.status.nodeInfo.kubeletVersion"></span></p>
											<p><strong>内核版本:</strong> <span data-bind="text:$data.status.nodeInfo.operatingSystem"></span>/<span data-bind="text:$data.status.nodeInfo.kernelVersion"></span></p>
											<p><strong>操作系统:</strong> <span data-bind="text:$data.status.nodeInfo.osImage"></span></p>
											<p><strong>加入集群时间:</strong> <span data-bind="text:$data.metadata.creationTimestamp"></span></p>
										</div>
										<div class="icon">
											<i class="fa fa-server"></i>
										</div>
										<a href="#" class="small-box-footer">
											更多详细信息 <i class="fa fa-arrow-circle-right"></i>
										</a>
									</div>
								</div>
						</div>
					</div>
					<#--<a href="#" class="btn btn-primary" data-bind='click: add'><i class="fa fa-plus-circle"></i>  添加项目</a>-->
				</div>
			</div>
		</section>
</body>
<script type="text/javascript">
	let viewModel = {
		nodeList: ko.observableArray([]),
		getNodeList: function() {
			let that = this;
			$.get('${ctx}/devops/k8s/getNodeList', function(dts){
				that.nodeList(dts);
			});
		}
	}
	ko.applyBindings(viewModel);
	$(document).ready(function() {
		viewModel.getNodeList();
	});
</script>
</html>
