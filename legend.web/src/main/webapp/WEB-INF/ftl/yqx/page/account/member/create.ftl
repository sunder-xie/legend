<#include "yqx/layout/header.ftl">
<#--样式引入区-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/account/create.css?a40d1d0bbfbce9cc5ece513dc36e9458" type="text/css"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/member/create.css?2629accb2e1b77f36df8a905bee0cfac" type="text/css"/>
<#--样式引入区-->
<div class="yqx-wrapper clearfix">
    <div class="aside">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    </div>
    <div class="aside-main">
        <h3 class="Z-title">客户管理 > <a target="_self" href="${BASE_PATH}/account/setting"> 优惠设置 > </a>
            <i>
                <#if memberCardInfo?exists>
                    编辑会员卡类型
                <#else>
                    新增会员卡类型
                </#if>
            </i>
        </h3>

        <div class="order-panel">
            <div class="order-panel-body info-body">
                <div class="info info-base">
                    <h2 class="info-head">填写基本信息</h2>
                    <ul>
                        <li>
                            <div class="form-label form-label-must">
                                会员卡名称
                            </div>
                            <div class="form-item">
                                <input type="hidden" id="memberCardId" name="id" class="hidden" value="${memberCardInfo.id}">
                                <input type="text" name="name" class="yqx-input"
                                       placeholder="例：金卡"
                                       value="${memberCardInfo.typeName?default('')}" data-v-type="required | maxLength:20">
                            </div>
                        </li>
                        <li class="describe">
                            <div class="form-label">
                                描述
                            </div>
                            <div class="form-item">
                                <input type="text" name="descript"
                                       data-v-type="maxLength: 100"
                                       class="yqx-input yqx-input-icon"
                                       placeholder="例：享受店内服务工时9折，精品8.8折"
                                       value="${memberCardInfo.cardInfoExplain?default('')}">
                            </div>
                        </li>
                    </ul>
                </div>
                <div class="info info-region">
                    <h2 class="info-head">会员特权设定（设置会员折扣范围）</h2>
                    <input type="hidden" id="discount" value="${memberCardInfo.discountType}"/>
                    <div class="show-grid">
                            <div class="single-tag">
                                <div data-flag="0" class="card-discount <#if !memberCardInfo?exists>
                                            selected
                                        <#else>
                                            <#if memberCardInfo.discountType==0>selected</#if>
                                        </#if>">
                                    <div class="tag-control">无折扣</div><div class="tag-padding"></div>
                                </div>
                                <div data-flag="1" class="card-discount <#if memberCardInfo.discountType==1>selected</#if>">
                                    <div class="tag-control">
                                        整单折扣</div><div class="tag-padding">
                                    <div class="form-item">
                                        <input type="text" class="yqx-input yqx-input-small account_input service-all-discount"
                                               data-v-type="required | number | maxValue: 10"
                                               <#if memberCardInfo.discountType==1>
                                                    value="${memberCardInfo.orderDiscount}"
                                               <#else>
                                                    disabled
                                               </#if>
                                                name="orderDiscount">折
                                    </div>
                                    </div>
                                </div>
                                <div id="service" class="card-discount service <#if memberCardInfo.discountType==2||memberCardInfo.discountType==4>selected</#if>" data-flag="2">
                                <div class="tag-control">
                                    服务类型折扣</div><div class="tag-padding">
                                        <label class="js-all">全部服务<input type="radio" name="serviceDiscountType"
                                        <#if memberCardInfo.serviceDiscountType != 2>
                                             checked
                                        </#if>
                                             class="radio-discount" value="1"></label><div class="form-item">
                                        <input type="text" class="yqx-input yqx-input-small account_input service-all-discount"
                                               data-v-type="required | number | maxValue: 10"
                                            <#if (memberCardInfo.discountType==2||memberCardInfo.discountType==4) && memberCardInfo.serviceDiscountType == 1>
                                                value="${memberCardInfo.serviceDiscount}"
                                            <#else>
                                                   disabled
                                            </#if>
                                            name="serviceDiscount"><i>折</i>
                                        </div><label class="js-part">部分服务<input type="radio" name="serviceDiscountType"
                                                <#if (memberCardInfo.discountType == 2||memberCardInfo.discountType==4) && memberCardInfo.serviceDiscountType == 2>
                                                    checked
                                                </#if>
                                                    class="radio-discount" value="2"></label>
                                    </div>

                                </div>
                                <div class="table-box service-table <#if (memberCardInfo.discountType == 2||memberCardInfo.discountType==4) && memberCardInfo.serviceDiscountType == 2><#else>hide</#if>">
                                    <table class="yqx-table">
                                        <thead>
                                        <th>服务类型</th>
                                        <th>折扣</th>
                                        <th>操作</th>
                                        </thead>
                                    <#if memberCardInfo.cardServiceRels>
                                        <#list memberCardInfo.cardServiceRels as item>
                                            <tr class="service-datatr">
                                                <td>
                                                    <div class="form-item width-full service-item">
                                                        <input type="text"
                                                               placeholder="输入查询"
                                                               data-v-type="required"
                                                               value="${item.serviceCatName}"
                                                               class="yqx-input yqx-input-small js-service-type has-icon">
                                                        <i class="fa icon-small icon-angle-down"></i>
                                                        <input type="hidden" name="catId" value="${item.serviceCatId}">
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="form-item">
                                                        <input type="text" class="yqx-input"
                                                               data-v-type="number | required | maxValue:10"
                                                               value="${item.discount}"
                                                               name="discount"><i>折</i>
                                                    </div>
                                                </td>
                                                <td>
                                                    <i class="del js-del">删除</i>
                                                </td>
                                            </tr>
                                        </#list>
                                    </#if>
                                    </table>
                                    <button class="js-add-service yqx-btn-3 yqx-btn-small">添加服务类型</button>
                                </div>

                                <div class="card-discount goods <#if memberCardInfo.discountType==3||memberCardInfo.discountType==4>selected</#if>" data-flag="3" id="goods">
                                <div class="tag-control">
                                    配件类型折扣</div><div class="tag-padding">
                                    <label class="js-all">全部配件<input type="radio" name="goodsDiscountType"
                                        <#if memberCardInfo.goodDiscountType != 2>
                                             checked
                                        </#if>
                                        class="radio-discount" value="1"></label><div class="form-item">
                                        <input type="text" class="yqx-input yqx-input-small account_input service-all-discount"
                                               data-v-type="required | number | maxValue: 10"
                                            <#if (memberCardInfo.discountType == 3||memberCardInfo.discountType==4) && memberCardInfo.goodDiscountType == 1>
                                                value="${memberCardInfo.goodDiscount}"
                                            <#else>
                                                   disabled
                                            </#if>
                                            name="goodsDiscount">折
                                        </div><label class="js-part">部分配件<input type="radio" name="goodsDiscountType"
                                                    <#if (memberCardInfo.discountType == 3||memberCardInfo.discountType==4) && memberCardInfo.goodDiscountType == 2>
                                                     checked
                                                    </#if>
                                                    class="radio-discount" value="2"></label>
                                    </div>

                                </div>
                                <div class="table-box goods-table <#if !memberCardInfo.cardGoodsRels>hide</#if>">
                                    <table class="yqx-table">
                                        <thead>
                                        <th>配件类型</th>
                                        <th>折扣</th>
                                        <th>操作</th>
                                        </thead>
                                    <#if memberCardInfo.cardGoodsRels>
                                        <#list memberCardInfo.cardGoodsRels as item>
                                            <tr class="goods-datatr">
                                                <td>
                                                    <div class="form-item width-full goods-item">
                                                        <input type="text" name="catName"
                                                               data-v-type="required"
                                                               placeholder="选择配件类型"
                                                               value="${item.catName}"
                                                               class="yqx-input yqx-input-small js-goods-type goods_type_input" >
                                                        <input type="hidden" name="catId" value="${item.catId}">
                                                        <input type="hidden" name="customCat" value="${item.customCat}">
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="form-item">
                                                        <input type="text" class="yqx-input"
                                                               data-v-type="number | required | maxValue:10"
                                                               value="${item.discount}"
                                                               name="discount"><i>折</i>
                                                    </div>
                                                </td>
                                                <td>
                                                    <i class="del js-del">删除</i>
                                                </td>
                                            </tr>
                                        </#list>
                                    </#if>
                                    </table>
                                    <button class="js-add-goods yqx-btn-3 yqx-btn-small">添加配件类型</button>
                                </div>
                            </div>
                    </div>

                </div>
                <div class="info info-expire show-grid">
                    <h2 class="info-head">有效期</h2>

                    <div class="col-6">
                        <div class="form-label form-label-must">
                            有效期
                        </div>
                        <div class="form-item">
                            <input type="text" name="effectivePeriodDays"
                                   class="yqx-input yqx-input-icon im-input" value="${memberCardInfo.effectivePeriodDays}" data-v-type="required | integer:1 | notempty | maxLength:6">
                            <span class="fa">天</span>
                        </div>
                    </div>
                </div>

                <div class="info info-regular" style="display: none;">
                    <h2 class="info-head">使用规则</h2>
                    <div>
                        <input type="checkbox" id="compatibleWithCoupon" checked>
                        <label for="compatibleWithCoupon" style="cursor:pointer;">允许与优惠券共同使用（不选即不允许）</label>
                    </div>
                </div>
                <div class="info info-set">
                    <h2 class="info-head">价值设定</h2>
                    <ul class="show-grid">
                        <li class="col-6">
                            <div class="form-label">
                                会员卡余额<br/>(默认为0)
                            </div>
                            <div class="form-item">
                                <input type="text" name="initBalance" class="yqx-input"
                                       value="${memberCardInfo.initBalance?default(0)}" data-v-type="required | number">
                                <span class="fa">元</span>
                            </div>
                        </li>
                        <li class="col-6">
                            <div class="form-label">
                                售价<br/>(默认为0)
                            </div>
                            <div class="form-item">
                                <input type="text" name="salePrice" class="yqx-input yqx-input-icon" data-v-type="required | number" value="${memberCardInfo.salePrice?default(0)}">
                                <span class="fa">元</span>
                            </div>
                        </li>
                    </ul>
                </div>

                <div class="order-panel-foot clearfix">
                    <button class="yqx-btn yqx-btn-2 js-submit">提交</button>
                    <button class="yqx-btn yqx-btn-1 return js-goback fr">返回</button>
                </div>
                <div class="order-panel-foot clearfix hidden">
                    <button class="yqx-btn yqx-btn-2 js-submit">修改</button>

                </div>
            </div>

        </div>
    </div>
</div>
<!--服务项目-->
<script type="text/html" id="serviceTpl">
    <tr class="service-datatr">
        <!--服务类别-->
        <td>
            <div class="form-item width-full service-item">
                <input type="text"
                       placeholder="输入查询"
                       data-v-type="required"
                       class="yqx-input yqx-input-small js-service-type has-icon">
                <i class="fa icon-small icon-angle-down"></i>
                <input type="hidden" name="catId">
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" class="yqx-input"
                       data-v-type="number | required | maxValue:10"
                       name="discount"><i>折</i>
            </div>
        </td>
        <td>
            <i class="del js-del">删除</i>
        </td>
    </tr>
</script>
<!--配件类型-->
<script type="text/html" id="goodsTpl">
    <tr class="goods-datatr">
        <!--配件类型-->
        <td>
            <div class="form-item width-full goods-item">
                <input type="text" name="catName"
                       data-v-type="required"
                       placeholder="选择配件类型"
                       readonly
                       class="yqx-input yqx-input-small js-goods-type goods_type_input">
                <input type="hidden" name="catId">
                <input type="hidden" name="customCat">
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" class="yqx-input"
                       data-v-type="number | required | maxValue:10"
                       name="discount"><i>折</i>
            </div>
        </td>
        <td>
            <i class="del js-del">删除</i>
        </td>
    </tr>
</script>

<!-- 配件分类模板 -->
<#include "yqx/tpl/common/goods-type-tpl.ftl">

<script type="text/javascript" src="${BASE_PATH}/static/js/page/account/member/create.js?ca101ac5fbfe8fd6f1faeb5c3c39c2f8"></script>
<#include "yqx/layout/footer.ftl">