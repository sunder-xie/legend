<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/resources/js/lib/jqPlot/css/jquery.jqplot.min.css"/>

<!--[if lt IE 9]>
<script language="javascript" type="text/javascript"
        src="${BASE_PATH}/resources/js/lib/jqPlot/excanvas.min.js"></script><![endif]-->
<script language="javascript" type="text/javascript"
        src="${BASE_PATH}/resources/js/lib/jqPlot/jquery.jqplot.min.js"></script>
<script type="text/javascript"
        src="${BASE_PATH}/resources/js/lib/jqPlot/plugins/jqplot.logAxisRenderer.min.js"></script>
<script type="text/javascript"
        src="${BASE_PATH}/resources/js/lib/jqPlot/plugins/jqplot.canvasAxisTickRenderer.min.js"></script>
<script type="text/javascript"
        src="${BASE_PATH}/resources/js/lib/jqPlot/plugins/jqplot.dateAxisRenderer.min.js"></script>

<script type="text/javascript"
        src="${BASE_PATH}/resources/js/lib/jqPlot/plugins/jqplot.canvasTextRenderer.min.js"></script>
<script type="text/javascript"
        src="${BASE_PATH}/resources/js/lib/jqPlot/plugins/jqplot.canvasAxisLabelRenderer.min.js"></script>
<script language="javascript" type="text/javascript"
        src="${BASE_PATH}/resources/js/lib/jqPlot/plugins/jqplot.barRenderer.min.js"></script>
<script language="javascript" type="text/javascript"
        src="${BASE_PATH}/resources/js/lib/jqPlot/plugins/jqplot.pieRenderer.min.js"></script>
<script language="javascript" type="text/javascript"
        src="${BASE_PATH}/resources/js/lib/jqPlot/plugins/jqplot.categoryAxisRenderer.min.js"></script>
<script language="javascript" type="text/javascript"
        src="${BASE_PATH}/resources/js/lib/jqPlot/plugins/jqplot.highlighter.min.js"></script>
<script language="javascript" type="text/javascript"
        src="${BASE_PATH}/resources/js/lib/jqPlot/plugins/jqplot.pointLabels.min.js"></script>
<script type="text/javascript"
        src="${BASE_PATH}/resources/js/lib/jqPlot/plugins/jqplot.donutRenderer.min.js"></script>
<script type="text/javascript"
        src="${BASE_PATH}/resources/js/lib/jqPlot/plugins/jqplot.bubbleRenderer.min.js"></script>
<#--<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/statistics.css">-->

</head>
<div class="wrapper">
<#include "statistics/statistics_nav.ftl">

    <br/><br/>

    <div class="block">
        <div id="points-chart">
        </div>
    </div>
    <br/><br/>
</div>
<script type="text/javascript">

    $(document).ready(function () {

        $.ajax({
            type: 'get',
            dataType: 'json',
            url: BASE_PATH + '/shop/stats/daily',
            success: function (result) {
                if (result.success != true) {
                    return false;
                } else {
                    var data = result.data;
                    drawPointsChart('points-chart', data);
                }
            },
            error: function (a, b, c) {

            }
        });
        /* $.ajax({
             type: 'get',
             dataType: 'json',
             url: BASE_PATH + '/shop/stats/staff_tri_month',
             success: function (result) {
                 if (result.success != true) {
                     return false;
                 } else {
                     var data = result.data;
                     drawBarchart('bar-chart', data);
                 }
             },
             error: function (a, b, c) {

             }
         });*/
    });

    function drawPointsChart(containerElement, data) {

        var line2 = [];
        for (var i = 0; i < data.length; i++) {
            line2.push([data[i]['date'], data[i]['price']]);
        }
        //    var line2 = [['1/1/2008', 42], ['2/14/2008', 56], ['3/7/2008', 39], ['4/22/2008', 81]];

        var plot2 = $.jqplot(containerElement, [line2], {
            title: '门店业绩',
            axes: {
                xaxis: {
                    renderer: $.jqplot.DateAxisRenderer,
                    labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
                    tickRenderer: $.jqplot.CanvasAxisTickRenderer,
                    tickInterval:'1 days',
                    tickOptions: {
                        formatString:'%#m-%#d',
                        //  labelPosition: 'middle',
                        angle: -30
                    },
                    pad:1,
                },
                yaxis: {
                    labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
                    min:0,
                    tickOptions: { formatString: '%d' },
                    rendererOptions: {
                        forceTickAt0: true
                    }
                }
            }, highlighter: {
                show: true,
                showLabel: true,
                tooltipAxes: 'y',
                sizeAdjust: 7.5 , tooltipLocation : 'ne'
            }
        });


    }
</script>
<#include "layout/footer.ftl" >
