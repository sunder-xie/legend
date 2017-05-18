/**
 * Created by huage on 2016/12/29.
 */

$(function () {
    var second_nav_map = {
            // 送保险活动
            "insurance/anxin/virtual/flow/activity-description": [
                "insurance/anxin/virtual/flow/activity-description"
            ],
            // 查询保单
            "insurance/anxin/view/insure-list": [
                "insurance/anxin/view/insure-list",
                "insurance/anxin/view/insurance-detail",
                "insurance/anxin/view/virtual-detail"
            ],
            // 录入保单
            "insurance/offline/list": [
                "insurance/offline/entering",
                "insurance/offline/list"
            ],
            // 创建保单
            "insurance/anxin/flow/insurance-flow": {
                "insurance/anxin/flow/insurance-flow": [
                    "insurance/anxin/flow/insurance-flow",
                    "insurance/anxin/flow/confirm-info",
                    "insurance/anxin/flow/back-update",
                    "insurance/anxin/flow/insurance-result",
                    "insurance/anxin/virtual/flow/virtual-plan",
                    "insurance/anxin/virtual/flow/virtual-service",
                    "insurance/anxin/virtual/flow/virtual-modify",
                    "insurance/anxin/virtual/flow/virtual-verify/unionpay"
                ], "smart/bihu/flow/bihu-flow": [
                    //智能投保
                    "smart/bihu/flow/bihu-flow",
                    // 充值页面
                    "smart/bihu/recharge/index",
                    // 使用记录-充值+消费
                    "smart/bihu/usedView/recharge-list",
                    //智能投保成功
                    'smart/bihu/flow/bihu-flwo-two',
                    //智能投保确认页面
                    'smart/bihu/flow/confirm-info',
                    'smart/bihu/flow/back-update',
                    'smart/bihu/flow/insurance-result',
                    'smart/bihu/recharge/judge-go',
                    'smart/bihu/flow/virtual-modify',
                    'smart/bihu/flow/virtual-plan',
                    'smart/bihu/recharge/bihu-verify/alipay'
                ]
            },
            //现金券
            'insurance/anxin/cashCoupon/cashIntroductionHome':{
                'insurance/anxin/cashCoupon/cashIntroductionHome':[
                    'insurance/anxin/cashCoupon/cashIntroductionHome',
                    'insurance/anxin/cashCoupon/cashCouponIntroduction'
                ],
                'insurance/anxin/cashCoupon/cashCouponList':[
                    'insurance/anxin/cashCoupon/cashRebateCount',
                    'insurance/anxin/cashCoupon/cashCouponList'
                    ]
            },
            // 服务券
            "insurance/anxin/settle/service-list": [
                "insurance/anxin/settle/service-list",
                "insurance/anxin/settle/consume-service"
            ],
            // 服务包培训资料
            "insurance/anxin/train/training-materials": [
                "insurance/anxin/train/training-materials"
            ],
            //淘汽对账--活动补贴
            "insurance/anxin/coupon/coupon-settle": [
                "insurance/anxin/coupon/coupon-settle"
            ],
            // 淘汽对账-服务包
            "insurance/anxin/balance": [
                "insurance/anxin/balance"
            ],

            // 淘汽对账--淘汽对账-奖励金
            "insurance/anxin/view/bounty": [
                "insurance/anxin/view/bounty"
            ],
            // 淘汽对账
            "insurance/settle/view/index": [
                "insurance/settle/view/index",
                "/legend/insurance/settle/view/cash",
                "/legend/insurance/settle/view/package",
                "/legend/insurance/settle/view/bonus",
                "/legend/insurance/settle/view/rule"
            ],
            // 门店收款账户
            "insurance/anxin/settle/bank-card": [
                "insurance/anxin/settle/bank-card",
                "insurance/anxin/settle/bank-card-detail",
                "insurance/anxin/settle/bank-card-routing"
            ]
        },
        href = location.href, choose_second_url, choose_third_url, item, pattern, item_k, index_third,index_second,f=0;
    //外层循环标签
    loop:
        for (var k in second_nav_map) {
            choose_second_url = k;
            choose_third_url = '';
            item = second_nav_map[k];
            if (item instanceof Array) {         //若为数组，则没三级菜单
                for (var j = 0; j < item.length; j++) {
                    pattern = new RegExp(item[j]);
                    if (pattern.test(href)) {
                        check_i();
                        break loop;
                    }
                }
            } else {
                // 否则是有的
                f+=1;
                for (var it in item) {
                    choose_third_url = it;
                    item_k = item[it];
                    for (var h = 0; h < item_k.length; h++) {
                        pattern = new RegExp(item_k[h]);
                        if (pattern.test(href)) {
                            check_i();
                            break loop;
                        }
                    }

                }
            }
        }

    //  左侧菜单栏
    function check_i() {
        // 判断是否是 送保险服务
        var modeV = util.getPara("mode");                   //拿到mode的值
        if (modeV == "3") {
            // 设置左边框为 送保险的选中框
            $(".left_nav_send_insurance").addClass("current");
        }else{
            $('.Z-nav-item dd a').each(function () {
                var $this = $(this),
                    $href = $this.prop('href').indexOf(choose_second_url);
                if ($href != -1) {
                    index_second = $this.parent().addClass('current').next('div').addClass('bc_gray');
                    return false;
                }
            });
            if(choose_third_url){
                index_third = $('[href*="' + it + '"]').index() || 0;
                $('.creat-Insurance').eq(f-1).find('a').eq(index_third).addClass('arrow');
            }
        }
    }

    // 带跳转链接
    var Model = {}, dgLoginId;

    seajs.use('ajax');
    Model.checkPassword = function (pwd) {
        var url = BASE_PATH + '/shop/manager/check-password';
        return $.post(url, {password: pwd});
    };

    Model.judgeProvince = function () {
       var url = BASE_PATH + '/insurance/anxin/cashCoupon/judgeProvince';
        return $.post(url);
    };

    (function initialize() {
        Model.judgeProvince()
            .done(function (result) {
                if (result.success && result.data) {
                    $('#province_nav').show();
                }
            });
    })();

    $(document)
        .on('click', '.dialog-toggle', function () {
            var that = this;
            seajs.use('dialog', function (dg) {
                dgLoginId = dg.open({
                    area: ["380px", "auto"],
                    end: function () {
                        $(".ks-dialog-login-pwd").attr('type', 'text');
                    },
                    content: $('#loginTpl').html()
                });
                $('.ks-dialog-login-btn').data('url', $(that).attr('href'));
                $('.ks-dialog-login-pwd').focus();
            });
            return false;
        })
        .on('input', '.ks-dialog-login-pwd', function () {
            $(this).attr('type', 'password');
        })
        .on('click', '.ks-dialog-login-btn', function () {
            var pwd = $('.ks-dialog-login-pwd').val();

            if (pwd == null || pwd === "") {
                $('.ks-dialog-login-err').text('密码不能为空');
                return;
            }

            Model.checkPassword(pwd)
                .done(function (result) {
                    if (result.success && result.data) {
                        /**
                         * 服务包培训资料页面登录弹窗处理
                         * create by sky 2016 11 1
                         */
                        sessionStorage.setItem('trainingMaterials', 1);
                        location.href = $('.ks-dialog-login-btn').data('url');
                    } else {
                        $('.ks-dialog-login-err').text(result.errorMsg);
                    }
                });
        })
        .on("click", ".clearSession", function () {               //点击创建保单,清除session里面的id
            sessionStorage.clear();
        });
    //子菜单
    $('.creat-billing').on('mouseenter', function () {
        var $this = $(this),
            $this_parent = $this.parent();
        $this.addClass('arrow_billings');
        $this_parent.css('background-color', '#f0f0f0').prev('dd').addClass('second-current');
    }).on('mouseout', function () {
        var $this = $(this),
            $this_parent = $this.parent();
        $this.removeClass('arrow_billings');
        $this_parent.css('background-color', '').prev('dd').removeClass('second-current');
    });
});
