<#assign ctx=context.contextPath/>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>平台 | 登录</title>
    <link rel="icon" href="${ctx}/res/img/favicon.ico">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="stylesheet" href="${ctx}/res/lib/bootstrap/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctx}/res/lib/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="${ctx}/res/lib/Ionicons/css/ionicons.min.css">
    <link rel="stylesheet" href="${ctx}/res/adminLTE/dist/css/AdminLTE.min.css">
    <link rel="stylesheet" href="${ctx}/res/adminLTE/plugins/iCheck/square/blue.css">
    <style>
        .login-box-body{filter:alpha(Opacity=40);-moz-opacity:0.5;opacity: 0.9;}
    </style>
    <script type="text/javascript">
        <!-- 解决登录页面嵌套问题 -->
        if (window !== top){
            top.location.href = location.href;
        }
    </script>
</head>
<body class="hold-transition login-page" style="background-image: url('https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0137755bb2433fa8012099c80f9faa.jpg%402o.jpg&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1634527660&t=429e5fe6e52f5a6bdfa895af03f2526c')" >
<div class="login-box">
    <div class="login-logo">
        <a href="#" style="color: white">平台登录</a>
    </div>
    <!-- /.login-logo -->
    <div class="login-box-body">
        <p class="login-box-msg">输入登录信息</p>
        <form action="${ctx}/login" method="post" onsubmit="return validator()">
            <div class="form-group has-feedback">
                <input type="text" class="form-control" placeholder="请输入账号" id="username" name="username"/>
                <span class="glyphicon glyphicon-user form-control-feedback"></span>
            </div>
            <div class="form-group has-feedback">
                <input type="password" class="form-control" placeholder="请输入密码" id="password" name="password"/>
                <span class="glyphicon glyphicon-lock form-control-feedback"></span>
            </div>
            <div class="row">
                <div class="col-xs-8">
                    <div class="checkbox icheck">
                        <label>
                            <input type="checkbox"> 记住我
                        </label>
                    </div>
                </div>
                <div class="col-xs-4">
                    <button type="submit" class="btn btn-primary btn-block btn-flat">登录</button>
                </div>
            </div>
            <div class="row">
                <span style="color: red;font-size: 14px;padding-left:20px" data-bind="text: errorMessage"></span>
            </div>
        </form>
    </div>
</div>
<script src="${ctx}/res/lib/jquery/dist/jquery.min.js"></script>
<script src="${ctx}/res/lib/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="${ctx}/res/adminLTE/plugins/iCheck/icheck.min.js"></script>
<script src="${ctx}/res/lib/knockout/knockout-3.5.0.js"></script>
<script>
    let errorViewModel;
    $(function() {
        errorViewModel = {
            errorMessage: ko.observable('${error!}')
        };
        ko.applyBindings(errorViewModel);
        $('input').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
            increaseArea: '20%'
        });
        /*$(window).resize(function() {
            var $content = $('.dl');
            $content.height($(this).height());
        }).resize();*/
    });
    function validator() {
        const username = $("#username").val();
        const password = $("#password").val();
        if (username === '' || password === '') {
            errorViewModel.errorMessage("用户名或密码不能为空!");
            return false;
        }
        return true;
    }
</script>
</body>
</html>