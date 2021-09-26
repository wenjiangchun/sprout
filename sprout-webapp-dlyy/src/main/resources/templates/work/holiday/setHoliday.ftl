<!DOCTYPE html>
<html>
<head>
  <#include "../../common/head.ftl"/>
  <!-- fullCalendar -->
  <link rel="stylesheet" href="${ctx}/res/lib/fullcalendar/dist/fullcalendar.min.css">
  <link rel="stylesheet" href="${ctx}/res/lib/fullcalendar/dist/fullcalendar.print.min.css" media="print">
</head>
<body class="hold-transition skin-blue sidebar-mini">
    <section class="content-header">
      <ol class="breadcrumb">
        <li><a href="javascript:void(0)" onclick="top.location.href='${ctx}/'"><i class="fa fa-dashboard"></i> 主页</a></li>
        <li><a href="#">工作管理</a></li>
        <li class="active">节假日管理</li>
      </ol>
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-md-3">
          <div class="box box-solid">
            <div class="box-header with-border">
              <h4 class="box-title">节假日类型</h4>
              <div class="pull-right box-tools">
                <!-- button with a dropdown -->
                <div class="btn-group">
                  <button type="button" class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                    <i class="fa fa-bars"></i></button>
                  <ul class="dropdown-menu pull-right" role="menu">
                    <li><a href="#" id="batchGenerateHoliday"> 生成当年数据</a></li>
                    <li class="divider"></li>
                    <li><a href="#">查看日历</a></li>
                  </ul>
                </div>
                <button type="button" class="btn btn-default btn-sm" data-widget="collapse"><i class="fa fa-minus"></i>
                </button>
              </div>
            </div>
            <div class="box-body">
              <!-- the events -->
              <div id="external-events">
                <#--<div class="external-event bg-green">春节
                </div>
                <div class="external-event bg-yellow">清明节</div>
                <div class="external-event bg-aqua">端午节</div>
                <div class="external-event bg-light-blue">中秋节</div>
                <div class="external-event bg-red">国庆节</div>
                <div class="external-event bg-orange">元旦</div>
                <div class="external-event bg-purple">周末</div>-->
              </div>
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /. box -->
          <div class="box box-solid">
            <div class="box-header with-border">
              <h3 class="box-title">添加节日类型</h3>
            </div>
            <div class="box-body">
              <div class="btn-group" style="width: 100%; margin-bottom: 10px;">
                <!--<button type="button" id="color-chooser-btn" class="btn btn-info btn-block dropdown-toggle" data-toggle="dropdown">Color <span class="caret"></span></button>-->
                <ul class="fc-color-picker" id="color-chooser">
                  <li><a class="text-aqua" href="#"><i class="fa fa-square"></i></a></li>
                  <li><a class="text-blue" href="#"><i class="fa fa-square"></i></a></li>
                  <li><a class="text-light-blue" href="#"><i class="fa fa-square"></i></a></li>
                  <li><a class="text-teal" href="#"><i class="fa fa-square"></i></a></li>
                  <li><a class="text-yellow" href="#"><i class="fa fa-square"></i></a></li>
                  <li><a class="text-orange" href="#"><i class="fa fa-square"></i></a></li>
                  <li><a class="text-green" href="#"><i class="fa fa-square"></i></a></li>
                  <li><a class="text-lime" href="#"><i class="fa fa-square"></i></a></li>
                  <li><a class="text-red" href="#"><i class="fa fa-square"></i></a></li>
                  <li><a class="text-purple" href="#"><i class="fa fa-square"></i></a></li>
                  <li><a class="text-fuchsia" href="#"><i class="fa fa-square"></i></a></li>
                  <li><a class="text-muted" href="#"><i class="fa fa-square"></i></a></li>
                  <li><a class="text-navy" href="#"><i class="fa fa-square"></i></a></li>
                </ul>
              </div>
              <!-- /btn-group -->
              <div class="input-group">
                <input id="new-event" type="text" class="form-control" placeholder="请输入节日名称">
                <div class="input-group-btn">
                  <button id="addHolidayItem" type="button" class="btn btn-primary btn-flat">添加</button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="col-md-9">
          <div class="box box-primary">
            <div class="box-body no-padding">
              <!-- THE CALENDAR -->
              <div id="calendar"></div>
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /. box -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->
    </section>
    <!-- /.content -->

<!-- ./wrapper -->

<script src="${ctx}/res/lib/jquery-ui/jquery-ui.min.js"></script>
<script src="${ctx}/res/lib/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script src="${ctx}/res/lib/fastclick/lib/fastclick.js"></script>
<script src="${ctx}/res/lib/moment/moment.js"></script>
<script src="${ctx}/res/lib/fullcalendar/dist/fullcalendar.min.js"></script>
<script src="${ctx}/res/lib/fullcalendar/dist/locale/zh-cn.js"></script>
<!-- Page specific script -->
<script>
  let viewModel = {
    getAll: function() {
      let events = [];
      $('#calendar').fullCalendar('removeEvents');
      $.get('${ctx}/work/holiday/findAll', function(data) {
        _.each(data, function(dt,idx) {
          let holidayEvent = {
            id             : dt.id,
            title          : dt.holidayItem.name,
            start          : new Date(dt.year, dt.month - 1, dt.day),
            backgroundColor: dt.holidayItem.color, //red
            borderColor    : dt.holidayItem.color,
            workDay        : dt.workDay//red,
          };
          events.push(holidayEvent);
          /*if (idx === 0) {
            let holidayEvent = {
              title          : dt.holidayItem.name,
              start          : new Date(dt.year, dt.month, dt.day),
              backgroundColor: dt.color, //red
              borderColor    : dt.color,
              workDay        : dt.workDay//red
            };
            events.push(holidayEvent);
          } else {
            //计算日期是否连续 连续的话将日期事件合并一个
            let lastEvent = _.last(events);
            if (lastEvent.title === dt.holidayItem.name && viewModel.isNextDay(lastEvent.workDay, dt.workDay)) {
              lastEvent.workDay = dt.workDay;
              lastEvent.end = dt.workDay;
            } else {
              let holidayEvent = {
                title          : dt.holidayItem.name,
                start          : dt.workDay,
                backgroundColor: dt.holidayItem.color,
                borderColor    : dt.holidayItem.color,
                workDay        : dt.workDay
              };
              events.push(holidayEvent);
            }
          }*/
        });
        //处理events
        _.each(events, function(eventObject,idx) {
          $('#calendar').fullCalendar('renderEvent', eventObject, true)
        });
      });
    },
    isNextDay: function(firstDay, nextDay) {
      //TODO 待处理
      return false;
    },
    checkDay: function(day) {
      alert(day)
      let checked = false;
      $.get('${ctx}/work/holiday/checkWorkDay', {'workDay': day}, function(data){
        checked = data;
      });
      return checked;
    },
    getHolidayItemList: function() {
      let that = this;
      $('#external-events').html('');
      $.get('${ctx}/work/holiday/getHolidayItemList', function(data) {
        _.each(data, function(item, idx) {
          const currColor = item.color;
          const val = item.name;
          var event = $('<div />')
          event.css({
            'background-color': currColor,
            'border-color'    : currColor,
            'color'           : '#fff'
          }).addClass('external-event')
          event.html(val)
          $('#external-events').prepend(event)
          //Add draggable funtionality
          that.init_event(event)
        });
      });
    },
    init_event: function(ele) {
      ele.each(function () {
        let eventObject = {
          title: $.trim($(this).text()) // use the element's text as the event title
        }
        $(this).data('eventObject', eventObject)
        $(this).draggable({
          zIndex        : 1070,
          revert        : true, // will cause the event to go back to its
          revertDuration: 0  //  original position after the drag
        })
      })
    }
  }

  $(function () {
    /*function init_events(ele) {
      ele.each(function () {
        // create an Event Object (http://arshaw.com/fullcalendar/docs/event_data/Event_Object/)
        // it doesn't need to have a start or end
        var eventObject = {
          title: $.trim($(this).text()) // use the element's text as the event title
        }

        // store the Event Object in the DOM element so we can get to it later
        $(this).data('eventObject', eventObject)

        // make the event draggable using jQuery UI
        $(this).draggable({
          zIndex        : 1070,
          revert        : true, // will cause the event to go back to its
          revertDuration: 0  //  original position after the drag
        })

      })
    }*/



    /* initialize the calendar
     -----------------------------------------------------------------*/
    //Date for the calendar events (dummy data)
    var date = new Date()
    var d    = date.getDate(),
        m    = date.getMonth(),
        y    = date.getFullYear()
    $('#calendar').fullCalendar({
      defaultView: 'month',
      contentHeight: 600,
      locale: 'zh-cn',
      header    : {
        left  : 'prev,next today',
        center: 'title'
        //right : 'month,agendaWeek,agendaDay'
      },
     /* buttonText: {
        today: '今天',
        month: '月',
        week : '星期',
        day  : '天'
      },*/
      //Random default events
      /*events    : [
        {
          title          : 'All Day Event',
          start          : new Date(y, m, 1),
          backgroundColor: '#f56954', //red
          borderColor    : '#f56954' //red
        }
      ],*/
      editable  : true,
      droppable : true,
      eventDragStop: function(event, jsEvent, ui, view) {
          console.log(event);
      },
      eventClick: function(calEvent, jsEvent, view) {
        layer.confirm('删除当前节假日？', {
          btn: ['确定','取消'] //按钮
        }, function(){
          $.get('${ctx}/work/holiday/deleteHoliday?workDay=' + calEvent.workDay, function(data){
            if (data.flag) {
              layer.alert('删除成功');
              $('#calendar').fullCalendar('removeEvents', calEvent.id);
            }
         }) ;
        }, function(){

        });
      },
      eventAllow: function(dropLocation, draggedEvent) {
        return false;
      },
      drop      : function (date, allDay) {
        var originalEventObject = $(this).data('eventObject')
        var copiedEventObject = $.extend({}, originalEventObject)
        copiedEventObject.start           = date
        copiedEventObject.allDay          = allDay
        copiedEventObject.backgroundColor = $(this).css('background-color')
        copiedEventObject.borderColor     = $(this).css('background-color')
        $.get('${ctx}/work/holiday/checkWorkDay?workDay=' + date, function(checked){
          if(checked) {
            $.post('${ctx}/work/holiday/saveHoliday?itemName=' + originalEventObject.title + '&workDay=' + copiedEventObject.start, function(data) {
              if (data.id != null) {
                copiedEventObject.id = data.id;
                copiedEventObject.workDay = data.workDay;
                $('#calendar').fullCalendar('renderEvent', copiedEventObject, true)
              }
            })
          }
        });
      }
    })

    /* ADDING EVENTS */
    var currColor = '#3c8dbc' //Red by default
    //Color chooser button
    var colorChooser = $('#color-chooser-btn')
    $('#color-chooser > li > a').click(function (e) {
      //e.preventDefault()
      //Save color
      currColor = $(this).css('color')
      //Add color effect to button
      $('#addHolidayItem').css({ 'background-color': currColor, 'border-color': currColor })
    })
    $('#addHolidayItem').click(function (e) {
      //e.preventDefault()
      //Get value and make sure it is not null
      var val = $('#new-event').val()
      if (val.length === 0) {
        return
      }
      $.post('${ctx}/work/holiday/saveHolidayItem', {color: currColor, name: val}, function(data) {
        if (data.flag) {
          viewModel.getHolidayItemList()
          //重新绘制
          viewModel.getAll();
          $('#new-event').val('')
        }
      });
    })

    $('#batchGenerateHoliday').click(function() {
      $.get('${ctx}/work/holiday/generateHoliday', function(data){
        if (data.flag) {
          layer.alert(data.content);
          viewModel.getAll();
        } else {
          layer.alert(data.content);
        }
      });
    });
    viewModel.getAll();
    viewModel.getHolidayItemList();
    viewModel.init_event($('#external-events div.external-event'))
  })

</script>
</body>
</html>
