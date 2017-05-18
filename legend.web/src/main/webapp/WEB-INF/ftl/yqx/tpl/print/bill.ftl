<#--报价单-->
<section class="print-box bill-box" data-print="bill">
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
        <h2 class="text" id="编号">（报价单） NO：<i class="text"></i></h2>
    </div><div class="contact-box">
        <h3 id="地址" ><i>地址：</i><i class="address text">${shop.address}</i></h3>
        <h3 id="电话">电话：<i class="text"></i>${shop.tel}</h3>
    </div>
    </header>
    <article>
        <ul class="box order-info">
            <li id="开单时间" class="time"><i class="key">开单时间：</i><i class="text"></i></li>
            <li id="服务顾问" class="server"><i class="key">服务顾问：</i><i class="text"></i></li>
        </ul>
        <ul class="box customer-box">
            <li class="text customer" id="车主">
                <i class="text"></i>
            </li><li class="text relative-customer" id="联系人">
                <i class="text"></i>
            </li><li id="单位" class="fr customer-company">
                <i class="key">客户单位：</i><i class="text"></i>
            </li><li id="车牌">
                <i class="key">车牌：</i><i class="text"></i>
            </li><li id="车辆型号">
                <i class="key">车辆型号：</i><i class="text"></i>
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
                <td>更换机油更换机油更换机油更换机油<i class="note"></i></td>
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
                <td>淘汽云修 100臻 全合成车用润滑油 SN 0W-40 4L 1瓶装 6瓶/箱<i class="note"></i></td>
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
                <td>两前轮动平衡<i class="note"></i></td>
                <td>80</td>
                <td>0</td>
            </tr>
        </table>
        <ul class="box total-box">
            <li>合计</li>
            <li id="服务总费用">服务总费用：<i class="text">120</i></li>
            <li id="配件总费用">配件总费用：<i class="text">100</i></li>
            <li id="附加总费用">附加总费用：<i class="text">80</i></li>
            <li id="总优惠">总优惠：<i class="text">0</i></li>
            <li class="border-left">
                <ul>
                    <li id="应收金额">总计：<i class="text">300</i></li>
                    <li id="应收金额大写"><i class="text">叁佰元</i></li>
                </ul>
            </li>
        </ul>
        <ul class="box footer-box">
            <li>
                <i class="five">打印时间：</i><i class="print-time text"></i>
                <i class="four">客户签字</i><i class="sign-text"></i><i class="space year-space"></i><i>年</i><i class="space"></i><i>月</i><i class="space"></i><i>日</i>
            </li>
        </ul>
    </article>
    <footer>
    </footer>
</section>