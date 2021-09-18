var dataTables;
function createTable(options) {
	var tableId = options.divId;
	var url = options.url;
    var columns = options.columns;
	//var columns = options.columns;
	if (columns == null) {
		layer.alert("未定义表格列");
		return;
	}
	var grid =  $('#'+tableId).DataTable({
		"processing": true,
	    "serverSide": true,
        "searching": false,
        "columns":columns,
        "language": {
            "lengthMenu": "每页显示 _MENU_条记录",
            "zeroRecords": "没有检索到数据",
            "info": "显示第 _START_ - _END_ 条记录；共 _TOTAL_ 条记录",
            "infoEmpty": "",
            "processing": "正在加载数据...",
            "infoFiltered": "",
            "search":"检索：",
            "paginate": {
                "first": "首页",
                "previous": "上一页",
                "next": "下一页",
                "last": '尾页'
            }
        },
        "ajax": {
            "url": url,
            "data": function (d) {
                return addQueryParams(d);
            }
        }
	});
	dataTables = grid;
	return grid;
}

/**
 * 组装页面中列表查询参数
 * @returns {String}
 */
function addQueryParams(d) {
    d['queryParams'] = {};
    let queryParams = {};
	$(".datatable_query").each(function() {
		var name = $(this).attr("name");
		var value = $(this).val();
		if (value !== "" && value != null) {
            queryParams[name] = value;
		}
	});
	d.queryParams = queryParams;
    console.log(d);
	return d;

}

/**
 * 刷新表格
 */
function refreshTable() {
	dataTables.draw();
}
