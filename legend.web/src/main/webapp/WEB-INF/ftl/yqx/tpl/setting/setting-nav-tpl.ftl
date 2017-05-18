<#--
    设置页面 通用左侧菜单
    lwj 2016-11-03

    用到的页面：设置的 all
 -->
<style>
    .aside-nav{
        border: 1px solid #cacaca;
        padding: 0;
        border-top: 0;
    }
    .aside-nav-list{
        display: none;
    }
    .aside-nav-root{
        border-top:1px solid #cacaca;
        overflow: hidden;
    }
    .aside-nav-root img{
        float: right;
        padding: 14px 10px;
    }
    .aside-nav li.aside-nav-list{
        border:none;
    }
     .link-download {
         width: 140px;
         margin-top: 10px;
         padding: 10px;
         font-size: 12px;
         color: #333;
         background: url("${BASE_PATH}/static/img/common/order/download_03.png") 98px 13px no-repeat #fff;
         border: 1px solid #ddd;
     }
    .material{border-bottom: 1px solid #ddd;}
    .material .material-title{font-weight: bold; color: #333; line-height: 20px;}
    .material .material-english{font-family: arial, verdana, sans-serif; color: #666; line-height: 20px;}
    .material-list{ margin-top: 10px;}
    .material-list a{display: block; color: #333; line-height: 25px; background: url("${BASE_PATH}/static/img/common/order/download-ico1.png") no-repeat right;}
    .material-list a:hover{cursor:pointer;color: #85af1d;background: url("${BASE_PATH}/static/img/common/order/download-ico2.png") no-repeat right; }

</style>
<ul class="aside-nav" data-tpl-ref="settlement-nav-tpl">
    <#if BPSHARE =='true' && SESSION_USER_IS_ADMIN == 1>
    <li class="aside-nav-root">
        钣喷设置
        <img src="${BASE_PATH}/static/img/page/setting/menu-off.png"/>
    </li>
    <li class="aside-nav-list">
        <dl>
            <dd><a href="${BASE_PATH}/workshop/team/listpage">班组管理</a></dd>
            <dd><a href="${BASE_PATH}/workshop/productionline/productionline-list">生产线管理</a></dd>
            <dd><a href="${BASE_PATH}/share/partner/index">股东管理</a></dd>
            <dd><a href="${BASE_PATH}/share/channel/index">渠道管理</a></dd>
            <dd><a href="${BASE_PATH}/workshop/paintspecies/paintspecies-list">面漆种类设置</a></dd>
            <dd><a href="${BASE_PATH}/workshop/paintlevel/paintlevel-list">面漆级别设置</a></dd>
        </dl>
    </li>
    </#if>
    <li class="aside-nav-root">
        资料管理
        <img src="${BASE_PATH}/static/img/page/setting/menu-off.png"/>
    </li>
    <li class="aside-nav-list">
        <dl>
            <dd><a href="${BASE_PATH}/shop/setting/serviceInfo/serviceInfo-list">服务资料</a></dd>
            <dd><a href="${BASE_PATH}/shop/goods/goods-list">配件资料</a></dd>
            <dd><a href="${BASE_PATH}/shop/setting/supplier/supplier-list">供应商资料</a></dd>
        </dl>
    </li>
    <li class="aside-nav-root">
        账号管理
        <img src="${BASE_PATH}/static/img/page/setting/menu-off.png"/>
    </li>
    <li class="aside-nav-list">
        <dl>
            <#if SESSION_USER_IS_ADMIN == 1>
            <dd><a href="${BASE_PATH}/shop/setting/edit-detail">门店信息</a></dd>
            </#if>
            <dd><a href="${BASE_PATH}/shop/setting/user-info/user-info">个人信息</a></dd>
        </dl>
    </li>
    <#if SESSION_USER_IS_ADMIN == 1>
    <li class="aside-nav-root">
        人员管理
        <img src="${BASE_PATH}/static/img/page/setting/menu-off.png"/>
    </li>
    <li class="aside-nav-list">
        <dl>
            <dd><a href="${BASE_PATH}/shop/setting/roles/roles-list">岗位管理</a></dd>
            <dd><a href="${BASE_PATH}/shop/setting/user-info/user-list">员工账号</a></dd>
        </dl>
    </li>
    <li class="aside-nav-root">
        安全管理
        <img src="${BASE_PATH}/static/img/page/setting/menu-off.png"/>
    </li>
    <li class="aside-nav-list">
        <dl>
            <dd><a href="${BASE_PATH}/shop/security-login/level-setting">安全登录设置</a></dd>
            <dd><a href="${BASE_PATH}/shop/conf/securityexport/toset">导出加密设置</a></dd>
        </dl>
    </li>
    </#if>
    <li class="aside-nav-root">
        功能配置
        <img src="${BASE_PATH}/static/img/page/setting/menu-off.png"/>
    </li>
    <li class="aside-nav-list">
        <dl>
            <dd><a href="${BASE_PATH}/shop/print-config?refer=setting">打印设置</a></dd>
        <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'><!-- 档口版没有业务类型 -->
            <dd><a href="${BASE_PATH}/shop/setting/order-type">业务类型</a></dd>
        </#if>
            <dd><a href="${BASE_PATH}/shop/setting/debit-type">收款类型</a></dd>
        <#if BPSHARE =='true' && SESSION_USER_IS_ADMIN == 1>
            <dd><a href="${BASE_PATH}/shop/setting/pay-type/bp">付款类型</a></dd>
        <#else>
            <dd><a href="${BASE_PATH}/shop/setting/pay-type">付款类型</a></dd>
        </#if>
            <dd><a href="${BASE_PATH}/shop/setting/payment">结算方式</a></dd>
        <!-- 档口版没有提醒设置 -->
        <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
            <dd><a href="${BASE_PATH}/shop/conf/notice">提醒设置</a></dd>
        </#if>
            <dd><a href="${BASE_PATH}/shop/conf/normal-service">标准化服务</a></dd>
            <dd><a href="${BASE_PATH}/shop/conf/msg-push">消息推送配置</a></dd>
            <dd><a href="${BASE_PATH}/shop/conf/work-check-on">上下班时间设置</a></dd>
        <#if SESSION_SHOP_LEVEL?number != 10>
            <dd><a href="${BASE_PATH}/shop/conf/wechat-evaluation">微信评论</a></dd>
        </#if>
        <#if SESSION_USER_IS_ADMIN == 1>
            <dd><a href="${BASE_PATH}/shop/conf/payment-mode">支付方式</a></dd>
        </#if>
        </dl>
    </li>
    <li class="aside-nav-root">
        数据导入
        <img src="${BASE_PATH}/static/img/page/setting/menu-off.png"/>
    </li>
    <li class="aside-nav-list">
        <dl>
            <dd><a href="${BASE_PATH}/init/import/serviceInfo">服务导入</a></dd>
            <dd><a href="${BASE_PATH}/init/import/goods">配件导入</a></dd>
            <dd><a href="${BASE_PATH}/init/import/supplier">供应商导入</a></dd>
            <dd><a href="${BASE_PATH}/init/import/customerCar">客户车辆导入</a></dd>
            <dd><a href="${BASE_PATH}/init/import/orderHistory">维修历史导入</a></dd>
            <dd><a href="${BASE_PATH}/init/import/memberCard">会员导入</a></dd>
            <dd><a href="${BASE_PATH}/init/import/accountCoupon">优惠劵导入</a></dd>
            <dd><a href="${BASE_PATH}/init/import/accountCombo">计次卡导入</a></dd>
        </dl>
    </li>

</ul>
<div role="link" class="link-download">
    <div class="material">
        <p class="material-title">培训资料下载</p>
        <p class="material-english">Download</p>
    </div>
    <ul class="material-list">
        <li class="js-marketing"><a href="http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/image/orig_148896097078233517.ppt">系统设置资料</a></li>
    </ul>
</div>
<script>
    //菜单回显逻辑
    function getAsideNav() {
        return [
            <#if BPSHARE =='true' && SESSION_USER_IS_ADMIN == 1>
            //钣喷设置
            {
                "url":[
                    {"/workshop/team/listpage":[
                        "/workshop/team/editpage",
                        "/workshop/team/addpage"
                    ]},
                    {"/workshop/productionline/productionline-list":[
                            "/workshop/productionline/productionline-edit",
                            "/workshop/productionline/productionline-add"
                    ]},
                    {"/share/partner/index":[
                            "/share/partner/add"
                    ]},
                    {"/share/channel/index":[]},
                    {"/workshop/paintspecies/paintspecies-list":[]},
                    {"/workshop/paintlevel/paintlevel-list":[]}
                ]
            },
            </#if>
            //资料管理
            {
                "url":[
                    {"/shop/setting/serviceInfo/serviceInfo-list":[
                            "/shop/setting/serviceInfo/serviceInfo-edit",
                            "/shop/setting/serviceInfo/serviceInfo-fee-edit",
                            "/shop/setting/serviceInfo/serviceCate-list"
                    ]},
                    {"/shop/goods/goods-list":[
                            "/shop/goods/goods-tqmall-list",
                            "/shop/goods/goods-toadd",
                            "/shop/goods/goods-toedit",
                            "shop/goods/toPaintGoodsAdd",
                            "shop/goods/editBPGoodsPage",
                            "shop/goods_category/goods-category-list",
                            "shop/goods_brand/goods-brand-list",
                            "shop/goods_unit/goods-unit-list",
                    ]},
                    {"/shop/setting/supplier/supplier-list":[
                        "/shop/setting/supplier/supplier-edit"
                    ]}
                ]
            },
            //账号管理
            {
                "url":[
                <#if SESSION_USER_IS_ADMIN == 1>
                    {"/shop/setting/edit-detail":[
                            "/shop/setting/finance/finance-detail-shop"
                    ]},
                </#if>
                    {"/shop/setting/user-info/user-info":[
                        "/shop/setting/finance/finance-detail-user",
                        "/shop/setting/technician/technician-detail"
                    ]}
                ]
            },
            <#if SESSION_USER_IS_ADMIN == 1>
            //人员管理
            {
                "url":[
                    {"/shop/setting/roles/roles-list":[]},
                    {"/shop/setting/user-info/user-list":[
                            "/shop/setting/user-info/user-edit",
                            "/shop/setting/user-info/user-add"
                    ]}
                ]
            },
            //安全管理
            {
                "url":[
                    {"/shop/security-login/level-setting":[
                            "/shop/security-login/device-list",
                            "/shop/security-login/network-setting"
                    ]},
                    {"/shop/conf/securityexport/toset":[
                            "shop/conf/securityexport/toreset"
                    ]}
                ]
            },
            </#if>
            //功能配置
            {
                "url":[
                    {"/shop/print-config":[]},
                <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                    {"/shop/setting/order-type":[]},
                </#if>
                    {"/setting/debit-type":[]},
                    {"/setting/pay-type":[]},
                    {"/shop/setting/payment":[]},
                <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                    {"/shop/conf/notice":[]},
                </#if>
                    {"/shop/conf/normal-service":[]},
                    {"/shop/conf/msg-push":[]},
                    {"/shop/conf/work-check-on":[]}
                <#if SESSION_SHOP_LEVEL?number != 10>
                    ,{"/shop/conf/wechat-evaluation":[]}
                </#if>
                <#if SESSION_USER_IS_ADMIN == 1>
                    ,{"/shop/conf/payment-mode":[]}
                </#if>
                ]
            },
            //数据导入
            {
                "url":[
                    {"/init/import/serviceInfo":[]},
                    {"/init/import/goods":[]},
                    {"/init/import/supplier":[]},
                    {"/init/import/customerCar":[]},
                    {"/init/import/orderHistory":[]},
                    {"/init/import/memberCard":[]},
                    {"/init/import/accountCoupon":[]},
                    {"/init/import/accountCombo":[]}
                ]
            }
        ];
    }

    //左侧菜单展开收缩效果
    $(document).on('click','.aside-nav-root',function(){
        var imgSrcOn = '${BASE_PATH}/static/img/page/setting/menu-on.png',
                imgSrcOff = '${BASE_PATH}/static/img/page/setting/menu-off.png';
        var extend = $(this).data('extend');
        var that = this;
        if(!extend || extend == 'false') {
            $(this).find('img').attr('src', imgSrcOn).siblings('img').attr('src', imgSrcOff);
            $(this).next('.aside-nav-list').show(500)
                    .siblings('.aside-nav-list').hide(500);
            $(this).data('extend', true);
            // 收起其他的tab
            $('.aside-nav-root').each(function () {
                if(this !== that && $(this).data('extend') == true) {
                    $(this).trigger('click');
                }
            });
        }else{
            $(this).find('img').attr('src', imgSrcOff).siblings('img').attr('src', imgSrcOn);
            $(this).next('.aside-nav-list').hide(500);
            $(this).data('extend', false);
        }
    });

</script>
<#include "yqx/tpl/common/aside-nav-tpl.ftl">
<script>
    //进入页面时相应菜单展开（应用于设置模块左侧菜单）
    $(function(){
        var current = $('.aside-nav-list').find('a');
        current.each(function(){
            var imgSrcOn = '${BASE_PATH}/static/img/page/setting/menu-on.png',
                    imgSrcOff = '${BASE_PATH}/static/img/page/setting/menu-off.png';
            if($(this).hasClass('current')){
                var asideNavRoot = $(this).parents('.aside-nav-list').prev('.aside-nav-root'),
                        asideNavList = $(this).parents('.aside-nav-list');
                asideNavRoot.find('img').attr('src', imgSrcOn).siblings('img').attr('src', imgSrcOff);
                asideNavList.show();
                asideNavRoot.data('extend', true);
            }
        })
    })
</script>