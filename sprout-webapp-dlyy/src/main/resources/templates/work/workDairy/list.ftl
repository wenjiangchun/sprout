<!DOCTYPE html>
<html>
<head>
<title>工作日志</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
	<#include "../../common/upload.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">工作管理</a></li>
				<li class="active">工作日志</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">工作日志列表</h3>
							<div class="box-tools">
								<#--<a href="#" id="refreshRepository" class="btn btn-default"><i class="fa fa-repeat"></i>  刷新</a>
								<a href="#" id="add_btn" class="btn btn-info"><i class="fa fa-plus-circle"></i>  添加</a>-->
							</div>
								<#--<form class="form-inline">
									<input type="hidden" class="datatable_query" name="parent.id" data-bind="value: parentId"/>
									<div class="box-body">
										<div class="form-group">
											<label for="name_like">工作内容</label>
											<input type="text" name="content_like" class="datatable_query form-control">
										</div>
										<button type="button" class="btn btn-sm btn-primary" data-bind='click: query' style="margin-left:5px;">
											<i class="fa fa-search"></i> 查询
										</button>
										<button type="button" class="btn btn-sm btn-default" data-bind='click: reset' style="margin-left:10px;">清空</button>
									</div>
								</form>-->
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table id="contentTable" class="table table-bordered table-striped table-hover">
								<thead>
								<tr>
								<tr>
									<th>日期</th>
									<th>姓名</th>
									<th>星期</th>
									<th>周次</th>
									<th>工作内容</th>
									<th>备注</th>
									<th>操作</th>
								</tr>
								</thead>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
					<!-- /.box -->
					<div class="row">
						<div class="col-xs-1">
							<input type="file" name="file" id="uploadExcel">
						</div>
						<div class="col-xs-4">
							<a href="#" class="btn btn-primary" data-bind='click: add'><i class="fa fa-pencil"></i>  填写工作日志</a>
							<a href="#" class="btn btn-danger" data-bind='click: sendEmail'><i class="fa fa-envelope"></i>  发送周报</a>
							<a href="#" class="btn btn-danger" data-bind='click: generateWorkDairy'><i class="fa fa-bars"></i>  生成日志</a>
						</div>
					</div>
				</div>
			</div>
		</section>
</body>
<script src="${ctx}/res/lib/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${ctx}/res/lib/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script type="text/javascript">
	let viewModel;
	$(document).ready(function() {
		viewModel = {
			dictName: ko.observable(''),
			parentId: ko.observable('${parentId!}'),
			initTable: function() {
				const options = {
					divId : "contentTable",
					url : "${ctx}/work/workDairy/search",
					columns:[{
						'data':'workDay'
					},{
						'data':'worker.name',
						'orderable': false
					},{
						'data':'weekDay',
						'orderable': false
					},{
						'data':'weekNum',
						'orderable': false
					},{
						'data':function(row, type, val, meta) {
							let content = row.content;
							if (content != null && content.length > 80) {
								content = content.substring(0,80) + '...';
							}
							return content;
						},
						'width': '45%',
						'orderable': false
					},{
						'data':function(row, type, val, meta) {
							let remark = row.remark;
							if (remark != null && remark.length > 20) {
								remark = remark.substring(0,20) + '...';
							}
							return remark;
						},
						'width': '25%',
						'orderable': false
					},{
						'data':function(row, type, val, meta) {
							let html = "";
							html += "<a href='javascript:void(0)' onclick='viewModel.edit(" + row.id + ")' title='编辑'> <i class='fa fa-edit fa-lg'></i> </a> | ";
							html += "<a href='javascript:void(0)' onclick='viewModel.delete(" + row.id + ")' title='删除'> <i class='fa fa-trash-o fa-lg'></i> </a>";
							return html;
						},
						'orderable': false
					}]
				};
				createTable(options);
			},
			add: function() {
				let url = "${ctx}/work/workDairy/add";
				top.showMyModel(url,'填写工作日志', '70%', '70%', callBackAction);
			},
			reset: function() {
				$(".datatable_query").val('');
			},
			query: function() {
				refreshTable();
			},
			edit: function(id) {
				let url = "${ctx}/work/workDairy/edit/" + id;
				top.showMyModel(url,'编辑工作日志', '70%', '70%', callBackAction);
			},
			sendEmail: function() {
				layer.msg('数据处理发送中...', {
					icon: 16,
					shade: 0.01
				});
				let url = "${ctx}/work/workDairy/sendEmail";
				$.get(url, function(data) {
					layer.alert(data.content);
				});
			},
			generateWorkDairy: function() {
				layer.msg('数据生成中...', {
					icon: 16,
					shade: 0.01
				});
				$.post('${ctx}/work/workDairy/generateWorkDairy', function(data) {
					if(data.flag) {
						if (data.result > 0) {
							layer.alert('生成成功，共生成' + data.result + '条记录');
							refreshTable();
						} else {
							layer.alert('当前数据不需要生成');
						}
					} else {
						layer.alert(data.content);
					}
				});
			},
			delete: function(id) {
				if (id == null || id === "") {
					layer.alert("ID不能为空");
				} else {
					layer.confirm('确认删除?', {
						btn: ['确认','取消'] //按钮
					}, function(){
						const ids = [id];
						$.post({
							url:'${ctx}/work/workDairy/delete/'+ids,
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
			}
		};
		ko.applyBindings(viewModel);
		viewModel.initTable();
		$("#uploadExcel").fileinput({
			uploadUrl: '${ctx}/work/workDairy/uploadWorkDairy',
			enableResumableUpload: false,
			browseLabel: '周报上传',
			browseIcon: '<i class="fa fa-upload"></i>',
			showPreview:false,
			showCaption: false,
			showUploadStats: false,
			showUploadedThumbs: false,
			progressDelay:1000,
			dropZoneEnabled: false,
			uploadAsync:true,
			showUpload : false,
			showRemove : false,
			language : 'zh'
		}).on('fileuploaded', function(event, data, previewId, index, fileId) {
			layer.alert(data.response.content);
			refreshTable();
		}).on("filebatchselected", function(event, files) {
			$("#uploadExcel").fileinput("upload");
			$('.kv-hidden').hide();
			layer.msg('数据上传处理中...', {
				icon: 16,
				shade: 0.01
			});
		});
	});

	function callBackAction(data) {
		refreshTable();
	}
</script>
</html>
