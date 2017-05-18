<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/print-setting-detail.css?96a93ea2fed96a8446729596d8d4e371">
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/print/print.css?0c3994b5134160a03fdfc1ddc44657ff"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/print/print-order.css?9dcbc7be883ae8bc24691406d844e26d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/print/print-setting.css?15821b0c071ace199da1b907de7bc227"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <!-- 左侧导航区 end -->
    <input id="configInfo" value='${shopPrintConfig.configField}'' hidden>
    <input value="${shopPrintConfig.fontStyle}" id="fontStyle" hidden>
    <input value="${shopPrintConfig.printType}" id="printType" hidden>

    <!-- 右侧内容区 start -->
    <h1 class="fl font-yahei">打印设置</h1>
    <div class="order-right fl">
        <ul class="tab-box js-tab">
            <li class="tab-item<#if moduleUrl == 1 || (moduleUrl != 2 && moduleUrl != 3 && moduleUrl != 4)> current-item</#if>"
                data-print-template="1"
                data-target="dispatch">
                派工单
            </li><li class="tab-item<#if moduleUrl == 2> current-item</#if>"
                data-print-template="2"
                data-target="settle">
                结算单
            </li><li class="tab-item<#if moduleUrl == 3> current-item</#if>"
                data-print-template="3"
                data-target="bill">
                报价单
            </li><li class="tab-item<#if moduleUrl == 4> current-item</#if>"
                data-print-template="4"
                data-target="car-test">
                试车单
            </li>
        </ul>
    <#-- 派工单 start -->
        <div class="main dispatch-main hide">
        <div class="container print-container hide dispatch-container">
            <#include "yqx/tpl/print/order.ftl">
        </div><ul class="option-box dispatch hide">
            <li>
                <h3 class="title">选择打印版本</h3>
                <ul class="version-box js-choose-version">
                    <li data-print-type="1" class="
                    <#if shopPrintConfig.printType == 1 && shopPrintConfig.printTemplate == 1>tag-control selected</#if>
                    "
                    >
                        简单版
                    </li><li data-print-type="2" class="
                    <#if shopPrintConfig.printType == 2 && shopPrintConfig.printTemplate == 1>tag-control selected</#if>
                    ">
                        标准版
                    </li>
                    </ul>
                <ul class="version-box js-choose-version" style="padding-top: 0;">
                    <li data-print-type="3" class="
                    <#if shopPrintConfig.printType == 3 && shopPrintConfig.printTemplate == 1>tag-control selected</#if>
                    ">
                        专业版
                    </li><li data-print-type="4" class="
                    <#if shopPrintConfig.printType == 4 && shopPrintConfig.printTemplate == 1>tag-control selected</#if>
                    ">
                        自定义
                    </li>
                </ul>
            </li>
            <li class="font-box">
                <h3 class="title">打印样式</h3>
                <ul class="striped-content hide">
                    <li>
                        <label>
                            <input type="checkbox" class="js-table"
                                   data-key="line"
                                   data-target="striped">增加表格字段分割线
                        </label>
                    </li>
                </ul>
                <p>打印字体选择（将用在所有单据上）</p>
                <ul>
                    <li>
                        <label>
                            <input type="radio" name="1" class="js-font" value="font-12">大字体
                        </label>
                        <label>
                            <input type="radio" name="1" class="js-font" value="" checked>小字体
                        </label>
                    </li>
                </ul>
            </li>
            <li class="line-height-16">
                <h3 class="title" style="margin-bottom: 8px;">门店信息</h3>
                <p>将直接显示门店资料中门店注册公司</p>
                <p>名称，以及门店地址和门店联系电话</p>
            </li>
            <li class="hide">
                <h3 class="title">车辆信息</h3>
                <p>选择需要打印的信息</p>
                <ul>
                    <li>
                        <label>
                            <input type="checkbox" checked>车牌
                        </label>
                        <label>
                            <input type="checkbox" checked>车辆颜色
                        </label>
                        <label>
                            <input type="checkbox" checked data-target="行驶里程">行驶里程数
                        </label>
                        <label>
                            <input type="checkbox" checked>承保公司
                        </label>
                        <label>
                            <input type="checkbox" checked>发动机号
                        </label>
                        <label>
                            <input type="checkbox" checked>VIN码
                        </label>
                        <label>
                            <input type="checkbox" checked>油表油量
                        </label>
                        <label>
                            <input type="checkbox" checked>车辆型号
                        </label>
                    </li>
                </ul>
            </li>
            <li class="hide">
            <h3 class="title">维修信息</h3>
            <p>选择需要打印的信息</p>
            <ul>
                <li>
                    <label>
                        <input type="checkbox" checked>维修类别
                    </label>
                    <label>
                        <input type="checkbox" checked>进场方式
                    </label>
                    <label>
                        <input type="checkbox" checked>洗车
                    </label>
                    <label>
                        <input type="checkbox" checked>旧件带回
                    </label>
                    <label>
                        <input type="checkbox" checked>客户描述
                    </label>
                    <div class="empty-choose-box">
                        <p>选择需要打印的信息</p>
                        <ul>
                            <li>
                                <label><input type="checkbox"
                                              class="js-enable-empty"
                                              data-target="service"
                                        >服务项目留空白</label>
                                <ul class="radio-box">
                                    <li>
                                        <label>
                                        <input type="radio"
                                               class="js-empty-line"
                                               data-name="服务留白"
                                               name="service" data-target="service|1">预留一行空白行
                                        </label>
                                    </li>
                                    <li>
                                        <label>
                                        <input type="radio"
                                               class="js-empty-line"
                                               data-name="服务留白"
                                               name="service" data-target="service|2">预留两行空白行
                                        </label>
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <label><input type="checkbox"
                                              class="js-enable-empty"
                                              data-target="goods"
                                        >配件项目留空白</label>
                                <ul class="radio-box">
                                    <li>
                                        <label>
                                            <input type="radio"
                                                   class="js-empty-line"
                                                   data-name="配件留白"
                                                   name="goods" data-target="goods|1">预留一行空白行
                                        </label>
                                    </li>
                                    <li>
                                        <label>
                                            <input type="radio"
                                                   class="js-empty-line"
                                                   data-name="配件留白"
                                                   name="goods" data-target="goods|2">预留两行空白行
                                        </label>
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <label><input type="checkbox"
                                              class="js-enable-empty"
                                              data-target="addon"
                                        >附加项目留空白</label>
                                <ul class="radio-box">
                                    <li>
                                        <label>
                                            <input type="radio"
                                                   class="js-empty-line"
                                                   data-name="附加费用项目留白"
                                                   name="addon" data-target="addon|1">预留一行空白行
                                        </label>
                                    </li>
                                    <li>
                                        <label>
                                            <input type="radio"
                                                   class="js-empty-line"
                                                   data-name="附加费用项目留白"
                                                   name="addon" data-target="addon|2">预留两行空白行
                                        </label>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </li>
            </ul>
            </li>
            <li class="hide">
                <h3 class="title">其他信息</h3>
                <p>选择需要打印的信息</p>
                <ul>
                    <li>
                        <label>
                            <input type="checkbox" checked data-target="fixRecord">维修项目修改记录
                        </label>
                        <label>
                            <input type="checkbox" checked>维修建议
                        </label>
                        <label>
                            <input type="checkbox" checked>检验签字
                        </label>
                        <label>
                            <input type="checkbox" checked data-target="提示语">提示语（免责信息）
                            <textarea class="yqx-input info-show js-info-show">1.此单一式三联，客户白联用户提车，红联用户系统开单，黄联用户车辆检修。
2.友情提示：请您将车内贵重物品及现金带走，如有遗失本公司盖不承担责任。
3.维修项目以工单为准。</textarea>
                        </label>
                    </li>
                </ul>
            </li>
            <li class="btn-group">
                <button class="js-save yqx-btn yqx-btn-2">保存</button><button class="js-back yqx-btn yqx-btn-1">返回</button>
            </li>
        </ul>
    </div>
    <#-- 派工单 end -->
        <#--结算单-->
        <div class="main hide settle-main">
            <div class="container print-container settle-container">
                <#include "yqx/tpl/print/settle.ftl">
            </div>
            <ul class="option-box settle-option-box">
                <li>
                    <h3 class="title">选择打印版本</h3>
                    <ul class="version-box js-choose-version">
                        <li data-print-type="2" class="
                            <#if shopPrintConfig.printTemplate == 2 && shopPrintConfig.printType == 2>tag-control selected</#if>
                        "
                        >
                            标准版
                        </li><li data-print-type="4" class="
                            <#if shopPrintConfig.printType == 4 && shopPrintConfig.printTemplate == 2>tag-control selected</#if>
                        "
                                >
                            自定义
                        </li>
                    </ul>
                </li>
                <li class="font-box">
                    <h3 class="title">打印样式</h3>
                    <ul class="striped-content hide">
                        <li>
                            <label>
                                <input type="checkbox" class="js-table"
                                       data-key="line"
                                       data-target="striped">增加表格字段分割线
                            </label>
                        </li>
                    </ul>
                    <p>打印字体选择（将用在所有单据上）</p>
                    <ul>
                        <li>
                            <label>
                                <input type="radio" name="2" class="js-font" value="font-12">大字体
                            </label>
                            <label>
                                <input type="radio" name="2" class="js-font" value="" checked>小字体
                            </label>
                        </li>
                    </ul>
                </li>
                <li class="line-height-16">
                    <h3 class="title" style="margin-bottom: 8px;">门店信息</h3>
                    <p>将直接显示门店资料中门店注册公司</p>
                    <p>名称，以及门店地址和门店电话</p>
                </li>
                <li class="hide">
                    <h3 class="title">车辆信息</h3>
                    <p>选择需要打印的信息</p>
                    <ul>
                        <li>
                            <label>
                                <input type="checkbox" checked>车牌
                            </label>
                            <label>
                                <input type="checkbox" checked>车辆颜色
                            </label>
                            <label>
                                <input type="checkbox" checked data-target="行驶里程">行驶里程数
                            </label>
                            <label>
                                <input type="checkbox" checked>承保公司
                            </label>
                            <label>
                                <input type="checkbox" checked>发动机号
                            </label>
                            <label>
                                <input type="checkbox" checked>VIN码
                            </label>
                            <label>
                                <input type="checkbox" checked>油表油量
                            </label>
                            <label>
                                <input type="checkbox" checked>下次保养里程
                            </label>
                            <label>
                                <input type="checkbox" checked>车辆型号
                            </label>
                        </li>
                    </ul>
                </li>
                <li class="hide">
                    <h3 class="title">维修信息</h3>
                    <p>选择需要打印的信息</p>
                    <ul>
                        <li>
                            <label>
                                <input type="checkbox" checked>维修类别
                            </label>
                            <label>
                                <input type="checkbox" checked>进场方式
                            </label>
                            <label>
                                <input type="checkbox" checked>洗车
                            </label>
                            <label>
                                <input type="checkbox" checked>旧件带回
                            </label>
                            <label>
                                <input type="checkbox" checked data-target="打印时间">打印时间
                            </label>
                            <label>
                                <input type="checkbox" checked data-desc="note" data-target="服务备注">服务项目中的备注
                            </label>
                            <label>
                                <input type="checkbox" checked data-desc="note" data-target="配件备注">配件物料中的备注
                            </label>
                            <label>
                                <input type="checkbox" checked data-desc="note" data-target="附件备注">附加费用中的备注
                            </label>
                            <label class="table-merge">
                                <input type="checkbox" data-target="合并展示"><i>将服务项目、配件资料以及附加费用合并一起展示</i>
                            </label>
                        </li>
                    </ul>
                </li>
                <li class="hide">
                    <h3 class="title">其他信息</h3>
                    <p>选择需要打印的信息</p>
                    <ul>
                        <li>
                            <label>
                                <input type="checkbox" checked
                                       data-key="showAccount">会员卡计次卡剩余信息
                            </label>
                            <label>
                                <input type="checkbox" checked data-target="提示语">提示语（免责信息）
                                <textarea class="yqx-input info-show js-info-show">1.该单项目及金额经双方核实，客户签字生效
2.客户自带配件到我厂维修的项目本厂对此不作保修
3.挂账单位司机签名后将结算单带回单位主管部门，对结算信息如有异议须于三天内提出，逾期无异议则按结算单信息结算，并承诺自签字后30天内付清费用</textarea>
                            </label>
                        </li>
                    </ul>
                </li>
                <li class="btn-group">
                    <button class="js-save yqx-btn yqx-btn-2">保存</button><button class="js-back yqx-btn yqx-btn-1">返回</button>
                </li>
            </ul>
        </div>
        <#--结算单 end-->
        <#--报价单 start-->
        <div class="main hide bill-main">
            <div class="container print-container bill-container">
            <#include "yqx/tpl/print/bill.ftl">
            </div>
            <ul class="option-box bill-option-box">
                <li class="font-box">
                    <h3 class="title">打印样式</h3>
                    <p>打印字体选择（将用在所有单据上）</p>
                    <ul>
                        <li>
                            <label>
                                <input type="radio" name="3" class="js-font" value="font-12">大字体
                            </label>
                            <label>
                                <input type="radio" name="3" class="js-font" value="" checked>小字体
                            </label>
                        </li>
                    </ul>
                </li>
                <li class="line-height-16">
                    <h3 class="title" style="margin-bottom: 8px;">门店信息</h3>
                    <p>将直接显示门店资料中门店注册公司</p>
                    <p>名称，以及门店地址和门店联系电话</p>
                </li>
                <li class="btn-group">
                    <button class="js-save yqx-btn yqx-btn-2">保存</button><button class="js-back yqx-btn yqx-btn-1">返回</button>
                </li>
            </ul>
        </div>
        <#-- 报价单 end -->
        
        <#-- 试车单 start -->
        <div class="main hide car-test-main">
            <div class="container print-container car-test-container">
            <#include "yqx/tpl/print/car-test.ftl">
            </div>
            <ul class="option-box car-test-option-box">
                <li class="font-box">
                    <h3 class="title">打印样式</h3>

                    <p>打印字体选择（将用在所有单据上）</p>
                    <ul>
                        <li>
                            <label>
                                <input type="radio" name="4" class="js-font" value="font-12">大字体
                            </label>
                            <label>
                                <input type="radio" name="4" class="js-font" value="" checked>小字体
                            </label>
                        </li>
                    </ul>
                </li>
                <li class="line-height-16">
                    <h3 class="title" style="margin-bottom: 8px;">门店信息</h3>
                    <p>将直接显示门店资料中门店注册公司</p>
                    <p>名称，以及门店地址和门店联系电话</p>
                </li>
                <li class="btn-group">
                    <button class="js-save yqx-btn yqx-btn-2">保存</button><button class="js-back yqx-btn yqx-btn-1">返回</button>
                </li>
            </ul>
        </div>
        <#-- 试车单 end -->
    </div>

</div>
<script src="${BASE_PATH}/static/js/page/setting/print-setting-detail.js?0efc4fa2a19bf8bafa1cafb07b3e889f"></script>
<#include "yqx/layout/footer.ftl">