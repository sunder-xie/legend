/**
 * Created by huage on 2017/3/29.
 */
$(function () {
    seajs.use(['date', 'dialog', 'art','table'],function (dp, dg, at,tb) {
        //进页面加载
        // 开始结束日期
        dp.dpStartEnd();
        //初始化
        Smart.init_art(at);
        getList();
        //点击事件
        $(document).on('click','.search-btn',function () {
            var gmtAuditStart = $('#startDate').val(),
                gmtAuditEnd = $('#endDate').val(),
                vehicleSn = $('.car-input').val(),
                auditStatus = $('.state-choose').data('code');
                if((gmtAuditStart&&!gmtAuditEnd) || (!gmtAuditStart&&gmtAuditEnd)){
                    dg.warn('请选择完整录入时间区间');
                    return false;
                }
                if(gmtAuditStart) {
                    gmtAuditStart = Date.parse(new Date(gmtAuditStart));
                }
                if(gmtAuditEnd) {
                    gmtAuditEnd = Date.parse(new Date(gmtAuditEnd));
                }

                var data = {
                    vehicleSn:vehicleSn,
                    gmtAuditStart:gmtAuditStart,
                    gmtAuditEnd:gmtAuditEnd,
                    auditStatus:auditStatus
                };
            getList(data);
        }).on('click','.reject',function () {
            var $this = $(this),
                siblings = $this.siblings('.rejectReason');
                dg.open({
                    content:siblings
                })
        }).on('click','.state-choose',function () {
            var $this =$(this),
                $children = $this.siblings('.state-choose-btn');
            if($children.hasClass('s_up')){
                $this.siblings('ul').removeClass('dis-none').end().siblings('i').addClass('s_down').removeClass('s_up');
                return false;
            }
            $this.siblings('ul').addClass('dis-none').end().siblings('i').removeClass('s_down').addClass('s_up');
        }).on('click','.state-div ul li',function () {
            var $this = $(this),
                this_text = $this.text(),
                $parent = $this.parent('.select-ul'),
                code = $this.data('code');
                $parent.addClass('dis-none').siblings('input.state-choose').val(this_text).data('code',code)
                .siblings('i').removeClass('s_down').addClass('s_up');
        }).on('click','.entering-btn',function () {
            window.location.href = BASE_PATH+'/insurance/offline/entering'
        });

       function getList(data) {
           var url = BASE_PATH+'/insurance/offline/list/show';
           var Data ={};
           if(data){
               Data = data;
           }
           tb.init({
               //表格数据url，必需
               url: url,
               //表格数据目标填充id，必需
               fillid: 'tableTest',
               //分页容器id，必需
               pageid: 'pagingTest',
               //表格模板id，必需
               tplid: 'tableTestTpl',
               //扩展参数,可选
               data: Data,
               //关联查询表单id，可选
               formid: null,
               // 是否开启hash功能
               enabledHash: false,
               //渲染表格数据完后的回调方法,可选
               callback : null
           });
        }

    })
});
