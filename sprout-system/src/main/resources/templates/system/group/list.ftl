<!DOCTYPE html>
<html>
<head>
<title>机构管理</title>
	<#include "../../common/head.ftl"/>
	<#include "../../common/datatable.ftl"/>
	<#include "../../common/ztree.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<ol class="breadcrumb">
				<li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
				<li><a href="#">系统管理</a></li>
				<li class="active">机构管理</li>
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
						<!-- /.box-body -->
					</div>
				</div>
				<div class="col-xs-9">
					<div class="box">
						<div class="box-header">
							<h3 class="box-title">机构列表 <span data-bind="text: groupName"></span></h3>
							<div class="box-tools">
							</div>
								<form class="form-inline">
									<input type="hidden" class="datatable_query" name="parent.id" data-bind="value: groupId"/>
									<div class="box-body">
										<div class="form-group">
											<label for="fullName">机构名称</label>
											<input type="text" id="fullName" name="fullName_like" class="datatable_query form-control">
										</div>
										<button type="button" class="btn btn-sm btn-primary" onclick="refreshTable();" style="margin-left:5px;">
											<i class="fa fa-search"></i> 查询
										</button>
										<button type="button" class="btn btn-sm btn-danger" onclick="" style="margin-left:10px;">清空</button>
									</div>
								</form>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<table id="contentTable" class="table table-bordered table-striped table-hover">
								<thead>
								<tr>
									<th sName="id" type="checkbox">编号</th>
									<th sName="fullName">机构全称</th>
									<th sName="name">机构简称</th>
									<th sName="groupType.name">机构类型</th>
									<th sName="code">机构代码</th>
									<th sName="tel">固定电话</th>
									<th sName="operator" columnRender="formatOperator">操作</th>
								</tr>
								</thead>
							</table>
						</div>
						<!-- /.box-body -->
					</div>
					<!-- /.box -->
					<a href="#" class="btn btn-primary" data-bind='click: add'><i class="fa fa-plus-circle"></i>  添加机构</a>
				</div>
				<!-- /.col -->
			</div>
			<!-- /.row -->
		</section>
</body>
<script type="text/javascript">
	let currentViewModel;
	let tree;
	$(document).ready(function() {
		currentViewModel = {
			groupName: ko.observable(''),
			groupId: ko.observable('${parentId!}'),
			add: function() {
				let url = "${ctx}/system/group/add";
				if (this.groupId() != null && this.groupId() !== "") {
					url += "?parentId=" + this.groupId();
				}
				top.showMyModel(url,'添加机构', '800px', '60%', callBackAction);
		    }
		};
		ko.applyBindings(currentViewModel);
		initGroupTree();
		initDataTable();
		$("#clearBtn").click(function() { //清空按钮事件
			$(".databatle_query").val("");
		});
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
				var parentId = currentViewModel.groupId();
				if (parentId != null && parentId != "") {
					var node = tree.getNodeByParam("id",parentId);
					if(!node.isParent){
						node = node.getParentNode();
					}
					tree.selectNode(node,false);
					tree.expandNode(node, true, false, true);
				} else {
					var node = tree.getNodeByParam("fullName","组织机构树");
					tree.expandNode(node, true, false, true);
				}
			}
		});
	}

	function onClick(event, treeId, treeNode, clickFlag) {
		//currentViewModel.id = treeNode.id;
		tree.expandNode(treeNode, true, false, true);
		let fullName = treeNode.fullName!= null && treeNode.fullName !== '组织机构树' ? '(' + treeNode.fullName + ')': '';
		currentViewModel.groupId(treeNode.id);
		currentViewModel.groupName(fullName);
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
			url : "${ctx}/system/group/search"
		};
		createTable(options);
	}

	function deleteGroup(id){
		$.post("${ctx}/system/group/delete/"+id, function(data){
			if(data.messageType == "SUCCESS"){
				//alert("操作成功!");
			}else{
				alert("操作失败:" + data.content);
			}
			refreshTable();
			initGroupTree();
		});
	}

	function removeTreeNodeByNodeId(id){
		var treeObj = $.fn.zTree.getZTreeObj("groupTree");
		if (id != null && id != "") {
			var node = treeObj.getNodeByParam("id", id);
			if(node != null){
				treeObj.removeNode(node);
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
		/*if (data != undefined) {
			$("#parent_ID").val(data);
		}
		window.location.href="${ctx}/system/group/view?parentId=" + $("#parent_ID").val();*/
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

	function editGroup(groupId) {
		let url = "${ctx}/system/group/edit/ "+ groupId;
		top.showMyModel(url,'修改机构', '800px', '60%', callBackAction);
	}
	function formatOperator(data) {
		var html = "";
		html += "<a href='javascript:void(0)' onclick='editGroup(\"" + data.id + "\")' title=''> <i class='fa fa-edit fa-lg'></i> </a> | ";
		html += "<a href='javascript:void(0)' onclick='deleteGroup(\"" + data.id + "\")' title=''> <i class='fa fa-trash-o fa-lg'></i> </a>";
		/*html += "<a href='javascript:void(0)' onclick='addRole(\"" + data.id + "\")' title=''> <i class='fa fa-tag fa-lg'></i> </a>";*/
		return html;
	}
</script>
</html>
