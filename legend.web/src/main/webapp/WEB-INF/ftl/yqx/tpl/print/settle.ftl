<#--结算单-->
<section class="print-box settle-box" data-print="settle">
    <header>
<#if printPostion == 1>
    <div class="title-center">
        <#if printTitle != null >
            <img src="${printTitle}" alt="${shop.companyName}" width="445px" height="29px">
        <#else>
            <h1 class="text" id="维修厂名称"><i class="text">${shop.companyName}</i></h1>
        </#if>
        <h2 class="text" id="编号">（结算单） NO：<i class="text">${orderInfo.orderSn}</i></h2>
    </div>
<#else>
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
        <h2 class="text" id="编号">（结算单） NO：<i class="text"></i></h2>
    </div><div class="contact-box">
        <h3 id="地址" ><i>地址：</i><i class="address text">${shop.address}</i></h3>
        <h3 id="电话">电话：<i class="text">${shop.tel}</i></h3>
    </div>
</#if>
    </header>
    <article>
        <ul class="box order-info">
            <li id="开单时间" class="time"><i class="key">开单时间：</i><i class="text"></i></li>
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
                </li><li id="下次保养里程">
                    <i class="key">下次保养里程：</i><i class="text"></i>
                </li><li id="车辆型号">
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
            <li class="checkbox-box border-left">
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
        </ul>
        <table class="box service-box normal-table" id="服务项目">
            <thead>
            <tr>
                <th>序号</th>
                <th class="name">服务项目</th>
                <th class="number per-hour-price">工时费</th>
                <th class="unit">工时</th>
                <th class="price">金额</th>
                <th class="number">优惠</th>
            </tr>
            </thead>
            <tr class="service-tr">
                <td>1</td>
                <td>更换机油更换机油更换机油更换机油<i class="note">(备注)</i></td>
                <td>120</td>
                <td>1</td>
                <td>120</td>
                <td>0</td>
            </tr>
            <tr class="service-tr">
                <td>2</td>
                <td>更换机油更换机油更换机油更换机油<i class="note">(备注)</i></td>
                <td>120</td>
                <td>1</td>
                <td>120</td>
                <td>0</td>
            </tr>
            <tr class="service-tr">
                <td>3</td>
                <td>更换机油更换机油更换机油更换机油<i class="note">(备注)</i></td>
                <td>120</td>
                <td>1</td>
                <td>120</td>
                <td>0</td>
            </tr>
        </table>
        <table class="box goods-box normal-table" id="配件物料">
            <thead>
            <tr>
                <th>序号</th>
                <th class="name">配件物料</th>
                <th class="number per-hour-price">售价</th>
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
        <table class="box addon-box normal-table" id="附加项目">
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
        <table class="box merge-table hide addon-box">
            <thead>
            <tr>
                <th>序号</th>
                <th class="name">名称</th>
                <th class="measure">数量</th>
                <th class="number">金额</th>
                <th class="number">优惠</th>
            </tr>
            </thead>
            <tbody>
            <tr class="service-tr">
                <td>1</td>
                <td>更换机油更换机油更换机油更换机油<i class="note">(备注)</i></td>
                <td class="measure">1 工时</td>
                <td>120</td>
                <td>0</td>
            </tr>
            <tr class="service-tr">
                <td>2</td>
                <td>更换机油更换机油更换机油更换机油<i class="note">(备注)</i></td>
                <td class="measure">1 工时</td>
                <td>120</td>
                <td>0</td>
            </tr>
            <tr class="service-tr">
                <td>3</td>
                <td>更换机油更换机油更换机油更换机油<i class="note">(备注)</i></td>
                <td class="measure">1 工时</td>
                <td>120</td>
                <td>0</td>
            </tr>
            <tr class="goods-tr">
                <td>4</td>
                <td>淘汽云修 100臻 全合成车用润滑油 SN 0W-40 4L 1瓶装 6瓶/箱<i class="note">(备注)</i></td>
                <td class="measure">1 6瓶/箱</td>
                <td>100</td>
                <td>0</td>
            </tr>
            <tr class="goods-tr">
                <td>5</td>
                <td>淘汽云修 100臻 全合成车用润滑油 SN 0W-40 4L 1瓶装 6瓶/箱<i class="note">(备注)</i></td>
                <td class="measure">1 6瓶/箱</td>
                <td>100</td>
                <td>0</td>
            </tr>
            <tr class="addon-tr">
                <td>5</td>
                <td>两前轮动平衡<i class="note">(备注)</i></td>
                <td class="measure"></td>
                <td>80</td>
                <td>0</td>
            </tr>

            </tbody>
        </table>
        <ul class="box total-box">
            <li id="合计">合计</li>
            <li id="总费用" class="hide merge-fee">总费用：<i class="text">640 元</i></li>
            <li id="服务总费用" class="normal-fee">服务总费用：<i class="text">360</i></li>
            <li id="配件总费用" class="normal-fee">配件总费用：<i class="text">200</i></li>
            <li id="附加总费用" class="normal-fee">附加总费用：<i class="text">80</i></li>
            <li id="总优惠">总优惠：<i class="text">0</i></li>
            <li class="border-left">
                <ul>
                    <li id="应收金额">应收金额：<i class="text">640</i></li>
                    <li id="应收金额大写"><i class="text">陆佰肆拾元</i></li>
                </ul>
            </li>
        </ul>
        <ul class="box cash-info">
            <li>
                挂账金额：100
            </li>
            <li class="center">
                实收金额：540
            </li>
        </ul>
        <ul class="box cash-info" id="showAccount">
            <li>会员卡余额：20元
            </li>
            <li class="center">
                普通洗车剩余3次，保养剩余10次
            </li>
        </ul>
        <ul class="box footer-box clearfix">
            <li class="fl" id="打印时间">
                <i class="five">打印时间：</i><i class="print-time text"></i>
            </li>
            <li class="fr">
                <i class="four">客户签字</i><i class="sign-text text"></i><i class="space year-space"></i><i>年</i><i class="space"></i><i>月</i><i class="space"></i><i>日</i>
            </li>
        </ul>
        <ul class="box footer-box mark-box">
            <li>备注：这是备注，无备注时不显示</li>
        </ul>
    </article>
    <footer>
<#if printPostion == 1>
    <div class="footer-address">
        <h3 id="地址"><i>地址：</i><i class="address1 text">${shop.address}</i></h3>
        <h3 id="电话">电话：<i class="text">${shop.tel}</i></h3>
    </div>
</#if>
        <ol id="提示语">
            <li>
                1.该单项目及金额经双方核实，客户签字生效
            </li>
            <li>
                2.客户自带配件到我厂维修的项目本厂对此不作保修
            </li>
            <li>
                3.挂账单位司机签名后将结算单带回单位主管部门，对结算信息如有异议须于三天内提出，逾期无异议则按结算单信息结算，并承诺自签字后30天内付清费用
            </li>
        </ol>
    </footer>
</section>