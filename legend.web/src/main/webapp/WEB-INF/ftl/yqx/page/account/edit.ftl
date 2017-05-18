<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/edit.css?f7fd9674427a6ae633daee1650bcdac7"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">
    <div class="left-menu fl">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    </div>
    <div class="order-right fr">
        <h3 class="Z-title">
            客户管理 > <a href="${BASE_PATH}/account"> 客户查询</a> >  <a href="${BASE_PATH}/account/detail?customerId=${accountInfo.customerId}"> 客户详情</a> > <i>编辑客户资料</i>
        </h3>
        <!--账户详情 start-->
        <div class="content">
            <div class="detail-box cutomer">
                <input type="hidden" name="id" value="${accountInfo.customerId}"/>
                <input type="hidden" name="oldMobile" value="${accountInfo.customer.mobile}"/>
                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label form-label-width">车主：<i class="must"></i></div>
                        <div class="form-item">
                            <input class="yqx-input"
                                   name="customerName"
                                   data-v-type="required"
                                   value="${accountInfo.customer.customerName}"/>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label form-label-width">电话：<i class="must"></i></div>
                        <div class="form-item form-item-width">
                            <input class="yqx-input"
                                   name="mobile"
                                   data-v-type="required | phone"
                                   value="${accountInfo.customer.mobile}"/>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label form-label-width">固定电话：</div>
                        <div class="form-item form-item-width">
                            <input class="yqx-input"
                                   name="tel"
                                   value="${accountInfo.customer.tel}"/>
                        </div>
                    </div>
                </div>

                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label form-label-width">联系人：</div>
                        <div class="form-item form-item-width">
                            <input class="yqx-input"
                                   name="contact"
                                   value="${accountInfo.customer.contact}"/>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label form-label-width">联系人电话：</div>
                        <div class="form-item form-item-width">
                            <input class="yqx-input"
                                   name="contactMobile"
                                   value="${accountInfo.customer.contactMobile}"/>
                        </div>
                    </div>
                </div>

                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label form-label-width">客户生日：</div>
                        <div class="form-item form-item-width">
                            <input class="yqx-input js-date-2" placeholder="请选择时间"
                                   name="birthday"
                                   value="${accountInfo.customer.birthdayStr}"/>
                            <span class="fa icon-calendar"></span>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label form-label-width">身份证号：</div>
                        <div class="form-item form-item-width">
                            <input class="yqx-input"
                                   name="identityCard"
                                   value="${accountInfo.customer.identityCard}"/>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label form-label-width">驾驶证：</div>
                        <div class="form-item form-item-width">
                            <input class="yqx-input js-show-tips"
                                   name="drivingLicense"
                                   value="${accountInfo.customer.drivingLicense}"/>
                        </div>
                    </div>
                </div>

                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label form-label-width">客户来源：</div>
                        <div class="form-item form-item-width">
                            <input class="yqx-input js-show-tips"
                                   name="source"
                                   value="${accountInfo.customer.source}"/>
                        </div>
                    </div>
                    <div class="col-8">
                        <div class="form-label form-label-width">　客户地址：</div>
                        <div class="form-item">
                            <input class="yqx-input"
                                   name="customerAddr"
                                   value="${accountInfo.customer.customerAddr}"
                                   data-v-type="maxLength:50" data-label="客户地址"/>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                        <div class="form-label form-label-width">备注：</div>
                        <div class="form-item form-remark-width">
                            <input class="yqx-input"
                                   name="remark"
                                   value="${accountInfo.customer.remark}"/>
                        </div>
                </div>
                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label form-label-width">客户创建日期：</div>
                        <div class="form-item form-item-width">
                            <input class="yqx-input" value="${accountInfo.customerCreateTime}" disabled/>
                        </div>
                    </div>
                </div>
            </div>

        <div class="card">
        <#list accountInfo.memberCards as card>
            <div class="card-box">
                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label form-label-width">会员卡：</div>
                        <div class="form-item form-item-width">
                            <input type="text" class="yqx-input" disabled value="${card.cardTypeName}"/>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label form-label-width">卡号：<i class="must"></i></div>
                        <div class="form-item form-item-width card-tips-wrap" id="cardNumForm" data-oldnumber="${card.cardNumber}">
                            <input type="text" class="yqx-input js-card-number" name="cardNumber"
                                   value="${card.cardNumber}"/>
                            <input type="hidden" name="id" value="${card.id}"/>
                            <div class="card-tips hide js-card-tips">
                                <p class="title">最近办卡卡号</p>
                                <p class="js-recent-card-num"></p>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label form-label-width">余额：</div>
                        <div class="form-item form-item-width">
                            <input type="text" class="yqx-input" disabled value="${card.balance}"/>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label form-label-width">　　累计充值：</div>
                        <div class="form-item form-item-width">
                            <input type="text" class="yqx-input" disabled
                                   value="${card.depositCount}"
                                    />
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label form-label-width">　　　积分：</div>
                        <div class="form-item form-item-width">
                            <input type="text" class="yqx-input"
                                   disabled
                                   value="${card.expenseAmount}"
                                    />
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label form-label-width">办卡日期：</div>
                        <div class="form-item form-item-width">
                            <input type="text" class="yqx-input"
                                   disabled
                                   value="${card.gmtCreateStr}"
                                    />
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label form-label-width">发卡人：</div>
                        <div class="form-item form-item-width">
                            <input type="text" class="yqx-input"
                                   disabled
                                   value="${card.publisherName}"
                                    />
                        </div>
                    </div>
                </div>
            <#--<div class="credit-line">-->
            <#--<button class="js-show-record credit-record-btn">积分变更记录</button>-->
            <#--</div>-->
            </div>
        </#list>
        </div>
            <div class="btns-group clearfix">
                <button class="yqx-btn yqx-btn-2 fl js-save">保存</button>
            </div>
        </div>
        <!--账户详情 end-->



    </div>
</div>

<div class="dialog credit-record hide">
    <div class="dialog-title">
        积分变更记录
    </div>
    <div class="dialog-content">
        <table>
            <thead>
            <tr>
                <th>日期</th>
                <th>类型</th>
                <th>积分变更</th>
                <th>剩余积分</th>
                <th>关联工单</th>
            </tr>
            </thead>
            <tbody id="showRecordFill">

            </tbody>
        </table>
        <div class="yqx-page" id="showRecordPage"></div>
    </div>
</div>


<script type="text/template" id="showRecordTpl">
    <%if(json && json.data){%>
    <%var data = json.data;%>
    <%for(var i in data) {%>
    <%var e = data[i];%>
    <tr>
        <td><%=e.%></td>
        <td><%=e.%></td>
        <td><%=e.%></td>
        <td><%=e.%></td>
        <td><%=e.%></td>
    </tr>
    <%}%>
    <%}%>
</script>

<script type="text/javascript" src="${BASE_PATH}/static/js/page/account/edit.js?f1db42dc334d92ac97bde4d63b49a2dd"></script>
<#include "yqx/layout/footer.ftl">