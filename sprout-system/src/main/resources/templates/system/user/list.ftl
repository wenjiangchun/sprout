<!DOCTYPE html>
<html lang="zh">
<head>
<title>用户管理</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
	<#include "../../common/ztree.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">系统管理</a></li>
				<li class="active">用户管理</li>
			</ol>
		</section>
		<section class="content">
			<div class="row">
				<div class="col-xs-3">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">组织机构树</h3>
						</div>
						<div class="box-body">
							<ul id="groupTree" class="ztree"></ul>
						</div>
					</div>
				</div>
				<div class="col-xs-9">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">用户列表<span data-bind="text: groupName"></span></h3>
							<div class="box-tools">

							</div>
								<form class="form-inline">
									<input type="hidden" class="datatable_query" name="group.id" data-bind="value: groupId"/>
									<div class="box-body">
										<div class="form-group">
											<label for="loginname">登录名</label>
											<input type="text" id="loginname" name="loginName_like" class="datatable_query form-control">
										</div>
										<button type="button" class="btn btn-sm btn-primary" data-bind='click: query' style="margin-left:5px;">
											<i class="fa fa-search"></i> 查询
										</button>
										<button type="button" class="btn btn-sm btn-danger" data-bind='click: reset' style="margin-left:10px;">清空</button>
									</div>
								</form>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table id="contentTable" class="table table-bordered table-striped table-hover">
								<thead>
								<tr>
									<th sName="id" bSortable="true">编号</th>
									<th sName="loginName" bSortable="true">登录名</th>
									<th sName="name" bSortable="true">用户名</th>
									<th sName="mobile" >手机号</th>
									<th sName="group.name">所在机构</th>
									<th sName="sex" columnRender="formatSex">性别</th>
									<th sName="status" columnRender="formatStatus">状态</th>
									<th sName="operator" columnRender="formatOperator">操作</th>
								</tr>
								</thead>
							</table>
						</div>
					</div>
					<a href="#" class="btn btn-primary" data-bind='click: add'><i class="fa fa-plus-circle"></i>  添加用户</a>
				</div>
			</div>
		</section>
</body>
<script type="text/javascript">
	let viewModel;
	let tree;
	$(document).ready(function() {
		viewModel = {
			groupName: ko.observable(''),
			groupId: ko.observable('${groupId!}'),
			add: function() {
				let url = "${ctx}/system/user/add";
				if (this.groupId() != null && this.groupId() !== "") {
					url += "?groupId=" + this.groupId();
				}
				showMyModel(url,'添加用户', '800px', '60%', callBackAction);
			},
			reset: function() {
				$(".datatable_query").val('');
			},
			query: function() {
				refreshTable();
			},
			edit: function(id) {
				let url = "${ctx}/system/user/edit/" + id;
				showMyModel(url,'编辑用户', '800px', '60%', callBackAction);
			},
			delete: function(id) {
				if (id == null || id === "") {
					console.log("ID不能为空");
				} else {
					layer.confirm('确认删除?', {
						btn: ['确认','取消'] //按钮
					}, function(){
						const ids = [id];
						$.post({
							url:'${ctx}/system/user/delete/'+ids,
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
			}
		};
		ko.applyBindings(viewModel);
		initGroupTree();
		initDataTable();
	});

	function initGroupTree() {
		$.ajax({
			method : "post",
			url : "${ctx}/system/group/getTopGroups",
			dataType : "json",
			success : function(data) {
				var setting = {data:{
						simpleData:{
							enable:true,
							idKey:"id",
							pIdKey:"pid",
							rootPId:null
						},
						key:{
							name:"fullName"
						}
					}, callback: {
						onClick:onClick
					}};
				$.fn.zTree.init($("#groupTree"), setting, data);

				tree = $.fn.zTree.getZTreeObj("groupTree");
				let parentId = viewModel.groupId();
				if (parentId != null && parentId != "") {
					let node = tree.getNodeByParam("id",parentId);
					if(!node.isParent){
						node = node.getParentNode();
					}
					tree.selectNode(node,false);
					tree.expandNode(node, true, false, true);
				} else {
					let node = tree.getNodeByParam("fullName","组织机构树");
					tree.expandNode(node, true, false, true);
				}
			}
		});
	}

	function onClick(event, treeId, treeNode, clickFlag) {
		tree.expandNode(treeNode, true, false, true);
		let fullName = treeNode.fullName!= null && treeNode.fullName !== '组织机构树' ? '(所属机构: ' + treeNode.fullName + ')': '';
		viewModel.groupId(treeNode.id);
		viewModel.groupName(fullName);
		refreshTable();
	}

	/**
	 * 用户列表操作按钮对应事件
	 * id 为用户ID
	 * operator 操作类型 如"edit","delete"等
	 */
	function operatorUser(id, operator) {
		if (id != null) {
			window.location.href = "${ctx}/system/user/" + operator + "/" + id;
		}
	}

	/**
	 * 初始化用户分页列表
	 */
	function initDataTable() {
		var options = {
			divId : "contentTable",
			url : "${ctx}/system/user/search"
			//columns : columns
		};
		createTable(options);
	}

	function editUser(userId) {
		if (userId == null || userId == "") {
			alert("用户ID不能为空");
		} else {
			//showMyModal("${ctx}/system/user/edit/"+userId, "编辑用户", callBackAction);
			window.location.href="${ctx}/system/user/edit/"+userId;
		}
	}

	function deleteUser(userId) {
		if (userId == null || userId == "") {
			alert("用户ID不能为空");
		} else {
			if (window.confirm("确认删除数据?")) {
				var userIds = [userId];
				$.ajax({
					method:'post',
					url:'${ctx}/system/user/delete/'+userIds,
					success:function(data) {
						callBackAction(data);
					}
				});
			}
		}
	}

	function addRole(userId) {
		if (userId == null || userId == "") {
			alert("用户ID不能为空");
		} else {
			//showMyModal("${ctx}/system/user/addRoles/"+userId, "用户授权", callBackAction);
			window.location.href="${ctx}/system/user/addRoles/"+userId;
		}
	}

	function callBackAction(data) {
		refreshTable();
		initGroupTree();
	}
	function formatId(data) {
		return data[0];
	}
	function formatSex(data) {
		if (data.sex == "F") {
			return "<i class='fa fa-female fa-lg green'></i>";
		} else if (data.sex == "M") {
			return "<i class='fa fa-male fa-lg red'></i>";
		}
		return "保密";
	}

	function formatStatus(data) {
		if (data.status == "D") {
			return "<span class='label label-danger'>禁用</span>";
		}
		return "<span class='label label-success'>启用</span>";
	}

	function formatUserType(data) {
		if (data.userType == "Z") {
			return "<span class='label label-success'>正式</span>";
		}
		if (data.userType == "S") {
			return "<span class='label label-danger'>试用</span>";
		}
		return "<span class='label label-danger'>无效</span>";
	}

	function formatOperator(data) {
		var html = "";
		html += "<a href='javascript:void(0)' onclick='viewModel.edit(" + data.id + ")' title='编辑'> <i class='fa fa-edit fa-lg'></i> </a> | ";
		html += "<a href='javascript:void(0)' onclick='viewModel.delete(\"" + data.id + "\")' title='删除'> <i class='fa fa-trash-o fa-lg'></i> </a> ";
	/*	html += "<a href='javascript:void(0)' onclick='addRole(\"" + data.id + "\")' title=''> <i class='fa fa-tag fa-lg'></i> </a>";*/
		return html;
	}

	function say(name) {
		name += '..'
		console.log('hello' + name)
	}
</script>
</html>
