<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet"
      href="${BASE_PATH}/static/css/common/warehouse/share/share-common.css?ae097a4de686f0eba0d6e4c670b7d664"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/share/sale.css?eda57f3a9a184c0fc5e194b599db75f6"/>
<!-- 样式引入区 end-->
<input type="hidden" id="isHasGoodsOnSale" value="${isShowList}">

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="wrapper-left fl">
    <#include "yqx/tpl/warehouse/warehouse-nav-tpl.ftl">
    </div>
<#--选择出售库存配件列表 start-->
    <div class="wrapper-main warehouse fr hide warehouse-goods-list">
        <h1 class="title">购买库存配件</h1>

        <div class="container">
            <div class="list-box">
                <div class="contact-box">
                    <h3 class="title">门店联系方式
                        <#if SESSION_USER_IS_ADMIN == 1>
                        <button class="yqx-btn yqx-btn-3 js-contact-edit shop-edit fr">编辑</button>
                        </#if>
                    </h3>
                    <ul>
                        <li>
                            <label>门店联系人：</label>

                            <div class="form-item">
                                <i class="shop-user"></i>
                                <input name="userId" type="hidden">
                            </div>
                        </li>
                        <li class="li-tel">
                            <label>联系电话：</label>

                            <div class="form-item">
                                <i class="shop-tel"></i>
                            </div>
                        </li>
                    </ul>
                    <ul>
                        <li>
                            <label>门店位置：</label>

                            <div class="form-item">
                                <i class="shop-address"></i>
                            </div>
                        </li>
                    </ul>
                </div>
                <div class="list-header clearfix">
                    <h3 class="fl">选择出售库存配件</h3>
                </div>
            <#--选择出售库存配件-->
                <table class="yqx-table yqx-table-hover">
                    <thead>
                    <tr>
                        <th style="width: 15px;">
                            <input type="checkbox" class="js-onsale-pick-all" checked>
                        </th>
                        <th class="no">序号</th>
                        <th class="name">配件名称</th>
                        <th class="stock-num">库存数量</th>
                        <th class="in-time">入库日期</th>
                        <th class="get-price">进货价(元)</th>
                        <th class="sale-num">销售数量</th>
                        <th class="sale-price">销售价(元)</th>
                    </tr>
                    </thead>
                    <tbody id="goodsListFill">

                    </tbody>
                </table>
                <div class="yqx-page" id="goodsListPage"></div>
                <button class="yqx-btn yqx-btn-3 choose-btn js-choose-dialog">继续选择出售配件</button>
                <div class="picked-status">已选择配件 <i class="pick-num">0</i> 件</div>
                <div class="footer clearfix">
                    <button class="fl yqx-btn yqx-btn-2 publish-btn js-publish">发布已选配件</button>
                    <button class="fr yqx-btn yqx-btn-1 js-to-sale to-sale-btn">返回</button>
                </div>
            </div>
        </div>
    </div>
<#--选择出售库存配件列表 end-->

    <div class="wrapper-main warehouse-share warehouse fr">
        <h1 class="title">库存配件出售<i class="sub">超过两个月未周转库存</i></h1>

        <div class="top-box">
            <div class="title-box stock-title">
                <div class="s-icon-box">
                    <div class="back-icon">
                        <div class="icon"></div>
                    </div>
                </div>
                <div class="text-box">
                    <h2 class="number">
                        <i id="goodsKindsNum" class="num"></i><i class="unit"> 个</i>
                    </h2>
                    <div class="name">
                        未周转配件<div class="name-desc js-title-tips">
                        <div class="icon"></div>
                        <div class="tips-box hide">
                            <i>超过两个月未出售配件数量</i>
                            <div class="tips-angle"></div>
                            <div class="tips-angle-border"></div>
                        </div>
                        </div>
                    </div>
                </div>
            </div><div class="title-box encounter-price">
                <div class="s-icon-box">
                    <div class="back-icon">
                        <div class="icon"></div>
                    </div>
                </div>
                <div class="text-box">

                    <h2 class="number">
                        <i id="inventoryPrice" class="num"></i><i class="unit"> 元</i>
                    </h2>
                    <div class="name">
                        <i>进货金额</i><div class="name-desc js-title-tips">
                            <div class="icon"></div>
                            <div class="tips-box hide">
                                <i>进货金额 = 平均进货价 * 库存数量</i>
                                <div class="tips-angle"></div>
                                <div class="tips-angle-border"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div><div class="title-box loss">
                <div class="s-icon-box">
                    <div class="back-icon">
                        <div class="icon"></div>
                    </div>
                </div>
                <div class="text-box">
                    <h2 class="number">
                        <i id="lossAmount" class="num"></i><i class="unit"> 元</i>
                    </h2>

                    <div class="name">
                        近两个月损失<div class="name-desc js-title-tips">
                        <div class="icon"></div>
                        <div class="tips-box hide">
                            <i>近两个月损失 =（销售价 - 进货价）* 月周转率</i>
                            <div class="tips-angle"></div>
                            <div class="tips-angle-border"></div>
                        </div>
                    </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="init-box <#if isShowList>hide</#if>">
            <h2 class="sub-title">库存配件出售流程</h2>

            <div class="process-box">
                <ol class="clearfix">
                    <li>
                        <div class="step-box first">
                            <img src="${BASE_PATH}/static/img/page/warehouse/share/step-first.png" class="icon">
                        </div>
                        <div class="text-box">
                        <h3>第一步</h3>

                        <p>选择出售配件</p>
                            </div>
                    </li>
                    <li>
                        <div class="step-box second">
                            <img src="${BASE_PATH}/static/img/page/warehouse/share/step-second.png" class="icon">
                        </div>
                        <div class="text-box">
                        <h3>第二步</h3>

                        <p>确定配件售价、联系信息</p>
                            </div>
                    </li>
                    <li>
                        <div class="step-box third">
                            <img src="${BASE_PATH}/static/img/page/warehouse/share/step-third.png" class="icon">
                        </div>
                        <div class="text-box">
                        <h3>第三步</h3>

                        <p>发布出售配件</p>
                            </div>
                    </li>
                </ol>
                <p>
                    <button class="yqx-btn yqx-btn-3 start-btn js-start">开始选择出售配件</button>
                </p>
            </div>
            <footer class="declare-info">
                <p>库存配件买卖说明：</p>
                <ol class="info-box">
                    <li>1、云修库存配件买卖功能是为云修门店提供滞销配件流通渠道，出售配件门店可以发布需要出售配件信息，全国其他云修店可查看配件、购买配件；</li>
                    <li>2、需要购买配件的云修门店可以查看配件详细信息，获取门店联系方式，联系出售配件门店进行配件交易；</li>
                    <li>3、淘汽云修仅提供配件信息平台，不负责产品真伪鉴定；配件买卖双方请核实产品质量及价格；双方产生的纠纷均与淘汽云修无关。</li>
                </ol>
            </footer>
        </div>
        <div class="container <#if !isShowList>hide</#if>">
            <h2 class="font-yahei container-title">出售库存配件</h2>
            <ul class="list-tab-box js-list-tab">
                <li class="list-tab current-tab" id="allSaleNum">
                    <p>全部</p>

                    <p class="count"></p>
                </li><li class="list-tab" data-status="1" id="onsaleNum">
                    <p>出售中</p>

                    <p class="count"></p>
                </li><li class="list-tab" data-status="0" id="waitForSaleNum">
                    <p>停止出售</p>

                    <p class="count"></p>
                </li><li class="list-tab" data-status="9" id="hasSensitiveWordsNum">
                    <p>审核未通过</p>

                    <p class="count"></p>
                </li>
                <li class="fr">
                    <button class="yqx-btn yqx-btn-3 choose-goods js-choose-dialog">选择出售配件</button>
                </li>
            </ul>
            <div class="list-box">
                <div class="hide" id="saleListForm">
                    <input name="goodsStatus">
                </div>
                <table class="yqx-table yqx-table-hover">
                    <thead>
                    <tr>
                        <th class="no">序号</th>
                        <th class="name">配件名称</th>
                        <th class="stock-num">库存数量</th>
                        <th class="in-time">入库日期</th>
                        <th class="get-price">进货价(元)</th>
                        <th class="sale-num" style="width: 80px;">销售数量</th>
                        <th class="sale-price" style="width: 70px;">销售价(元)</th>
                        <th class="sale-status">出售状态</th>
                        <th class="remark hide">备注</th>
                        <th class="change-status">修改状态</th>
                    </tr>
                    </thead>
                    <tbody id="saleListFill">

                    </tbody>
                </table>
                <div class="yqx-page" id="saleListPage"></div>
            </div>
            <h1 class="title shop-title">门店联系方式
                <#if SESSION_USER_IS_ADMIN == 1><button class="yqx-btn yqx-btn-3 js-contact-edit shop-edit fr">编辑</button></#if>
            </h1>
            <div class="contact-box">
                <ul>
                    <li>
                        <label>门店联系人：</label>

                        <div class="form-item">
                            <i class="shop-user"></i>
                        </div>
                    </li>
                    <li class="li-tel">
                        <label>联系电话：</label>

                        <div class="form-item">
                            <i class="shop-tel"></i>
                        </div>
                    </li>
                </ul>
                <ul>
                    <li>
                        <label>门店位置：</label>

                        <div class="form-item">
                            <i class="shop-address"></i>
                        </div>
                    </li>
                </ul>
            </div>
            <footer class="declare-info">
                <p>库存配件买卖说明：</p>
                <ol class="info-box">
                    <li>1、云修库存配件买卖功能是为云修门店提供滞销配件流通渠道，出售配件门店可以发布需要出售配件信息，全国其他云修店可查看配件、购买配件；</li>
                    <li>2、需要购买配件云修门店可以查看配件详细信息，获取门店联系方式，联系出售配件门店进行配件交易；</li>
                    <li>3、淘汽云修仅提供配件信息平台，不负责产品真伪鉴定；配件买卖双方请核实产品质量及价格；双方产生纠纷均与淘汽云修无关。</li>
                </ol>
            </footer>
        </div>
    </div>
    <!-- 左侧导航区 end -->
</div>
<!-- 表格模板 end -->
<div class="hide contact-dialog yqx-dialog">
    <header>
        <h2 class="dialog-title">编辑门店联系方式</h2>
    </header>
    <section>
        <ul>
            <input id="adminId" value="${SESSION_USER_ID}" hidden>
            <li>门店联系人
                <div class="form-item">
                    <input class="js-shop-user yqx-input shop-user" data-v-type="required" name="name">
                    <input type="hidden" name="userId">
                    <span class="fa icon-angle-down"></span>
                </div>
            </li>
            <li>
                联系电话：<i class="shop-tel"></i>
            </li>
            <li>
                门店地址：<i class="shop-address"></i>
            </li>
        </ul>
    </section>
    <footer class="clearfix">
        <button class="fl yqx-btn yqx-btn-2 js-contact-confirm">确定</button>
        <button class="fr yqx-btn yqx-btn-1 js-close">返回</button>
    </footer>
</div>

<script type="text/template" id="saleListTpl">
    <%var item;%>
    <%var status = null;%>
    <%for(var i in json.data.content) {%>
    <%item = json.data.content[i];%>
    <%status = {}%>
    <%if(item.goodsStatus == 0) {%><% status = {str: '待出售', btn: '出售', class: 'blocked', btnClass: 'js-republish', targetStatus: '1'};%><%}%>
    <%if(item.goodsStatus == 1) {%><% status = {str: '出售中', btn: '停止出售', class: 'in-sale', btnClass: 'js-change-status', targetStatus: '0'};%><%}%>
    <%if(item.goodsStatus == 9) {%><% status = {str: '审核未通过', btn: '重新发布', class: 'blocked', btnClass: 'js-republish', targetStatus: '1'};%><%}%>
    <tr class="<%=status.class||''%>" data-id="<%=item.id%>">
        <td><%=(+i + 1) + (json.data.page - 1) * json.data.size%></td>

        <td class="name">
            <%if(item.goodsStatus == 0 || item.goodsStatus == 9) {%>
            <div class="form-item">
                <textarea class="yqx-textarea goods-name" data-v-type="required|maxLength:50"><%=item.goodsName%></textarea>
            </div>
            <%} else {%>
            <div class="form-item">
                <i class="goods-name ellipsis-2 js-show-tips"><%=item.goodsName%></i>
            </div>
            <%}%>
        </td>
        <td class="stock-num"><%=item.goodsStock%><i class="measure-unit"><%=item.measureUnit%></i></td>
    <#--入库日期-->
        <td><%=item.lastInTimeStr%></td>
        <td class="get-price"><%=toPrice(item.inventoryPrice)%></td>
        <td class="sale-num"><%=item.saleNumber%><i class="measure-unit"><%=item.measureUnit%></i></td>
        <td class="sale-price price">
            <%if(item.goodsStatus == 0 || item.goodsStatus == 9) {%>
            <div class="form-item">
                <input class="yqx-input goods-price" data-v-type="required|number:销售价|maxValue:99999999|price" name="goodsPrice"
                       value="<%=item.goodsPrice%>">
            </div>
            <%} else {%>
            <div class="form-item">
                <i class="goods-price"><%=toPrice(item.goodsPrice)%></i>
            </div>
            <%}%>
        </td>
        <%if(item.goodsStatus == 9) {%>
        <td class="sale-status blocked-word">
            <p>审核</p>
            <p>未通过</p>
        </td>
        <%} else {%>
        <td class="sale-status">
            <%=status.str%>
        </td>
        <%}%>
        <td class="remark  <%if(prams.goodsStatus != 9) {%>hide<%}%>">
            <div class="ellipsis-2 js-show-tips">
            <%=item.goodsRemark%>
                </div>
        </td>
        <td class="change-status">
            <button class="status-btn <%=status.btnClass||''%>" data-target-status="<%=status.targetStatus || ''%>">
                <%=status.btn%>
            </button>
        </td>
    </tr>
    <%}%>
    <%if(i === undefined) {%>
    <%if(prams.goodsStatus != 9) {%>
    <tr>
        <td colspan="9">暂无数据</td>
    </tr>
    <%} else {%>
    <tr>
        <td colspan="10">暂无数据</td>
    </tr>
    <%}%>
    <%}%>
</script>

<#--选择出售库存配件-->
<script type="text/template" id="goodsListTpl">
    <%var item;%>
    <%for(var i in data) {%>
    <%item = data[i];%>
    <tr id="goodsList_<%=item.id%>" data-id="<%=item.id%>">
        <td>
            <label>
                <input class="js-goods-pick" checked type="checkbox" data-id="<%=item.id%>">
            </label>
        </td>
        <td><%=(+i + 1)%></td>
        <td class="name">
            <div class="form-item">
                <textarea class="yqx-textarea js-picked-input goods-name ellipsis-2" name="goodsName" data-v-type="required|maxLength:50"><%=item.name%></textarea>
            </div>
        </td>
        <td class="stock-num"><%=item.stock%><i class="measure-unit"><%=item.measureUnit%></i></td>
    <#--入库日期-->
        <td><%=item.lastInTimeStr%></td>
        <td class="get-price"><%=toPrice(item.inventoryPrice)%></td>
        <td>
            <div class="form-item">
                <input class="yqx-input js-picked-input text-center" name="saleNumber" value="<%=item.saleNumber || item.stock%>" data-v-type="required | number:数量"><i
                    class="measure-unit"><%=item.measureUnit%></i>
            </div>
        </td>
        <td class="sale-price">
            <div class="form-item">
                <input class="yqx-input js-picked-input" name="goodsPrice" value="<%=item.price%>"
                       data-v-type="required | number:销售价 |maxValue:99999999|price">
            </div>
        </td>
    </tr>
    <%}%>
    <%if(i === undefined) {%>
    <tr>
        <td colspan="8">暂无数据</td>
    </tr>
    <%}%>
</script>
<#--选择配件弹框-->
<#include "yqx/page/warehouse/share/choose-goods-tpl.ftl">
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/warehouse/share/sale.js?6cff2489c17087c8a7eb8196d1803c3f"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">