<!DOCTYPE html>
<html>
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
				<li class="active">数据源浏览</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-3">
					<div class="box box-solid">
						<div class="box-header">
							<h3 class="box-title">
								<select class="form-control" id="dataSourceMeta">
									<#list dataSourceMetaList as ds>
										<option value="${ds.id}">${ds.name}</option>
									</#list>
								</select></h3>
							<div class="box-tools pull-right">
								<button type="button" class="btn btn-default btn-sm" data-bind="click: reloadTables"><i class="fa fa-refresh"></i></button>
							</div>
						</div>
						<!-- /.box-header -->
						<div class="box-body">

							<ul id="dbTree" class="ztree" style="height:600px;overflow:auto"></ul>
						</div>
						<!-- /.box-body -->
					</div>
					<!-- /.box -->
				</div>
				<div class="col-xs-9">
					<div class="nav-tabs-custom">
						<ul class="nav nav-tabs">
							<li class="active"><a href="#tab_1" data-toggle="tab">数据浏览</a></li>
							<li><a href="#tab_2" data-toggle="tab">元数据</a></li>
							<li class="pull-right"><span data-bind="text:tableName" class="text-red text-bold"></span></li>
						</ul>
						<div class="tab-content">
							<div class="tab-pane active" id="tab_1">
								<table id="contentTable1" class="table table-bordered table-striped table-hover">
									<thead>
									   <tr data-bind="foreach: dbDescArr">
										   <td data-bind="text: columnName"></td>
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
									<tr>
										<th>字段名称</th>
										<th>字段类型</th>
										<th>注释</th>
										<th>是否主键</th>
										<th>是否可以为空</th>
										<th>默认值</th>
										<th>编码</th>
										<th>压缩方式</th>
									</tr>
									</thead>
									<tbody data-bind="foreach: dbDescArr">
										<tr>
											<td data-bind="text: columnName"></td>
											<td data-bind="text: columnType"></td>
											<td data-bind="text: comment"></td>
											<td></td>
											<td data-bind="text: notNull"></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>
									</tbody>
								</table>
							</div>
							<!-- /.tab-pane -->
							<!-- /.tab-pane -->
						</div>
						<!-- /.tab-content -->
					</div>
				</div>
				<!-- /.col -->
			</div>
			<!-- /.row -->
		</section>
</body>
<script src="${ctx}/res/lib/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${ctx}/res/lib/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script type="text/javascript">
	let viewModel, tree;
	$(document).ready(function() {
		viewModel = {
			dbTables: ko.observableArray([]),
			dbDescArr: ko.observableArray([]),
			tableName: ko.observable(),
			initTable: function(metaId, tableName) {
				let columns = [];
				_.each(this.dbDescArr(), function(column) {
					columns.push({'data':column.columnName});
				});
				const options = {
					divId : "contentTable1",
					url : "${ctx}/data/dataSourceNavigate/getDBData/" + metaId + "/" + tableName,
					columns:columns
				};
				createTable(options);
			},
			reloadTables: function() {
				$.get('${ctx}/data/dataSourceNavigate/showDB', {"metaId": $("#dataSourceMeta").val()}, function(data) {
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
						metaId : $("#dataSourceMeta").val()
					}
					viewModel.tableName(treeNode.name);
					$.get('${ctx}/data/dataSourceNavigate/showTableDesc', param, function(data) {
						//根据获取数据取得结果
						viewModel.dbDescArr(data);
						viewModel.initTable(param.metaId, param.tableName);
					});
				}
			}
		};
		ko.applyBindings(viewModel);
	});
</script>
</html>
