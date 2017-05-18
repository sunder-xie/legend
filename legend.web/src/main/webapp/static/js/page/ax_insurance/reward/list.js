/**
 * Created by ZhuangQianQian on 16/8/2.
 */
$(function(){
    var $doc = $(document);
    seajs.use([
        'table',
        'art'
    ], function (tb, at){
        at.helper("$dateFormat", function dateFormat(date, format) {
            date = new Date(date);
            var map = {
                M: date.getMonth() + 1,
                //月份
                d: date.getDate(),
                //日
                h: date.getHours(),
                //小时
                m: date.getMinutes(),
                //分
                s: date.getSeconds(),
                //秒
                q: Math.floor((date.getMonth() + 3) / 3),
                //季度
                S: date.getMilliseconds()
            };
            format = format.replace(/([yMdhmsqS])+/g, function(all, t) {
                var v = map[t];
                if (v !== undefined) {
                    if (all.length > 1) {
                        v = "0" + v;
                        v = v.substr(v.length - 2);
                    }
                    return v;
                } else if (t === "y") {
                    return (date.getFullYear() + "").substr(4 - all.length);
                }
                return all;
            });
            return format;
        });
        function tabSelect(obj){
            var type = '',url = '';
            $(".Z-tab-item").removeClass("on");
            $(obj=obj?obj:".first").addClass("on");
            if($(obj).hasClass("first")){
                type = 0;
                url = BASE_PATH + '/insurance/anxin/view/getWaitEffectReward';
            }else if($(obj).hasClass("second")){
                type = 1;
                url = BASE_PATH + '/insurance/anxin/view/getFinanceReward?payType='+type;
            }else{
                type = 2;
                url = BASE_PATH + '/insurance/anxin/view/getFinanceReward?payType='+type;
            }
            $.ajax({
                url : BASE_PATH+'/insurance/anxin/view/getReward',
                success:function(json){
                    if(json.success){
                        $(".un-effect em").text(json.data.noEffectiveNum);
                        $(".effected em").text(json.data.surplusRewardNum);
                    }else{
                        dg.fail(json.message);
                    }
                }
            });
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'tableTest',
                //分页容器id，必需
                pageid: 'pagingTest',
                //表格模板id，必需
                tplid: 'tableTestTpl',
                //如果模板需要自定义参数,可选
                tpldata: {'type':type},
                //扩展参数,可选
                data: {},
                //关联查询表单id，可选
                formid: null,
                //渲染表格数据完后的回调方法,可选
                callback : null
            });
        }
        tabSelect();
        $doc.on("click",".Z-tab-item",function(){
            tabSelect($(this));
        });
    });
});