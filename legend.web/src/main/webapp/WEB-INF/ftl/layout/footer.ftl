<div class="qxy_foot">
    <div class="qxy_foot_inner">
        <div class="qxy_foot_col qxy_foot_col1">
            <div>
                <p class="qfc_1_1 qxy_foot_col_font20">淘汽云修<span>商家版</span></p>
                <p class="qfc_1_2">
                    全国性的汽修连锁品牌 <br/>
                    只为更好的服务中国车主
                </p>
            </div>
        </div>
        <div class="qxy_foot_col qxy_foot_col2">
            <div>
                <div class="qxy-foot-hotline-box">
                    <span class="qxy-foot-col-gray fl">客服热线：</span>
                    <div class="qxy-foot-text">
                        <p class="qxy-foot-col-tel">400-9937-288</p>
                        <p>转2转2 云修门店汽车技术维修帮助</p>
                        <p>转2转3 云修门店系统操作帮助</p>
                    </div>
                </div>
                <div>
                    <span class="qxy-foot-col-gray fl">反馈邮箱：</span>
                    <div class="qxy-foot-text">
                        <a class="qxy-foot-col-tel" href="mailto:yunhelp@tqmall.com">yunhelp@tqmall.com</a>
                    </div>
                </div>
            </div>
        </div>
        <div class="qxy_foot_col qxy_foot_col3">
            <div>
                <p class="qfc_3_1">帮助支持</p>
                <p><a href="${BASE_PATH}/portal/help?type=0&id=12">合作店常见问题汇总</a></p>
                <p><a href="${BASE_PATH}/portal/help?type=0&id=51">云修36项检测表</a></p>
                <div class="contact">
                    <p class="contact-qq"><i class="icon-qq"></i><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=3056630970&site=qq&menu=yes">客服1</a></p><p class="contact-qq"><i class="icon-qq"></i><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=3320476090&site=qq&menu=yes">客服2</a></p><p class="contact-qq"><i class="icon-qq"></i><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=3274979296&site=qq&menu=yes">客服3</a></p>
                </div>
            </div>
        </div>
        <div class="qxy_foot_col qxy_foot_col4">
            <div>
                <p class="qfc_4_1">淘汽云修<br />微信公众号</p>
                <p class="qfc_4_2 qxy_foot_col_gray">手机扫一扫关注</p>
            </div>
        </div>
    </div>
</div>

<script id="partsTypeTpl" type="text/html">
    <div class="pop_filtrate_wrap">
        <div class="pop_head">
            <div class="search_input">
                <input type="text" placeholder="搜索类别..."/><button
                    class="search_btn"></button>
            </div>
            <h1 class="head_title">全部类别</h1>
        </div>
        <div class="pop_main">
            <% if( success ) { %>
            <ul class="nav_side">
                <%
                var groupData = group(data);
                var len = groupData.length;
                %>
                <% for (var i = 0; i < len; i++) { %>
                <% var item = groupData[i]; %>
                <li data-ids="<%= item.ids %>" data-map="<%= item.catNameMappings %>"><p><%= item.catNameMappings %></p></li>
                <% } %>
            </ul>
            <% } %>
            <div class="nav_content">
                <div class="nav_content_inner" id="navContentInner"></div>
            </div>
        </div>
    </div>
</script>
<script id="partsTypeContentTpl" type="text/html">
    <% for(var i = 0; i < $data.length; i++) { %>
    <div class="category_box">
        <p class="category"><span class="category_name"></span><i class="c_arrows"></i></p>
        <div class="item_box">
            <% for(var j = 0; j < $data[i].data.length; j++) { %>
            <% var item = $data[i].data[j]; %>
            <p class="item" data-id="<%= item.id %>" data-name="<%= item.catName %>"><%= item.catName %></p>
            <% } %>
        </div>
        <div class="item_btns">
            <button class="more_btn"></button>
        </div>
    </div>
    <% } %>
</script>
<script id="activityPreviewTpl" type="text/html">
    <div class="pop_scroller">
        <div class="act_preview_box">
            <h1 class="act_title"><%= data.actName %></h1>
            <div class="shop_info">
                <p class="shop_name"><%= data.shopName %></p>
                <p class="shop_address"><%= data.address %></p>
            </div>
            <%== data.detailDesc %>
        </div>
    </div>
</script>

<script id="staffBoxTpl" type="text/html">
    <div class="us_box person-accounts">
        <div class="us_t">
            <span>添加员工账号</span>
        </div>
        <div class="staff_info_box1">
            <ul class="us_list us_list1 clearfix">
                <li>
                    <span class="list_title"><font class="red">*</font>帐　　号</span><input class="tz_yg dis-inp" title="${shopName}" value="${shopName}" disabled> - <input
                        id="userAccount" type="text" value=""/>
                </li>
                <li><span class="list_title"><font class="red">*</font>姓　　名</span><input id="userName" type="text"
                                                                                          value=""/>

                </li>
                <li><span class="list_title"><font class="red">*</font>初始密码</span><input id="userPassword" type="password"
                                                                                          value=""/>
                </li>
                <li><span class="list_title"><font class="red">*</font>联系电话</span><input id="userMobile" type="text" value=""/>
                </li>
                <li>
                    <p class="tips">注：密码长度6~12位,密码必须包含数字和字母，字母不区分大小写</p>
                </li>
                <li class="time">
                    <span class="list_title"><font class="red">*</font>登录时间</span>
                    <div class="field_box clockpicker" data-placement="bottom" data-align="top" data-autoclose="true">
                        <input type="text" class="qxy_input time-ico js-time-ico" placeholder="选择开始时间" name="startTime" v_type='{"required":true}' value="00:00:00">
                    </div>
                    <div class="field_box clockpicker" data-placement="bottom" data-align="top" data-autoclose="true">
                        <input type="text" class="qxy_input time-ico js-time-ico" placeholder="选择结束时间" name="endTime" v_type='{"required":true}' value="23:59:59">
                    </div>
                </li>
                <#if isUseWorkShop == true && isShare == true>
                <li>
                    <span class="list_title">班　　组</span>
                    <select class="team-select" name="teamId" id="teamId" >
                        <#list teamVOs as item>
                            <option value="${item.id}" <#if item.id == 0> selected </#if>>${item.name}</option>
                        </#list>
                    </select>
                    <a class="btn_cmn btn_gray mar-l-20"  href="${BASE_PATH}/workshop/team/listpage">管理班组</a>
                </li>
                <li>
                    <span class="list_title">工　　序</span>
                    <select name="processId" class="team-select" id="processId" >
                        <#list processList as item>
                            <option value="${item.id}" <#if item.id ==0> selected </#if> >${item.name}</option>
                        </#list>
                    </select>
                </li>
                <li></li>
                <li>
                    <p class="tips">注：快修组必填，事故组、快喷组员工可不填</p>
                </li>
                <li class="time">
                    <span><input type="checkbox" id="job" class="job" checked="checked">在岗</span>
                </li>
            </#if>
            </ul>
            <div class="role-box">
                <span class="list_title">APP 角色 <font class="red">*</font></span>
                <label><input type="checkbox" name="pvgRoleId" value="2"/>店长</label>
                <label><input type="checkbox" name="pvgRoleId" value="3" />客服</label>
                <label><input type="checkbox" name="pvgRoleId" value="4"/>仓管</label>
                <label><input type="checkbox" name="pvgRoleId" value="5"/>技师</label>
                <label><input type="checkbox" name="pvgRoleId" value="6"/>财务</label>
            </div>
            <div class="dialog-btn">
                <button class="rbut">提 交</button>
                <button class="lbut">取 消</button>
            </div>
        </div>

    </div>
</script>


<script id="memberServiceTpl" type="text/html">
    <input type="hidden" id="member_flag" name="member_flag" value="0"/>
    <div class="mb-10">
        <label>结算折扣：</label><input type="text" name="member_discount" id="member_discount" class="mp_txt mp_unit_txt"
                                   value="<%=data.discount%>" readonly/><span
            class="mp_unit readonly">%</span>
    </div>
    <input type="hidden" id="member_id" name="member_id" value="<%=data.id%>"/>
    <div class="mb-10">
        <label>消费金额：</label><input type="text" data-limit_type="price" id="member_pay_amount" name="member_pay_amount"
                                   class="mp_txt mp_half_unit_txt J_input_limit mp_fee_amonut"/><span
            class="mp_unit">元</span>

        <p class="mp_money-container mr-35">余额：<span class="red" name="member_balance" id="member_balance"><%=data.balance%></span>元
        </p>
    </div>
    <%var temp = data.memberServiceEntryList%>
    <%for(var index in temp){%>
    <%item = temp[index]%>
    <div class="mb-10 entry">
        <input type="hidden" class="member_service_avg" value="<%=item.serviceAvg%>">
        <input type="hidden" class="member_entry_id" value="<%=item.id%>">
        <label><%=item.serviceName%>：</label>
        <button class="mp_service_sub">-</button>
        <input type="text" data-limit_type="number"
               class="mp_txt mp_half_middle_txt J_input_limit member_entry_count"
               data-total_count="<%=item.serviceCount%>" value="<%=item.serviceCount%>" readonly/><span
            class="mp_unit readonly">次</span>

        <p class="mp_money-container mr-35">共：<span class="member_entry_value"><%=item.serviceValue%></span>元</p>
    </div>
    <%}%>
    <p class="mp_card_reckoner-item"><label>会员卡备注：</label><%=data.comment%><span></span></p>
</script>

<script id="chainMemberServiceTpl" type="text/html">
    <input type="hidden" id="member_id" name="member_id" value="<%=data.id%>"/>
    <input type="hidden" id="member_flag" name="member_flag" value="1"/>
    <div class="mb-10">
        <label>消费金额：</label><input type="text" data-limit_type="price" id="member_pay_amount" name="member_pay_amount"
                                   class="mp_txt mp_half_unit_txt J_input_limit mp_fee_amonut"/><span
            class="mp_unit">元</span>

        <p class="mp_money-container mr-35">余额：<span class="red" name="member_balance" id="member_balance"><%=data.amount%></span>元
        </p>
    </div>
    <table class="card-tab">
        <tr>
            <th>优惠券名称</th>
            <th class="nic">使用张数</th>
        </tr>
        <%var temp = data.chainMemberServiceVOList%>
        <%for(var index in temp){%>
        <%item = temp[index]%>
        <input type="hidden" class="member_entry_id" value="<%=item.id%>">
        <input type="hidden" class="member_service_avg" value="0">
        <tr>
            <td class="">
                <span class="ch_used">
                    <p class="bigger-used"><%=item.suiteName%>-<%=item.serviceName%></p>
                    <p>到期日 <%=item.expireTimeStr%></p>
                </span>
            </td>
            <td>
                <div class="choice-number">
                    <span class="reduce cut set-green">-</span>
                    <input class="number member_entry_count" data-total_count="<%=item.serviceCount%>" value="0"
                           readonly type="text">
                    <span class="reduce add set-green">+</span>
                </div>
                <div class="number-box">剩余<span class="number-n"><%=item.serviceCount - item.usedCount%></span>张</div>
            </td>
        </tr>
        <%}%>
    </table>
</script>

<script id="chargeServiceTpl" type="text/html">
    <div class="chg-box">
        <h1 class="chg_tit"><span class="chg_service_name"><%=service%></span>充值</h1>

        <div class="chg_content">
            <p class="chg-row"><label>卡号：</label><input type="text" class="chg_serial txt" value="<%=serial%>"
                                                        readonly/></p>

            <p class="chg-row"><label>余额：</label><#--
                --><input type="text" class="chg_service_count txt chg_half_unit_txt"
                          value="<%=service_count%>" readonly/><span class="txt_unit mr-10">次</span><#--
                --><input type="text" class=" chg_balance txt chg_half_unit_txt"
                          value="<%=balance%>" readonly/><span class="txt_unit">元</span></p>

            <p class="chg-row"><label>充值：</label><#--
                --><input type="text" class="rechg_service_count txt chg_half_unit_txt J_input_limit"
                          data-limit_type="number" value="0"/><span class="txt_unit mr-10">次</span><#--
                --><input type="text" class="rechg_balance txt chg_half_unit_txt J_input_limit"
                          data-limit_type="price" value="0"/><span class="txt_unit">元</span></p>
        </div>
        <div class="chg_bottom">
            <button type="button" class="ok-btn bot-btn chg_ok mr-10">确认</button>
            <button type="button" class="cancel-btn bot-btn chg_cancel">取消</button>
        </div>
    </div>
</script>
<script id="chargeAmountTpl" type="text/html">
    <div class="chg-box">
        <h1 class="chg_tit">充值金额</h1>

        <div class="chg_content">
            <p class="chg-row"><label>卡号：</label><input type="text" class="chg_serial txt" value="<%=serial%>"
                                                        readonly/></p>

            <p class="chg-row"><label>余额：</label><input type="text" class="chg_balance txt chg_unit_txt J_input_limit"
                                                        data-limit_type="price" value="<%=balance%>" readonly/><span
                    class="txt_unit">元</span></p>

            <p class="chg-row"><label>充值：</label><input type="text" class="rechg_balance txt chg_unit_txt J_input_limit"
                                                        data-limit_type="price" value="0"/><span
                    class="txt_unit">元</span></p>
        </div>
        <div class="chg_bottom">
            <button type="button" class="ok-btn bot-btn chg_ok mr-10">确认</button>
            <button type="button" class="cancel-btn bot-btn chg_cancel">取消</button>
        </div>
    </div>
</script>


<script type="text/javascript" src="${BASE_PATH}/resources/js/common/popup.js?3a084ca1d606e27e6547c63f5ba59424"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/common/util.js?2e320316fd90d31740aecb76ba81b8a2"></script>

<script type="text/javascript">
    $(document).ready(function () {
        // 递减
        var cut = $(".choice-number .cut"),
                add = $(".choice-number .add");
        cut.on('click', function () {
            if ($(".choice-number .reduce").hasClass("set-green")) {
                var numberObj = $(this).next(),
                        number = numberObj.text();
                if (number < 2) {
                    return;
                }
                else {
                    numberObj.text(--number);
                }
            }
        });
        //递增
        add.click(function () {
            if ($(".choice-number .reduce").hasClass("set-green")) {
                var numberObj = $(this).prev(),
                        number = numberObj.text(),
                        numberN = $(this).parents("td").find(".number-n").text();
                if (number == numberN) {
                    return;
                }
                else {
                    numberObj.text(++number);
                }
            }
        });
    })

</script>
<script src="${BASE_PATH}/resources/script/libs/tips.js?e250bff627ebeab4eead5769c49f9bb6"></script>

<script>
    var _hmt = _hmt || [];
    (function() {
        var hm = document.createElement("script");
        hm.src = "//hm.baidu.com/hm.js?177aadfd52500674827db74ca8f51989";
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(hm, s);
    })();
</script>
</body>
</html>

