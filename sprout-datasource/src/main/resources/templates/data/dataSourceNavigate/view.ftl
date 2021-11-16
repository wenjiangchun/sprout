<!DOCTYPE html>
<html lang="zh">
<head>
<title>数据浏览</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
	<#include "../../common/ztree.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">数据管理</a></li>
				<li><a href="${ctx}/data/dataSourceMeta/list">数据源管理</a></li>
				<li class="active">数据源浏览</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-2">
					<div class="box box-solid">
						<div class="box-header">
							<h3 class="box-title">
							   ${dsMeta.name!}
							</h3>
							<div class="box-tools pull-right">
								<button type="button" class="btn btn-default btn-sm" data-bind="click: reloadTables"><i class="fa fa-refresh"></i></button>
							</div>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<ul id="dbTree" class="ztree" style="height:800px;overflow:auto"></ul>
						</div>
						<!-- /.box-body -->
					</div>
					<!-- /.box -->
				</div>
				<div class="col-xs-10">
					<div class="nav-tabs-custom">
						<ul class="nav nav-tabs">
							<li class="active"><a href="#tab_1" data-toggle="tab">数据浏览</a></li>
							<li><a href="#tab_2" data-toggle="tab">元数据</a></li>
							<li class="pull-right"><a href="#"><span class="label label-success" data-bind="text:tableName"></span></a>
							</li>
						</ul>
						<div class="tab-content">
							<div class="tab-pane active" id="tab_1">
								<table id="contentTable1" class="table table-bordered table-striped table-hover">
									<thead>
									   <tr data-bind="foreach: dbColumnDts">
										   <th data-bind="text: $data[0]"></th>
									   </tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
							<!-- /.tab-pane -->
							<div class="tab-pane" id="tab_2">
								<table id="contentTable" class="table table-bordered table-striped table-hover">
									<thead>
									<tr data-bind="foreach: dbColumnLabels">
										<th data-bind="text: $data"></th>
									</tr>
									</thead>
									<tbody data-bind="foreach: dbColumnDts">
										<tr data-bind="foreach: $data">
											<td data-bind="text: $data"></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</section>
</body>
<script src="${ctx}/res/lib/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${ctx}/res/lib/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script type="text/javascript">
	let viewModel, tree;
	$(document).ready(function() {
		viewModel = {
			dbTables: ko.observableArray([]),
			dbColumnLabels: ko.observableArray([]),
			dbColumnDts: ko.observableArray([]),
			tableName: ko.observable(),
			initTable: function(columnArr,metaId, tableName) {
				if (dataTables != null) {
					dataTables.clear();
					dataTables.destroy();
					dataTables = null;
				}
				viewModel.dbColumnLabels([]);
				viewModel.dbColumnDts([]);
				_.each(columnArr, function(dt, idx) {
					let columnDt = [];
					_.map(columnArr[idx], function(value, key) {
						if (idx === 0) {
							viewModel.dbColumnLabels.push(key);
						}
						columnDt.push(value);
					});
					viewModel.dbColumnDts.push(columnDt);
				});

				let columns = [];
				_.each(viewModel.dbColumnDts(), function(column) {
					columns.push({'data':column[0], 'orderable':false});
				});
				const options = {
					divId : "contentTable1",
					url : "${ctx}/data/dataSourceNavigate/getDBData/" + metaId + "/" + tableName,
					columns:columns
				};
				createTable(options);
			},
			reloadTables: function() {
				/*viewModel.dbColumnDts([]);
				viewModel.dbColumnLabels([]);
				viewModel.tableName('');*/
				$.get('${ctx}/data/dataSourceNavigate/showDB', {"metaId": '${dsMeta.id!}'}, function(data) {
					var setting = {data:{
							simpleData:{
								enable:true,
								idKey:"id",
								pIdKey:"parentId",
								rootPId:null
							},
							key:{
								name:"name"
							}
						}, callback: {
							onClick:viewModel.clickTreeNode
						}};
					$.fn.zTree.init($("#dbTree"), setting, data);
					tree = $.fn.zTree.getZTreeObj("dbTree");
				});
			},
			clickTreeNode: function(event, treeId, treeNode, clickFlag) {
				if (treeNode.nodeType === 'table') {
					const param = {
						tableName : treeNode.name,
						schemaName : tree.getNodeByParam("id",treeNode.parentId).name,
						metaId : '${dsMeta.id!}'
					}
					viewModel.tableName(treeNode.name);
					$.get('${ctx}/data/dataSourceNavigate/showTableDesc', param, function(data) {
						//根据获取数据取得结果
						viewModel.initTable(data, param.metaId, param.tableName);
					});
				}
			}
		};
		ko.applyBindings(viewModel);
		viewModel.reloadTables();
	});
</script>
</html>
