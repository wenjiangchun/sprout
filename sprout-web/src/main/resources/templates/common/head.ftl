<#assign ctx=ctx.contextPath/>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<!-- Bootstrap 3.3.7 -->
<link rel="stylesheet" href="${ctx}/res/lib/bootstrap/dist/css/bootstrap.min.css">
<!-- Font Awesome -->
<link rel="stylesheet" href="${ctx}/res/lib/font-awesome/css/font-awesome.min.css">
<!-- Ionicons -->
<link rel="stylesheet" href="${ctx}/res/lib/Ionicons/css/ionicons.min.css">
<!-- Theme style -->
<link rel="stylesheet" href="${ctx}/res/adminLTE/dist/css/AdminLTE.css">
<!-- AdminLTE Skins. Choose a skin from the css/skins
     folder instead of downloading all of them to reduce the load. -->
<link rel="stylesheet" href="${ctx}/res/adminLTE/dist/css/skins/_all-skins.min.css">

<!-- Date Picker -->
<link rel="stylesheet"
      href="${ctx}/res/lib/bootstrap-datepicker/dist/css/bootstrap-datepicker.min.css">
<!-- Daterange picker -->
<link rel="stylesheet" href="${ctx}/res/lib/bootstrap-daterangepicker/daterangepicker.css">
<link rel="stylesheet" href="${ctx}/res/lib/select2/dist/css/select2.min.css">
<link rel="stylesheet" href="${ctx}/res/css/main.css">

<script src="${ctx}/res/lib/jquery/dist/jquery.min.js"></script>

<script src="${ctx}/res/lib/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="${ctx}/res/lib/bootstrap-daterangepicker/daterangepicker.js"></script>
<script src="${ctx}/res/lib/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js"></script>
<script src="${ctx}/res/lib/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script src="${ctx}/res/adminLTE/dist/js/adminlte.min.js"></script>

<script src="${ctx}/res/adminLTE/dist/js/demo.js"></script>
<script src="${ctx}/res/layer/layer.js"></script>
<#--<script src="${ctx}/res/lib/PACE/pace.min.js"></script>-->
<script src="${ctx}/res/lib/knockout/knockout-3.5.0.js"></script>
<script src="${ctx}/res/lib/select2/dist/js/select2.full.min.js"></script>
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