$(function() {
    seajs.use([
        "ajax", 
        "dialog", 
        'artTemplate',
        'paging'],function(ajax, dg, at, page){

        var dateFormat = function(date, format){
            date = new Date(date);
            var map = {
                "M": date.getMonth() + 1, //月份
                "d": date.getDate(), //日
                "h": date.getHours(), //小时
                "m": date.getMinutes(), //分
                "s": date.getSeconds(), //秒
                "q": Math.floor((date.getMonth() + 3) / 3), //季度
                "S": date.getMilliseconds() //毫秒
            };

            format = format.replace(/([yMdhmsqS])+/g, function(all, t){
                var v = map[t];
                if (v !== undefined) {
                    if (all.length > 1) {
                        v = '0' + v;
                        v = v.substr(v.length - 2);
                    }
                    return v;
                }
                else if (t === 'y') {
                    return (date.getFullYear() + '').substr(4 - all.length);
                }
                return all;
            });
            return format;
        }

        at.helper('$dateFormat',function (date, format){return dateFormat(date, format);});
        
        //请求采购金明细数据
        var requestData = function(params,p){
            p = p || 1;
            ajax.get({
                url : BASE_PATH + '/shop/yunxiu/purchase/list_flow',
                data : $.extend({page: p},params)
            }).done(function(json){
                if(json.success && json.list){
                    var htmlText = at('flow-tpl',{data:json.list});
                    $(".flow-data").html(htmlText);
                    if(!$.isEmptyObject(params)){
                        //开始时间
                        $('#d4311').val(params.startTime);
                        $('#d4312').val(params.endTime);
                    }
                    
            var endDate = $.trim($('#d4312').val());
                    page.init({
                        itemSize : json.total,
                        pageCount : Math.ceil(json.total/10),
                        current : p,
                        backFn : function(page){
                            requestData(params,page);
                        }
                    });
                }else{
                    dg.info('没有符合条件的结果',3);
                }
            });
        }
        //初始化明细数据。
        requestData({},1);

        //根据时间筛选明细数据 今天、一个月、半年、一年
        var condList = $('.condition','.search_box');
        var getParams = function(index){
            var format = 'yyyy-MM-dd';
            var params = {};
            var startDate = null;
            switch(index){
                case 0:
                    //今天 
                    startDate = new Date();
                    break;
                case 1:
                    //一个月
                    startDate =  (new Date()).getTime()-(30*24*60*60*1000);
                    break;
                case 2:
                    //半年
                    startDate =  (new Date()).getTime()-(6*30*24*60*60*1000);
                    break;
                case 3:
                    //一年
                    startDate =  (new Date()).getTime()-(365*24*60*60*1000);
                    break;
            }
            params.startTime = dateFormat(startDate, format);
            params.endTime = dateFormat(new Date(), format);
            return params;
        }
        condList.on('click',function(){
            var index = condList.index($(this));
            var params = getParams(index);
            requestData(params);
        });

        //根据时间段筛选明细数据
        $('.search_btn').click(function(){
            var startDate = $.trim($('#d4311').val());
            var endDate = $.trim($('#d4312').val());

            if(startDate == ""){
                dg.info('请选择开始时间',3);
                return;
            }

            if(endDate == ""){
                dg.info('请选择结束时间',3);
                return;
            }
            requestData({
                startTime : startDate,
                endTime : endDate
            });
        });
    });
});