$(document).ready(function ($) {

	var doc = $(document);
	var tableName = "order_search_table";
	doc.off("click", ".pick_btn").on("click", ".pick_btn", function () {
		var auditPassStartTime = $(".auditPassStartTime").val();
		var auditPassEndTime = $(".auditPassEndTime").val();
		loadData(auditPassStartTime, auditPassEndTime);

	});


	seajs.use(["table", "ajax", "dialog"], function (table, ajax, dg) {
		ajax.post({
			url: BASE_PATH + '/shop/settlement/activity/get_time',
			success: function (result) {
				if (result.success != true) {
					dg.info(result.errorMsg);
					return false;
				} else {
					var data = result.data;
					var auditPassStartTime = data.auditPassStartTime;
					var auditPassEndTime = data.auditPassEndTime;
					$(".auditPassStartTime").val(auditPassStartTime);
					$(".auditPassEndTime").val(auditPassEndTime);
					loadData(auditPassStartTime, auditPassEndTime);
				}
			}
		});
	});

	$(document).on("click", ".go_detail", function () {
		var actTplId = $.trim($(this).attr("act-tpl-id"));
		var auditPassStartTime = $.trim($(".auditPassStartTime").val());
		var auditPassEndTime = $.trim($(".auditPassEndTime").val());
		var url = BASE_PATH + "/shop/settlement/activity/detail?actTplId=" + actTplId
			+ "&auditPassStartTime=" + auditPassStartTime
			+ "&auditPassEndTime=" + auditPassEndTime;
		window.location.href = url;
	});
});

function loadData(auditPassStartTime, auditPassEndTime) {
	seajs.use(["table", "ajax", "dialog", "artTemplate"], function (table, ajax, dg, art) {
		ajax.get({
			data: {
				search_auditPassStartTime: auditPassStartTime,
				search_auditPassEndTime: auditPassEndTime
			},
			url: BASE_PATH + '/shop/settlement/activity/month_settle_list',
			success: function (result) {
				if (result.success != true) {
					dg.info("此时间范围内没有找到可统计的服务单，或您还未参加我们的活动");
					$(".table_content").html('');
					$("#act_charts").html('');
					return false;
				}
				var data = result["data"];

				var html = art.render('contentTemplate', {'templateData': data});
				$(".table_content").html(html);

				var names = [];
				var chartData = [];
				for (var i = 0; i < data.length - 1; i++) {
					names.push(data[i]['actName']);
					var tempData = {value: data[i]['totalSettleAmount'], name: data[i]['actName']};
					chartData.push(tempData);
				}

				// 基于准备好的dom，初始化echarts图表
				var myChart = echarts.init(document.getElementById('act_charts'));
				var barWidth;
				if(names.length ==1){
					barWidth=50;
				}

				option = {
					title: {
						text: ''
					},
					tooltip: {
						trigger: 'axis'
					},
					legend: {
						data: ['']
					},
					toolbox: {
						show: true,
						feature: {
							mark: {show: false},
							dataView: {show: false, readOnly: false},
							magicType: {show: false, type: ['line', 'bar']},
							restore: {show: false},
							saveAsImage: {show: false}
						}
					},
					calculable: true,
					xAxis: [
						{
							type: 'category',
							data: names,
							axisLabel: {
								rotate: 40
							}
						}
					],
					yAxis: [
						{
							type: 'value'
						}
					],
					series: [
						{
							name: '活动收入',
							type: 'bar',
							data: chartData,
							barWidth:barWidth
						}
					]
				};

				// 为echarts对象加载数据
				myChart.setOption(option);
			}
		});
	});
}