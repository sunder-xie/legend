<style>
    .aside-nav-list {
        display: none;
    }
    .aside-nav dd a{
        font-family: Simsun,Tahoma,Arial,Helvetica Neue,Hiragino Sans GB,sans-serif;
    }
    .link-download-box {
        box-sizing: border-box;
        width: 140px;
        margin-top: 10px;
        padding: 10px;
        font-size: 12px;
        color: #333;
        background: url("${BASE_PATH}/static/img/common/order/download_03.png") 98px 13px no-repeat #fff;
        border: 1px solid #ddd;
    }

    .material {
        border-bottom: 1px solid #ddd;
    }

    .material .material-title {
        font-weight: bold;
        color: #333;
        line-height: 20px;
    }

    .material .material-english {
        font-family: arial, verdana, sans-serif;
        color: #666;
        line-height: 20px;
    }

    .material-list {
        margin-top: 10px;
    }

    .material-list a {
        display: block;
        color: #333;
        line-height: 25px;
        background: url("${BASE_PATH}/static/img/common/order/download-ico1.png") no-repeat right;
    }

    .material-list a:hover {
        cursor: pointer;
        color: #85af1d;
        background: url("${BASE_PATH}/static/img/common/order/download-ico2.png") no-repeat right;
    }
</style>

<aside class="aside">
    <nav class="aside-nav">
        <ul>
            <#if SESSION_SHOP_IS_TQMALL_VERSION == 'true'>
                <#if moduleUrl == "customer">
                    <#--客户管理-->
                    <#include "yqx/page/marketing/left-nav-account.ftl"/>
                </#if>
                <#if moduleUrl=='wechat'>
                    <#--微信公众号-->
                    <#include "yqx/page/marketing/left-nav-wechat.ftl"/>
                </#if>
            <#else>
                <#if YBD?? && YBD=="true">
                    <#include "yqx/page/marketing/left-nav-gather.ftl"/>
                </#if>
                <#--客户营销-->
                <#include "yqx/page/marketing/left-nav-marketing.ftl"/>

                <#--客户管理-->
                <#include "yqx/page/marketing/left-nav-account.ftl"/>
                <#--微信公众号 基础版没有微信公众号-->
                <#if SESSION_SHOP_LEVEL != 10>
                    <#include "yqx/page/marketing/left-nav-wechat.ftl"/>
                </#if>
            </#if>

        </ul>
    </nav>
    <div role="link" class="link-download-box">
        <div class="material">
            <p class="material-title">培训资料下载</p>
            <p class="material-english">Download</p>
        </div>
        <ul class="material-list">

            <#if SESSION_SHOP_IS_TQMALL_VERSION == 'true'>
                <li class="js-marketing"><a href="http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/legend/ppt/accountMarketing.ppt">客户营销资料</a></li>
                <li class="js-management"><a href="http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/images/201608/source_img/original_p_147159576260223378.ppt">客户管理资料</a></li>
            <#else>
                <#if YBD?? && YBD=="true">
                    <li class="js-gather"><a href="http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/image/orig_148353506363573974.ppt">集客方案资料</a></li>
                </#if>
                <li class="js-marketing"><a href="http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/legend/ppt/accountMarketing.ppt">客户营销资料</a></li>
                <li class="js-management"><a href="http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/legend/ppt/accountManagement.ppt">客户管理资料</a></li>
                <#if SESSION_SHOP_LEVEL != 10>
                <li class="js-wechat"><a href="http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/legend/ppt/wechat.ppt">微信公众号资料</a></li>
                </#if>
            </#if>
        </ul>
    </div>
</aside>

<script data-ref-tpl="marketing/left-nav">
    jQuery(function () {
        /*^ 微信公众号判断 */
        var url = BASE_PATH + '/shop/wechat/op/qry-wechat-info';
        $.ajax({
            url: url,
            success: function (json) {
                var data = json.data;
                if (!json || !json.success || !data || data.shopStatus != 3) {
                    $('.js-nav-wx-list').remove();
                    $('.js-nav-wx-root').html('<a href="${BASE_PATH}/shop/wechat">微信公众号</a>');
                } else {
                    $('.js-wx-promotion').remove();
                }
            },
            error: function () {
                $('.js-nav-wx-list').remove();
                $('.js-wx-promotion').remove();
            }
        });
        /*$ 微信公众号判断 */

        /*^ 导航收起与展开模块 */
        var $current = $('.current', '.aside-nav');
        if ($current.length) {
            $current.parents('.aside-nav-list').show(500);
        } else {
            $('.aside-nav-list', '.aside-nav').eq(0).show(500);
        }

        $(document).on('click', '.aside-nav-root', function () {
            $(this).next('.aside-nav-list').toggle(500);
        });
        /*$ 导航收起与展开模块 */
    });
</script>
