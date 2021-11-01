<#assign ctx=context.contextPath/>
<!DOCTYPE html>
<html lang="zh">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <link rel="icon" href="${ctx}/res/img/favicon.ico">
  <title>东龙优易办公平台</title>
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <link rel="stylesheet" href="${ctx}/res/lib/bootstrap/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="${ctx}/res/lib/font-awesome/css/font-awesome.min.css">
  <link rel="stylesheet" href="${ctx}/res/lib/Ionicons/css/ionicons.min.css">
  <link rel="stylesheet" href="${ctx}/res/adminLTE/dist/css/AdminLTE.min.css">
  <link rel="stylesheet" href="${ctx}/res/adminLTE/dist/css/skins/_all-skins.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

  <header class="main-header">
    <#--<a href="#" class="logo">
      <span class="logo-mini">东龙优易</span>
      <span class="logo-lg"><b>Sprout</b>管理平台</span>
    </a>-->
    <a href="#" class="logo">
      <span class="logo-mini"><img src="${ctx}/res/img/dept-logo.png"></span>
      <span class="logo-lg"><img src="${ctx}/res/img/dept-logo.png">办公平台</span>
    </a>
    <nav class="navbar navbar-static-top">
      <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
        <span class="sr-only">Toggle navigation</span>
      </a>

      <div class="navbar-custom-menu">
        <ul class="nav navbar-nav">
          <li class="dropdown messages-menu">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <i class="fa fa-envelope-o"></i>
              <span class="label label-success">1</span>
            </a>
            <ul class="dropdown-menu">
              <li class="header">You have 1 messages</li>
              <li>
                <ul class="menu">
                  <li>
                    <a href="#">
                      <div class="pull-left">
                        <img src="${ctx}/res/adminLTE/dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
                      </div>
                      <h4>
                        Support Team
                        <small><i class="fa fa-clock-o"></i> 5 mins</small>
                      </h4>
                      <p>Why not buy a new awesome theme?</p>
                    </a>
                  </li>
                </ul>
              </li>
              <li class="footer"><a href="#">See All Messages</a></li>
            </ul>
          </li>
          <li class="dropdown notifications-menu">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <i class="fa fa-bell-o"></i>
              <span class="label label-warning" data-bind="text:noticeList().length"></span>
            </a>
            <ul class="dropdown-menu">
              <li class="header">您有 <span data-bind="text:noticeList().length"></span>条通知信息</li>
              <li>
                <ul class="menu">
                  <li>
                    <a href="#">
                      <i class="fa fa-warning text-yellow"></i> Very long description here that may not fit into the
                      page and may cause design problems
                    </a>
                  </li>
                </ul>
              </li>
              <li class="footer"><a href="#">View all</a></li>
            </ul>
          </li>
          <li class="dropdown tasks-menu">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <i class="fa fa-flag-o"></i>
              <span class="label label-danger" data-bind="text:todoList().length"></span>
            </a>
            <ul class="dropdown-menu">
              <li class="header">您有 <span data-bind="text:todoList().length"></span>条待办信息</li>
              <li>
                <ul class="menu" data-bind="foreach:todoList">
                  <li>
                    <a href="#">
                      <h3>
                        <span data-bind="text:$data.taskName"></span>
                        <small class="pull-right" data-bind="text:$data.taskTime"></small>
                      </h3>
                    </a>
                  </li>
                  <#--<li>
                    <a href="#">
                      <h3>
                        Create a nice theme
                        <small class="pull-right">40%</small>
                      </h3>
                      <div class="progress xs">
                        <div class="progress-bar progress-bar-green" style="width: 40%" role="progressbar"
                             aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                          <span class="sr-only">40% Complete</span>
                        </div>
                      </div>
                    </a>
                  </li>-->
                </ul>
              </li>
              <li class="footer">
                <a href="#">View all tasks</a>
              </li>
            </ul>
          </li>
          <li class="dropdown user user-menu">
            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
              <img src="${ctx}/res/adminLTE/dist/img/avatar5.png" class="user-image" alt="User Image">
              <span class="hidden-xs"><@shiro.principal/></span>
            </a>
            <ul class="dropdown-menu">
              <li class="user-header">
                <img src="${ctx}/res/adminLTE/dist/img/avatar5.png" class="img-circle" alt="User Image">
                <p style="color: #0a0a0a">
                  <@shiro.principal/>
                  <small><@shiro.principal property="groupName"/></small>
                </p>
              </li>
              <li class="user-footer">
                <div class="pull-left">
                  <a href="#" class="btn btn-default btn-flat">个人资料</a>
                </div>
                <div class="pull-right">
                  <a href="${ctx}/logout" class="btn btn-default btn-flat">退出</a>
                </div>
              </li>
            </ul>
          </li>
          <li>
            <a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a>
          </li>
        </ul>
      </div>
    </nav>
  </header>
  <aside class="main-sidebar">
    <section class="sidebar">
      <ul class="sidebar-menu" data-widget="tree">
        <li class="header">导航栏</li>
        <#--<li class="active treeview">
          <a href="#">
            <i class="fa fa-dashboard"></i> <span>主页</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li class="active"><a class="menuBtn" url="${ctx}/dashboard"><i class="fa fa-circle-o"></i> 面板</a></li>
          </ul>
        </li>-->
        <li class="treeview">
          <a href="#">
            <i class="fa fa-laptop"></i>
            <span>系统管理</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li><a href="#" class="menuBtn" url="${ctx}/system/user/view"><i class="fa fa-user-circle"></i> 用户管理</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/system/group/view"><i class="fa fa-users"></i> 组织管理</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/system/role/view"><i class="fa fa-cogs"></i> 角色管理</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/system/resource/view"><i class="fa fa-database"></i> 资源管理</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/system/dict/view"><i class="fa fa-book"></i> 字典管理</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/system/config/view"><i class="fa fa-circle-o"></i> 配置管理</a></li>

          </ul>
        </li>
        <#--<li class="treeview">
          <a href="#">
            <i class="fa fa-anchor"></i>
            <span>数据管理</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li><a href="#" class="menuBtn" url="${ctx}/data/dataSourceMeta/list"><i class="fa fa-circle-o"></i> 数据源管理</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/data/dataSourceNavigate/view"><i class="fa fa-circle-o"></i> 数据浏览</a></li>
          </ul>
        </li>-->
        <li class="treeview">
          <a href="#">
            <i class="fa fa-clone"></i>
            <span>流程管理</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li><a href="#" class="menuBtn" url="${ctx}/flowable/model/view"><i class="fa fa-pencil-square-o"></i> 流程模型管理</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/flowable/processDefinition/view"><i class="fa fa-anchor"></i> 流程定义管理</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/flowable/processInstance/view"><i class="fa fa-list"></i> 流程实例管理</a></li>
          </ul>
        </li>
        <li class="treeview">
          <a href="#">
            <i class="fa fa-reorder"></i>
            <span>工作管理</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li><a href="#" class="menuBtn" url="${ctx}/work/workDairy/view"><i class="fa fa-pencil-square-o"></i> 工作日志填写</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/work/dairySendConfig/view"><i class="fa fa-empire"></i> 日志发送配置</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/work/dairySendLog/view"><i class="fa fa-envelope-o"></i> 日志发送记录</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/work/holiday/setHoliday"><i class="fa fa-calendar"></i> 节假日管理</a></li>
          </ul>
        </li>
        <li class="treeview">
          <a href="#">
            <i class="fa fa-dashcube"></i>
            <span>考勤记录</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <#--<li><a href="#" class="menuBtn" url="${ctx}/dlyy/kudu/view"><i class="fa fa-circle-o"></i> Kudu资源库</a></li>-->
            <li><a href="#" class="menuBtn" url="${ctx}/dlyy/monitor/view"><i class="fa fa-video-camera"></i> 视频打卡记录</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/dlyy/monitor/analysis"><i class="fa fa-bar-chart-o"></i> 视频打卡汇总</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/dlyy/door/view"><i class="fa fa-circle-o"></i> 门禁打卡记录</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/dlyy/monitor/config"><i class="fa fa-cog"></i> 考勤规则设置</a></li>
            <#--<li><a href="#" class="menuBtn" url="${ctx}/dlyy/k8s/view"><i class="fa fa-circle-o"></i> K8s监控</a></li>-->
          </ul>
        </li>
        <li class="treeview">
          <a href="#">
            <i class="fa fa-pencil-square"></i>
            <span>请假管理</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li><a href="#" class="menuBtn" url="${ctx}/oa/leave/getMyLeave"><i class="fa fa-pencil"></i> 我的请假</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/oa/leave/todoView"><i class="fa fa-list"></i> 待办请假</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/oa/leaveStatistic/view"><i class="fa fa-table"></i> 请假汇总</a></li>
          </ul>
        </li>
        <li class="treeview">
          <a href="#">
            <i class="fa fa-pencil-square"></i>
            <span>办公资产</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li><a href="#" class="menuBtn" url="${ctx}/oa/asset/view"><i class="fa fa-pencil"></i> 资产管理</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/oa/asset/addAssetIn"><i class="fa fa-list"></i> 资产入库</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/oa/leave/todoView"><i class="fa fa-list"></i> 资产申请</a></li>
          </ul>
        </li>
        <li class="treeview">
          <a href="#">
            <i class="fa fa-opera"></i>
            <span>运维管理</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li><a href="#" class="menuBtn" url="${ctx}/devops/docker/view"><i class="fa fa-server"></i> Docker主机管理</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/dlyy/kudu/view"><i class="fa fa-circle-o"></i> Kudu资源库</a></li>
            <li><a href="#" class="menuBtn" url="${ctx}/devops/k8s/view"><i class="fa fa-cloud"></i> K8s集群监控</a></li>
          </ul>
        </li>
      </ul>
    </section>
  </aside>
  <div class="content-wrapper">
    <iframe src="${ctx}/dashboard" width="100%" height="800px"  id="content" name="myframe" frameborder="no" border="0"></iframe>
  </div>
  <footer class="main-footer">
    <div class="pull-right hidden-xs">
      <b>Version</b> 1.0.0
    </div>
    <strong>Copyright &copy; 2021 <a href="https://zdlonghu.com">东龙优易</a>.</strong>
  </footer>

  <aside class="control-sidebar control-sidebar-dark" style="display: none;">
    <ul class="nav nav-tabs nav-justified control-sidebar-tabs">
      <li><a href="#control-sidebar-home-tab" data-toggle="tab"><i class="fa fa-home"></i></a></li>
      <li><a href="#control-sidebar-settings-tab" data-toggle="tab"><i class="fa fa-gears"></i></a></li>
    </ul>
    <!-- Tab panes -->
    <div class="tab-content">
      <!-- Home tab content -->
      <div class="tab-pane" id="control-sidebar-home-tab">
        <h3 class="control-sidebar-heading">Recent Activity</h3>
        <ul class="control-sidebar-menu">
          <li>
            <a href="javascript:void(0)">
              <i class="menu-icon fa fa-birthday-cake bg-red"></i>

              <div class="menu-info">
                <h4 class="control-sidebar-subheading">Langdon's Birthday</h4>

                <p>Will be 23 on April 24th</p>
              </div>
            </a>
          </li>
          <li>
            <a href="javascript:void(0)">
              <i class="menu-icon fa fa-user bg-yellow"></i>

              <div class="menu-info">
                <h4 class="control-sidebar-subheading">Frodo Updated His Profile</h4>

                <p>New phone +1(800)555-1234</p>
              </div>
            </a>
          </li>
          <li>
            <a href="javascript:void(0)">
              <i class="menu-icon fa fa-envelope-o bg-light-blue"></i>

              <div class="menu-info">
                <h4 class="control-sidebar-subheading">Nora Joined Mailing List</h4>

                <p>nora@example.com</p>
              </div>
            </a>
          </li>
          <li>
            <a href="javascript:void(0)">
              <i class="menu-icon fa fa-file-code-o bg-green"></i>

              <div class="menu-info">
                <h4 class="control-sidebar-subheading">Cron Job 254 Executed</h4>

                <p>Execution time 5 seconds</p>
              </div>
            </a>
          </li>
        </ul>
        <!-- /.control-sidebar-menu -->

        <h3 class="control-sidebar-heading">Tasks Progress</h3>
        <ul class="control-sidebar-menu">
          <li>
            <a href="javascript:void(0)">
              <h4 class="control-sidebar-subheading">
                Custom Template Design
                <span class="label label-danger pull-right">70%</span>
              </h4>

              <div class="progress progress-xxs">
                <div class="progress-bar progress-bar-danger" style="width: 70%"></div>
              </div>
            </a>
          </li>
          <li>
            <a href="javascript:void(0)">
              <h4 class="control-sidebar-subheading">
                Update Resume
                <span class="label label-success pull-right">95%</span>
              </h4>

              <div class="progress progress-xxs">
                <div class="progress-bar progress-bar-success" style="width: 95%"></div>
              </div>
            </a>
          </li>
          <li>
            <a href="javascript:void(0)">
              <h4 class="control-sidebar-subheading">
                Laravel Integration
                <span class="label label-warning pull-right">50%</span>
              </h4>

              <div class="progress progress-xxs">
                <div class="progress-bar progress-bar-warning" style="width: 50%"></div>
              </div>
            </a>
          </li>
          <li>
            <a href="javascript:void(0)">
              <h4 class="control-sidebar-subheading">
                Back End Framework
                <span class="label label-primary pull-right">68%</span>
              </h4>

              <div class="progress progress-xxs">
                <div class="progress-bar progress-bar-primary" style="width: 68%"></div>
              </div>
            </a>
          </li>
        </ul>
        <!-- /.control-sidebar-menu -->

      </div>
      <!-- /.tab-pane -->
      <!-- Stats tab content -->
      <div class="tab-pane" id="control-sidebar-stats-tab">Stats Tab Content</div>
      <!-- /.tab-pane -->
      <!-- Settings tab content -->
      <div class="tab-pane" id="control-sidebar-settings-tab">
        <form method="post">
          <h3 class="control-sidebar-heading">General Settings</h3>

          <div class="form-group">
            <label class="control-sidebar-subheading">
              Report panel usage
              <input type="checkbox" class="pull-right" checked>
            </label>

            <p>
              Some information about this general settings option
            </p>
          </div>
          <!-- /.form-group -->

          <div class="form-group">
            <label class="control-sidebar-subheading">
              Allow mail redirect
              <input type="checkbox" class="pull-right" checked>
            </label>

            <p>
              Other sets of options are available
            </p>
          </div>
          <!-- /.form-group -->

          <div class="form-group">
            <label class="control-sidebar-subheading">
              Expose author name in posts
              <input type="checkbox" class="pull-right" checked>
            </label>

            <p>
              Allow the user to show his name in blog posts
            </p>
          </div>
          <!-- /.form-group -->
          <h3 class="control-sidebar-heading">Chat Settings</h3>

          <div class="form-group">
            <label class="control-sidebar-subheading">
              Show me as online
              <input type="checkbox" class="pull-right" checked>
            </label>
          </div>
          <!-- /.form-group -->
          <div class="form-group">
            <label class="control-sidebar-subheading">
              Turn off notifications
              <input type="checkbox" class="pull-right">
            </label>
          </div>
          <!-- /.form-group -->

          <div class="form-group">
            <label class="control-sidebar-subheading">
              Delete chat history
              <a href="javascript:void(0)" class="text-red pull-right"><i class="fa fa-trash-o"></i></a>
            </label>
          </div>
          <!-- /.form-group -->
        </form>
      </div>
      <!-- /.tab-pane -->
    </div>
  </aside>
  <!-- /.control-sidebar -->
  <!-- Add the sidebar's background. This div must be placed
       immediately after the control sidebar -->
  <div class="control-sidebar-bg"></div>
</div>
<script src="${ctx}/res/lib/jquery/dist/jquery.min.js"></script>
<script src="${ctx}/res/lib/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="${ctx}/res/adminLTE/dist/js/adminlte.min.js"></script>
<script src="${ctx}/res/layer/layer.js"></script>
<script src="${ctx}/res/lib/knockout/knockout-3.5.0.js"></script>
<script src="${ctx}/res/lib/underscore/underscore-min.js"></script>
<script>
  let viewModel = {
    todoList: ko.observableArray([]),
    noticeList: ko.observableArray([])
  }
  $(function(){
    $(".menuBtn").click(function(){
      let $this = $(this);
      $(".sidebar-menu").find(".active").each(function(idx,el) {
        $(el).removeClass("active");
      });
      $this.parent().addClass("active");
      $this.parent().parent().parent().addClass("active");
      document.getElementById("content").src = $(this).attr("url");
    });
    $(".menuBtn").eq(0).click();
    ko.applyBindings(viewModel);
    initWebSocket();
  });

  let myModel = {};

  function showMyModel(url, title, width, height, callBack) {
    myModel.id = layer.open({
      //skin: 'layui-layer-lan',
      type: 2,
      title: title,
      //shadeClose: false,
      shade: 0.5,
      area: [width, height],
      content: url
    });
    myModel.callBack = callBack;
  }

  function hideMyModal() {
    if (myModel.callBack != null) {
      myModel.callBack.apply(this, arguments);
    }
    layer.close(myModel.id);
  }

  function initWebSocket() {
    if ('WebSocket' in window) {
      websocket = new WebSocket('ws://localhost:8080/websocket/<@shiro.principal property="userId"/>');
      websocket.onopen = function () {
        console.log("连接成功");
        //获取待办事项和通知
        $.get('${ctx}/oa/leave/getTodoList/<@shiro.principal property="userId"/>', function(dts) {
          _.each(dts, function(dt) {
            viewModel.todoList.push({
              taskName: '【' + dt.applier.name + '】' +  dt.runtimeVariables.taskName,
              taskTime: dt.runtimeVariables.taskTime
            });
          });
          console.log(viewModel.todoList().length)
        });
      };

      websocket.onclose = function () {
        console.log("退出连接");
      };

      websocket.onmessage = function (message) {
        console.log(message);
        message = JSON.parse(message.data);
        //console.log("收到消息" + message.payLoad);
        //websocket.send("发送信息至服务器")
        console.log(message);
        if (message.messageType == 0) {
          viewModel.todoList.push({
            taskName: '【' + message.payLoad.applier.name + '】' +  message.payLoad.runtimeVariables.taskName,
            taskTime: message.payLoad.runtimeVariables.taskTime
          });
        }
      };

      websocket.onerror = function () {
        console.log("连接出错");
      };

      window.onbeforeunload = function () {
        websocket.close();
      };
    } else {
      console.warn('当前浏览器不支持websocket')
    }
  }
</script>
</body>
</html>
