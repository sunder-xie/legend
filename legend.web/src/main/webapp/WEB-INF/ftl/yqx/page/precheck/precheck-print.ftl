<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <title>维修保养委托书</title>
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/common/print/print_common.css?e174653fe93fff7de684a409310c97d7">
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/precheck/precheck_print.css?2547354498f9b00a6402053a2e60fe18">
    <script type="text/javascript" src="${BASE_PATH}/resources/js/lib/jquery.js"></script>
    <script>
        $(document).ready(function ($) {
            window.print();
        });
    </script>
</head>
<body>
<#if SESSION_SHOP_LEVEL != 10 && SESSION_SHOP_LEVEL != 11 && SESSION_SHOP_LEVEL != 12>
<p class="tq_print_logo"><img src="${BASE_PATH}/resources/images/print_logo.png"/></p>
</#if>
<div class="wts_head">${shop.companyName}</div>
<div class="gongsi_name">维修保养委托书</div>
<div class="wb_b">
    <span class="wts_head_dh">单号:${precheckHead.precheckSn}</span>
    <span class="wb_ri">开单日期:${precheckHead.gmtCreateStr}</span>
</div>
<table>
    <tr>
        <td class="half_tab" colspan="2">车主：${customerInfo.customerName}</td>
        <td class="half_tab" colspan="2">车主电话：${customerInfo.mobile}</td>
        <td class="half_tab" colspan="2">公司名称：${contacts.company}</td>
    </tr>
    <tr>
        <td class="half_tab" colspan="3">联系人：${contacts.contact}</td>
        <td class="half_tab" colspan="3">联系电话：${contacts.contactMobile}</td>
    </tr>
    <tr>
        <td class="half_tab" colspan="4">地址：${contacts.customerAddr}</td>
        <td class="half_tab" colspan="2">下次保养里程：${precheckHead.upkeepMileage}</td>
    </tr>
    <tr>
        <td class="third_tab" colspan="2">车牌：${customerInfo.license}</td>
        <td class="third_tab" colspan="2">VIN码：${customerInfo.vin}</td>
        <td class="third_tab" colspan="2">下次保养时间：${precheckHead.nextTime}</td>
    </tr>
    <tr>
        <td class="third_tab" colspan="2">车辆型号：${customerInfo.carInfo}</td>
        <td class="third_tab" colspan="2">发动机号：${customerInfo.engineNo}</td>
        <td class="third_tab" colspan="2">维修类型：</td>
    </tr>
    <tr>
        <td class="third_tab bottom" colspan="2">购置日期：${customerInfo.buyTimeStr}</td>
        <td class="third_tab bottom" colspan="2">行驶里程：${precheckHead.mileage}</td>
        <td class="third_tab bottom" colspan="2">承保公司：${precheckHead.insurance}</td>
    </tr>
</table>
<table>
    <tr>
        <td class="ck_title" colspan="6">车况进场检测(不正常请标明)</td>
    </tr>
    <tr>
        <td class="car_img bottom" colspan="3">
            <span class="waiguan">外观</span>
            <span class="jianyi_licheng">轮胎    建议更换里程<#if tyre_change.precheckItemId> <span
                    class="content">${tyre_change.suggestion}</span> <#else>__________</#if>公里</span>

            <div class="checkcarpic"><img src="${BASE_PATH}/static/img/page/magic/wg.jpg"/></div>
        <#include "yqx/tpl/order/precheck-tpl.ftl">

        </td>
        <td class="ul_img bottom" colspan="3">
            <div class="chenei">车内</div>
            <ul class="neishi_ul">
                <li class="neishi">
                    <span>灯光</span><span
                        class="content"><#if out_lights.precheckItemId>${out_lights.suggestion}<#else></#if></span>
                </li>
                <li class="neishi">
                    <span>内饰</span><span
                        class="content"><#if interior.precheckItemId>${interior.suggestion}<#else></#if></span>
                </li>
                <li class="neishi">
                    <span>显示屏</span><span
                        class="content"><#if screen.precheckItemId>${screen.suggestion}<#else></#if></span>
                </li>
                <li class="neishi">
                    <span>音响</span><span
                        class="content"><#if audio.precheckItemId>${audio.suggestion}<#else></#if></span>
                </li>
                <li class="neishi">
                    <span>点烟器</span><span
                        class="content"><#if cigar_lighter.precheckItemId>${cigar_lighter.suggestion}<#else></#if></span>
                </li>
                <li class="neishi">
                    <span>升降器</span><span
                        class="content"><#if glass_lifter.precheckItemId>${glass_lifter.suggestion}<#else></#if></span>
                </li>
                <li class="neishi">
                    <span>自动门锁</span><span
                        class="content"><#if auto_door.precheckItemId>${auto_door.suggestion}<#else></#if></span>
                </li>
                <div class="clear"></div>
            </ul>

            <div class="yibiao">仪表</div>
            <ul class="neishi_ul">
                <li class="neishi">
                    <span>仪表灯</span><span
                        class="content"><#if dashboard.precheckItemId>${dashboard.suggestion}<#else></#if></span>
                </li>
                <li class="neishi">
                    <span>故障灯</span><span
                        class="content"><#if trouble_light.precheckItemId>${trouble_light.suggestion}<#else></#if></span>
                </li>
                <li class="neishi">
                    <span>油表油量</span><span
                        class="content"><#if oil_meter.precheckItemId>${oil_meter.precheckValue}<#else></#if></span>
                </li>
                <div class="clear"></div>
            </ul>
            <div class="yibiao">发动机</div>
            <ul class="neishi_ul">
                <li class="neishi">
                    <span>异响</span><span
                        class="content"><#if noise.precheckItemId>${noise.suggestion}<#else></#if></span>
                </li>
                <li class="neishi">
                    <span>使用异常</span><span
                        class="content"><#if func_error.precheckItemId>${func_error.suggestion}<#else></#if></span>
                </li>
                <div class="clear"></div>
            </ul>
            <div class="yibiao">附加检测(建议更换里程)</div>
            <ul class="neishi_ul">
                <li class="neishi">
                    <span>三滤</span><span
                        class="content"><#if three_filters.precheckItemId>${three_filters.suggestion}<#else></#if></span>
                </li>
                <li class="neishi">
                    <span>机油</span><span
                        class="content"><#if engine_oil.precheckItemId>${engine_oil.suggestion}<#else></#if></span>
                </li>
                <li class="neishi">
                    <span>火花塞</span><span
                        class="content"><#if spark_plug.precheckItemId>${spark_plug.suggestion}<#else></#if></span>
                </li>
                <li class="neishi">
                    <span>刹车油</span><span
                        class="content"><#if brake_oil.precheckItemId>${brake_oil.suggestion}<#else></#if></span>
                </li>
                <li class="neishi">
                    <span>雨刮</span><span
                        class="content"><#if rain_wiper.precheckItemId>${rain_wiper.suggestion}<#else></#if></span>
                </li>
                <li class="neishi">
                    <span>变速箱油</span><span
                        class="content"><#if gear_oil.precheckItemId>${gear_oil.suggestion}<#else></#if></span>
                </li>
                <li class="neishi">
                    <span>刹车</span><span
                        class="content"><#if brake_pad.precheckItemId>${brake_pad.suggestion}<#else></#if></span>
                </li>
                <li class="neishi">
                    <span>防冻液</span><span
                        class="content"><#if antifreeze.precheckItemId>${antifreeze.suggestion}<#else></#if></span>
                </li>
                <li class="neishi">
                    <span>电瓶</span><span
                        class="content"><#if battery.precheckItemId>${battery.suggestion}<#else></#if></span>
                </li>
                <li class="neishi">
                    <span>助力液</span><span
                        class="content"><#if steering_fluid.precheckItemId>${steering_fluid.suggestion}<#else></#if></span>
                </li>
                <div class="clear"></div>
            </ul>
            <div class="yibiao">随车物品(请勾选)</div>
            <ul class="scwp_ul">
                <li class="neishi_1"><span>车钥匙</span>
                    <img class="sel_border" <#if key.precheckItemId>src="${BASE_PATH}/resources/images/selected.png"
                         <#else>src=""</#if> />
                </li>
                <li class="neishi_1"><span>灭火器</span>
                    <img class="sel_border"
                         <#if extinguisher.precheckItemId>src="${BASE_PATH}/resources/images/selected.png"
                         <#else>src=""</#if> />
                </li>
                <li class="neishi_1">
                    <span>随车工具箱</span>
                    <img class="sel_border"
                         <#if tool_box.precheckItemId>src="${BASE_PATH}/resources/images/selected.png"
                         <#else>src=""</#if> />
                </li>
                <li class="neishi_1"><span>千斤顶</span>
                    <img class="sel_border" <#if jack.precheckItemId>src="${BASE_PATH}/resources/images/selected.png"
                         <#else>src=""</#if> />
                </li>
                <li class="neishi_1"><span>备胎</span>
                    <img class="sel_border"
                         <#if back_tyre.precheckItemId>src="${BASE_PATH}/resources/images/selected.png"
                         <#else>src=""</#if> />
                </li>
                <li class="neishi_1"><span>驾驶证</span>
                    <img class="sel_border"
                         <#if drive_license.precheckItemId>src="${BASE_PATH}/resources/images/selected.png"
                         <#else>src=""</#if> />
                </li>
                <li class="neishi_1"><span>行驶证</span>
                    <img class="sel_border"
                         <#if car_license.precheckItemId>src="${BASE_PATH}/resources/images/selected.png"
                         <#else>src=""</#if> />
                </li>
                <li class="neishi_1"><span>随车资料</span>
                    <img class="sel_border"
                         <#if file_in_car.precheckItemId>src="${BASE_PATH}/resources/images/selected.png"
                         <#else>src=""</#if> />
                </li>
                <li class="neishi_2"><span>其他</span><#if other_suggestion.precheckItemId><span
                        class="content">${other_suggestion.suggesstion}</span><#else>____________</#if>
                </li>
                <div class="clear"></div>
            </ul>
        </td>
    </tr>
</table>
<table>
    <tr>
        <td class="ck_title" colspan="6">其他说明</td>
    </tr>
    <tr>
        <td class="xuhao">序号</td>
        <td class="neirong" colspan="2">内容</td>
        <td class="yaoqiu" colspan="3">要求(预计所需配件)</td>
    </tr>
<#list requestList as info>
    <tr>
        <td class="xuhao">${info.id}</td>
        <td class="neirong" colspan="2">${info.content}</td>
        <td class="yaoqiu" colspan="3">${info.contentGoods}</td>
    </tr>
</#list>
    <tr>
        <td class="sixed">预计工时(小时)</td>
        <td class="third_tab" colspan="2">${precheckHead.manHour}</td>
        <td class="sixed">预计总费用(元)</td>
        <td class="third_tab" colspan="2">${precheckHead.expFee}</td>
    </tr>
    <tr>
        <td class="tishi_and_jianyi" colspan="6">提示和建议：
    </tr>
    <tr>
        <td class="xieyi" colspan="6">维修保养委托书协议：
            <div class="xieyi_nr">1 本人完全了解本单位所列维修项目，并授权贵公司维修保养；2 预计费用、预计工时仅供参考，结算以实际维修费用为准；3
                本人授权公司因为维修需要对车辆进行路试，并以本车保险作为保障；4 顾客所交车辆如遇自然灾害等不可抗力之灾本公司恕不赔偿；5 本公司对入厂车辆内非本车装备之贵重物品的丢失不付任何责任；6
                本公司对旧件保管期为7天，之后本公司将自行处理；7 如涉及维修质量纠纷，按行业有关规定处理。
            </div>
        </td>
    </tr>
    <tr>
        <td class="khqz" colspan="3">服务顾问签字：<span class="ymd">年月日</span></td>
        <td class="khqz" colspan="3">客户签字：<span class="ymd">年月日</span></td>
    </tr>
</table>
<ul class="wxby_foot">
    <!--
    <li class="wf_name">杭州豪德汽车服务有限公司</li>
    <li class="wf_add">杭州市临平临平东路168号</li>
    <li class="wf_tqmall">杭州淘汽云修服务平台技术支持</li>
    <li class="wxrx">维修热线：0571-12345678</li>
    <li class="cz">传真：0571-987656543</li>
    <li class="telphone">400-9937-288</li>
    <li class="web_add">www.tqmall.com</li>
    <li class="tp_2">0571-12345567</li>
    <li class="qxrx">抢修热线：1234568654</li>
    <li class="ewm"></li>
    -->
</ul>
</body>
</html>
