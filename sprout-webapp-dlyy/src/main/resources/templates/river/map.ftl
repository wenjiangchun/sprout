<!DOCTYPE html>
<html lang="en">
<head>
    <#assign ctx=ctx.contextPath/>

    <script src="${ctx}/res/lib/jquery/dist/jquery.min.js"></script>


    <script src="${ctx}/res/layer/layer.js"></script>
    <#--<script src="${ctx}/res/lib/PACE/pace.min.js"></script>-->

    <script src="${ctx}/res/lib/underscore/underscore-min.js"></script>
    <script type="text/javascript">
        function initMenu(menuId) {
            let parent = $("#" + menuId).parent().parent().parent();
            $(".sidebar-menu").find(".active").removeClass("active menu-open");
            parent.addClass("active menu-open");
            $("#" + menuId).parent().addClass("active");
        }

        let myModel = {};

        function showMyModel(url, title, width, height, callBack) {
            myModel.id = layer.open({
                type: 2,
                title: title,
                shadeClose: true,
                shade: 0.8,
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
    </script>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
        body, html, #allmap {
            width: 100%;
            height: 750px;
            overflow: hidden;
            margin: 0;
            font-family: "微软雅黑";
        }
    </style>
    <script type="text/javascript" src="//api.map.baidu.com/api?v=2.0&ak=BTpF9QcKuvpSFUrG7dwWk8gxjx8HmW4w"></script>
    <title>地图展示</title>
</head>
<body>
<div id="allmap"></div>
</body>
<script type="text/javascript">
    // 百度地图API功能
    var map = new BMap.Map("allmap");    // 创建Map实例
    map.centerAndZoom(new BMap.Point(113.662911, 34.913763), 14);  // 初始化地图,设置中心点坐标和地图级别
    //添加地图类型控件
    /*map.addControl(new BMap.MapTypeControl({
        mapTypes:[
            BMAP_HYBRID_MAP,
            BMAP_NORMAL_MAP
        ]}));*/
    map.setCurrentCity("郑州");
    map.enableScrollWheelZoom(true);
    map.setMapType(BMAP_SATELLITE_MAP);

    var menu = new BMap.ContextMenu();
    var txtMenuItem = [
        {
            text: '放大地图',
            callback: function (point) {
                console.log(point);
                map.zoomIn();
            }
        }, {
            text: '缩小地图',
            callback: function () {
                map.zoomOut();
            }
        }, {
            text: '添加项目',
            callback: function (point) {
                //var marker = new BMap.Marker(point);
                //map.addOverlay(marker);
                const url = "${ctx}/river/project/addProject?x="+ point.lng + '&y=' + point.lat;
                showMyModel(url,'添加项目信息', '900px', '80%');
            }
        }
    ];
    //const iconUrls = ['${ctx}/res/img/zoomIn.png','${ctx}/res/img/zoomOut.png',BMAP_CONTEXT_MENU_ICON_ZOOMIN]
    const iconUrls = [BMAP_CONTEXT_MENU_ICON_ZOOMIN,BMAP_CONTEXT_MENU_ICON_ZOOMOUT,'${ctx}/res/img/zoomIn.png']
    for (var i = 0; i < txtMenuItem.length; i++) {
        menu.addItem(new BMap.MenuItem(txtMenuItem[i].text, txtMenuItem[i].callback, {'width':'100px', 'iconUrl': iconUrls[i]}));
        if (i === 1) {
            menu.addSeparator();
        }
    }
    map.addContextMenu(menu);


    function addMarker(project) {
        if (project != null) {
            let marker = new BMap.Marker(new BMap.Point(project.x, project.y));
            marker.id = project.id;
            marker.addEventListener('click', function(dt) {
                const url = "${ctx}/river/project/showProject?id="+ marker.id;
                showMyModel(url,'查看项目信息', '900px', '80%');
            });
            map.addOverlay(marker);

            let label = new BMap.Label(project.name,{'offset':new BMap.Size(20, -10)});
            label.setStyle({
                backgroundColor:'transparent',
                border:'0px dashed #000',
                color : "white",
                fontSize : "16px",
                fontWeight: "bold",
                height : "20px",
                lineHeight : "20px",
                fontFamily:"微软雅黑"
            });
            marker.setLabel(label);

        }
    }

    function removeMarker(id) {
        console.log('id=' + id);
        $.post('${ctx}/river/project/deleteProject',{'id': id}, function(data){
            if (data.flag) {
                let markers = map.getOverlays();
                const project = _.find(markers, function(marker){ return marker.id == id; });
                console.log(project.id)
                map.removeOverlay(project);
                layer.alert("删除成功");
            } else {
                layer.alert("删除失败，请重试！");
            }
        });
    }
    $.get('${ctx}/river/getProjectList', function(data){
        for(let i = 0; i < data.length; i++) {
            addMarker(data[i]);
        }
    });
</script>
</html>