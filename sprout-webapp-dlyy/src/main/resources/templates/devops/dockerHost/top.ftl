<!DOCTYPE html>
<html lang="zh">
<head>
    <title>镜像容器管理</title>
    <#include "../../common/head.ftl"/>
    <#include "../../common/form.ftl"/>
    <#include "../../common/datatable.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content">
    <div class="row">
        <div class="col-xs-12">
                <div class="box box-info" id="container">
                    <table class="table table-bordered">
                        <thead>
                        <tr data-bind="foreach:topTitles">
                            <th data-bind="text: $data"></th>
                        </tr>
                        </thead>
                        <tbody data-bind="foreach:topDts">
                        <tr data-bind="foreach:$data">
                            <td data-bind="text:$data"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
        </div>
    </div>
</section>
<script>
    let viewModel = {
        weekDay: ko.observable(''),
        topTitles: ko.observableArray([]),
        topDts: ko.observableArray([]),
        getTops: function() {
            $.get('${ctx}/devops/docker/getContainerInfo/${containerId}/top', {'ip': '${dockerHost.ip}', 'port': '${dockerHost.port}'},function(dts) {
                /*_.each(dts, function(dt) {

                });*/
                viewModel.topTitles(dts.Titles);
                viewModel.topDts(dts.Processes);
                console.log(dts);
            });
        },

    }
    ko.applyBindings(viewModel);
    $(function() {
        viewModel.getTops();
    });
    function timestampToTime(timestamp) {
        var date = new Date(timestamp * 1000);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
        var Y = date.getFullYear() + '-';
        var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        var D = date.getDate() + ' ';
        var h = date.getHours() + ':';
        var m = date.getMinutes() + ':';
        var s = date.getSeconds();
        return Y+M+D+h+m+s;
    }
</script>
</body>
</html>
