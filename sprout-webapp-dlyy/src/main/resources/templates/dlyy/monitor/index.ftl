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
        <li class="active">视频打卡记录</li>
    </ol>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">视频打卡记录</h3>
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
                        <button type="button" class="btn btn-warning" id="exportBtn"><i class="fa fa-cloud-download"></i> 导出</button>
                    </form>
                </div>
                <!-- /.box-header -->
                <div class="box-body">
                    <table id="myTable" class="table table-bordered table-striped table-hover table-responsive" style="padding: 10px">
                        <thead>
                        <tr>
                            <th>日期</th>
                            <#list nameList as name>
                                <th>${name!}</th>
                            </#list>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                        <tfoot>
                        <tr>
                            <th>日期</th>
                            <#list nameList as name>
                                <th>${name!}</th>
                            </#list>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
            <#--<a href="#" class="btn btn-primary" data-bind='click: add'><i class="fa fa-plus-circle"></i>  添加项目</a>-->
        </div>
    </div>
</section>
</body>
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
            window.location.href = "${ctx}/dlyy/monitor/exportExcel?startDate=" + startDate + "&endDate=" + endDate;
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
        $.get("${ctx}/dlyy/monitor/getRecordList", {"startDate" : startDate, "endDate": endDate}, function(data) {
            /*var myViewModel = {
                records: ko.observableArray(data)
            };
            ko.applyBindings(myViewModel);*/
            const dayList = data.dayList;
            const recordList = data.recordList;

            for (let i = 0; i < dayList.length; i++) {
                let $tr = $("<tr>");
                const day = dayList[i];
                $tr.append("<td>" + day + "</td>");
                for (let j = 1; j < columnLength; j++) {
                    //获取当前列头
                    const personName = $("th").eq(j).text();
                    //遍历循环
                    let exist = false;
                    for (let k = 0; k < recordList.length; k++) {
                        //判断数据是否属于该天该人
                        const record = recordList[k];
                        if (record.recordDate === day && record.name === personName) {
                            let content = '上午：' + record.amState + ' <br>下午：' + record.pmState;
                            if (content.indexOf('迟到')!= -1 || content.indexOf('早退') != -1 || content.indexOf('缺勤') != -1) {
                                $tr.append("<td style='background:lightcoral'>" + content + "</td>");
                            } else {
                                $tr.append("<td>" + content + "</td>");
                            }
                            exist = true;
                            break;
                        }
                    }
                    if (!exist) {
                        let content = '';
                        $tr.append("<td>" + content + "</td>");
                    }
                }
                $('tbody').append($tr);
            }
        })

        $('tbody').find("td").each(function(idx, el) {
            console.log($(el).text());
        });
    }
</script>
</html>