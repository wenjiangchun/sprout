<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>考勤记录</title>
    <#include "../../common/head.ftl"/>
    <#include "../../common/datatable.ftl"/>
    <#include "../../common/ztree.ftl"/>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<section class="content-header">
    <ol class="breadcrumb">
        <li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
        <li><a href="#">考勤记录</a></li>
        <li class="active">视频打卡汇总</li>
    </ol>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">视频打卡汇总</h3>
                    <div class="box-tools">
                    </div>
                    <form class="form-inline" style="margin: 5px">
                        <div class="form-group">
                            <input type="date" class="form-control" id="startDate" value="${firstDayOfCurrentMonth?string('yyyy-MM-dd')}"/>
                        </div>
                        <div class="form-group">
                            <input type="date" class="form-control" id="endDate" value="${currentDayOfCurrentMonth?string('yyyy-MM-dd')}"/>
                        </div>
                        <button type="button" class="btn btn-primary" id="searchBtn"><i class="fa fa-search"></i> 查询</button>
                        <#--<button type="button" class="btn btn-warning" id="exportBtn"><i class="fa fa-cloud-download"></i> 导出</button>-->
                    </form>
                </div>
                <!-- /.box-header -->
                <div class="box-body">
                    <div class="row">
                        <div class="col-xs-6">
                            <table id="myTable" class="table table-bordered table-striped table-hover table-responsive" style="padding: 10px;">
                                <thead>
                                <tr>
                                    <th>姓名</th>
                                    <th>正常(次)</th>
                                    <th>迟到(次)</th>
                                    <th>早退(次)</th>
                                    <th>缺勤(次)</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                        <div class="col-xs-6">
                            <div id="echarts" style="width: 100%; height: 450px">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <#--<a href="#" class="btn btn-primary" data-bind='click: add'><i class="fa fa-plus-circle"></i>  添加项目</a>-->
        </div>
    </div>
</section>
</body>
<script src="${ctx}/res/lib/echarts/echarts.min.js"></script>
<script>
    $(document).ready(function() {
        /*$.get("${ctx}/getRecordList", function(data) {
            var myViewModel = {
                records: ko.observableArray(data)
            };
            ko.applyBindings(myViewModel);
        })*/
        $("#searchBtn").click(function() {
            search();
        });
        $("#exportBtn").click(function() {
            let startDate = $("#startDate").val();
            let endDate = $("#endDate").val();
            window.location.href = "${ctx}/dlyy/door/exportExcel?startDate=" + startDate + "&endDate=" + endDate;
        });
        search();
    });

    function search() {
        $('tbody').html('');
        let startDate = $("#startDate").val();
        let endDate = $("#endDate").val();
        //计算日期序列
        //获取总列数
        const columnLength = $("thead").find("th").length;
        console.log('总列数为' + columnLength);
        $.get("${ctx}/dlyy/monitor/getTotalCount", {"startDate" : startDate, "endDate": endDate}, function(data) {
            for (let i = 0; i < data.length; i++) {
                let $tr = $("<tr>");
                const record = data[i];
                $tr.append("<td>" + record.workerName + "</td>");
                $tr.append("<td>" + record.normalCount + "</td>");
                $tr.append("<td>" + record.lateCount + "</td>");
                $tr.append("<td>" + record.earlyCount + "</td>");
                $tr.append("<td>" + record.absenceCount + "</td>");
                $('tbody').append($tr);
            }
            renderChart(data);
        })
    }

    function renderChart(data) {
        var chartDom = document.getElementById('echarts');
        var myChart = echarts.init(chartDom);
        var dt = [];
        dt.push(['人员', '正常', '迟到', '早退', '缺勤']);
        for (var i = 0; i < data.length; i++) {
            var d = [data[i].workerName, data[i].normalCount, data[i].lateCount,data[i].earlyCount, data[i].absenceCount];
            dt.push(d);
        }
        option = {
            title: {
                //left: 'center',
                text: '打卡汇总统计图'
            },
            toolbox: {
                feature: {
                    saveAsImage: {}
                }
            },
            legend: {},
            tooltip: {},
            dataset: {
                source: dt
            },
            color:['green','orange','#00f0ef','#dd4b39'],
            xAxis: {type: 'category'},
            yAxis: {
                type: 'value',
                axisLabel: {
                    formatter: '{value} 次'
                }
            },
            // Declare several bar series, each will be mapped
            // to a column of dataset.source by default.
            series: [
                {type: 'bar'},
                {type: 'bar'},
                {type: 'bar'},
                {type: 'bar'}
            ]
        };
        option && myChart.setOption(option);
    }
</script>
</html>