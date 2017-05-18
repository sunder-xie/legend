<#--派工单-->
<section class="print-box dispatch-box" data-print="dispatch">
    <header>
    <#if printLogo != null>
        <img class="print-logo" src="${printLogo}">
    <#elseif SESSION_SHOP_LEVEL != 10 && SESSION_SHOP_LEVEL != 11 && SESSION_SHOP_LEVEL != 12 && printTitle == null>
        <img class="print-logo" src="${BASE_PATH}/static/img/common/print/print-logo.png">
    </#if>
        <div class="title-box">
        <#if printTitle != null >
            <img src="${printTitle}" alt="${shop.companyName}" width="445px" height="29px">
        <#else>
            <h1 class="text" id="维修厂名称"><i class="text">${shop.companyName}</i></h1>
        </#if>
            <h2 class="text" id="编号">（派工单） NO：<i class="text"></i></h2>
        </div><div class="contact-box">
            <h3 id="地址" ><i>地址：</i><i class="address text">${shop.address}</i></h3>
            <h3 id="电话">电话：<i class="text">${shop.tel}</i></h3>
        </div>
    </header>
    <article>
        <ul class="box order-info">
            <li id="开单时间" class="time"><i class="key">开单时间：</i><i class="text"></i></li>
            <li id="预完工时间" class="time"><i class="key">预完工时间：</i><i class="text"></i></li>
            <li id="服务顾问" class="fr server"><i class="key">服务顾问：</i><i class="text"></i></li>
        </ul>
        <ul class="box customer-box">
            <li class="text customer" id="车主"><i class="text"></i></li>
            <li class="text relative-customer" id="联系人"><i class="text"></i></li>
            <li class="customer-company" id="单位"><i class="key">客户单位：</i><i class="text"></i></li>
        </ul>
        <div class="car-info box">
            <ul>
                <li id="车牌">
                    <i class="key">车牌：</i><i class="text"></i>
                </li><li id="车辆颜色">
                    <i class="key">车辆颜色：</i><i class="text"></i>
                </li><li id="行驶里程">
                    <i class="key">行驶里程数：</i><i class="text"></i>
                </li><li id="承保公司">
                    <i class="key">承保公司：</i><i class="text"></i>
                </li><li id="发动机号">
                    <i class="key">发动机号：</i><i class="text"></i>
                </li><li id="VIN码">
                    <i class="key">VIN码：</i><i class="text"></i>
                </li><li id="油表油量">
                    <i class="key">油表油量：</i><i class="text"></i>
                </li>   <li id="车辆型号">
                    <i class="key">车辆型号：</i><i class="text"></i>
                </li>
            </ul>
        </div>
        <ul class="box split-vertical">
            <li>
                <ul>
                    <li id="维修类别">
                        <i>维修类别：</i><i class="text"></i>
                    </li>
                    <li id="进场方式">
                        <i>进场方式：</i><i class="text"></i>
                    </li>
                </ul>
            </li>
            <li class="checkbox-box">
                <ul>
                    <li id="洗车">
                        <div class="checkbox"></div><i class="text">
                            洗车
                        </i>
                    </li>
                    <li id="旧件带回">
                        <div class="checkbox"></div><i class="text">
                        旧件带回
                    </i>
                    </li>
                </ul>
            </li>
            <li>
                <ul>
                    <li id="客户描述">
                        客户描述：<i class="text"></i>
                    </li>
                </ul>
            </li>
        </ul>
        <table class="box service-box" id="服务项目">
            <thead>
                <tr>
                    <th>序号</th>
                    <th class="name">服务项目</th>
                    <th class="number per-hour-price">工时费</th>
                    <th class="unit">工时</th>
                    <th class="price">金额</th>
                    <th class="number">优惠</th>
                    <th class="people">指派人员</th>
                    <th style="width:9mm;">复检</th>
                </tr>
            </thead>
            <tr class="service-tr">
                <td>1</td>
                <td>更换机油更换机油更换机油更换机油<i class="note">(备注)</i></td>
                <td>120</td>
                <td>1</td>
                <td>120</td>
                <td>0</td>
                <td></td>
                <td></td>
            </tr>
            <tr class="service-tr">
                <td>2</td>
                <td>更换机油更换机油更换机油更换机油<i class="note">(备注)</i></td>
                <td>120</td>
                <td>1</td>
                <td>120</td>
                <td>0</td>
                <td></td>
                <td></td>
            </tr>
        </table>
        <table class="box goods-box" id="配件物料">
            <thead>
                <tr>
                    <th>序号</th>
                    <th class="name">配件物料</th>
                    <th class="number">售价</th>
                    <th class="unit">数量</th>
                    <th class="price">金额</th>
                    <th class="number">优惠</th>
                </tr>
            </thead>
            <tr class="goods-tr">
                <td>1</td>
                <td>淘汽云修 100臻 全合成车用润滑油 SN 0W-40 4L 1瓶装 6瓶/箱<i class="note">(备注)</i></td>
                <td>100</td>
                <td>1</td>
                <td>100</td>
                <td>0</td>
            </tr>
            <tr class="goods-tr">
                <td>2</td>
                <td>淘汽云修 100臻 全合成车用润滑油 SN 0W-40 4L 1瓶装 6瓶/箱<i class="note">(备注)</i></td>
                <td>100</td>
                <td>1</td>
                <td>100</td>
                <td>0</td>
            </tr>
        </table>
        <table class="box addon-box" id="附加项目">
            <thead>
                <tr>
                    <th>序号</th>
                    <th class="name">附加项目</th>
                    <th class="price">金额</th>
                    <th class="number">优惠</th>
                </tr>
            </thead>
            <tr class="addon-tr">
                <td>1</td>
                <td>两前轮动平衡<i class="note">(备注)</i></td>
                <td>80</td>
                <td>0</td>
            </tr>
        </table>
        <ul class="box total-box">
            <li>合计</li>
            <li id="服务总费用">服务总费用：<i class="text">240</i></li>
            <li id="配件总费用">配件总费用：<i class="text">200</i></li>
            <li id="附加总费用">附加总费用：<i class="text">80</i></li>
            <li id="总优惠">总优惠：<i class="text">0</i></li>
            <li class="border-left">
                <ul>
                    <li id="应收金额">应收金额：<i class="text">520</i></li>
                    <li id="应收金额大写"><i class="text">伍佰贰拾元</i></li>
                </ul>
            </li>
        </ul>
        <ul class="box service-note-box">
            <li id="fixRecord">
                <i>维修项目修改记录：</i><i class="text">
                </i>
            </li>
            <li id="维修建议">
                <i>维修建议：</i><i class="text">
                </i>
            </li>
        </ul>
        <ul class="box check-box" id="检验签字">
            <li>
                检验签字
            </li>
            <li>
                机电
            </li>
            <li>
                钣喷
            </li>
            <li>
                美容
            </li>
            <li>
                总检
            </li>
        </ul>
        <ul class="box footer-box">
            <li>
                <i class="five">打印时间：</i><i class="print-time text"></i>
                <i class="four">客户签字</i><i class="sign-text"></i><i class="space year-space"></i><i>年</i><i class="space"></i><i>月</i><i class="space"></i><i>日</i>
            </li>
        </ul>
        <ul class="box footer-box mark-box">
            <li>备注：这是备注，无备注时不显示</li>
        </ul>
    </article>
    <footer>
        <ol id="提示语">
            <li>
                1.此单一式三联，客户白联用户提车，红联用户系统开单，黄联用户车辆检修。
            </li>
            <li>
                2.友情提示：请您将车内贵重物品及现金带走，如有遗失本公司盖不承担责任。
            </li>
            <li>
                3.维修项目以工单为准。
            </li>
        </ol>
    </footer>
</section>