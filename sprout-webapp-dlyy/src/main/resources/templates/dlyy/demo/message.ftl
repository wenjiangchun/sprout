<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>RabbitMQ消息发送测试</title>
    <#include "../../common/head.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content-header">
    <ol class="breadcrumb">
        <li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
        <li><a href="#">运维管理</a></li>
        <li class="active">RabbitMQ消息发送测试</li>
    </ol>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box box-default">
                <div class="box-header with-border">
                    <h3 class="box-title">发送消息</h3>
                    <div class="pull-right">
                        <button class="btn btn-box-tool" onclick="window.history.go(-1)"><i class="fa fa-reply"></i> </button>
                    </div>
                </div>
                <form id="inputForm" class="form-horizontal" action="${ctx}/dlyy/demo/sendMessage/" method="post">
                    <div class="box-body">
                        <div class="form-group">
                            <label for="message" class="col-sm-2 control-label">消息内容</label>
                            <div class="col-sm-10">
                                <textarea rows="3" id="message" name="message" class="form-control" maxlength="200" required></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="box-footer">
                        <button type="button" id='sendMessage' class="btn btn-primary pull-right"><i class="fa fa-send-o"></i> 发送</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</section>
</body>
<script>
    $('#sendMessage').click(function(){
        $.post('${ctx}/dlyy/demo/sendMessage?message=' + $('#message').val(), function(data){
            alert('message send success!');
        });
    });
</script>
</html>