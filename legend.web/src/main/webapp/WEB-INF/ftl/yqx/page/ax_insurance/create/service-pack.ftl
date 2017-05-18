<#include "yqx/layout/header.ftl">
<link href="${BASE_PATH}/static/css/page/ax_insurance/create/selectScheme.css?0a3e4d6d4460ae3bec0c56fe8c5d7311" type="text/css" rel="stylesheet">
<link href="${BASE_PATH}/static/css/page/ax_insurance/virtual/virtual-service.css?1ecadc72e6551b0bdac1d31892b9918b" type="text/css" rel="stylesheet">
<body>
<div class="wrapper clearfix">
    <div class="content clearfix">
    <#include "yqx/page/ax_insurance/left-nav.ftl">
        <div class="right">
            <ul class="nav_process">
                <li class="nav_pLi">1 输入基本信息</li>
                <li class="nav_pLi">2 填写车辆信息</li>
                <li class="green">3 选择投保方案</li>
                <li>4 确认投保信息</li>
                <li>5 车身拍照</li>
                <li>6 支付保费</li>
                <li>7 投保成功</li>
            </ul>
            <p class="choose">买保险送服务包</p>
            <!--服务包 start-->
            <div class="service-pack">
                <h2 class="pack-title">请选择车主喜欢的服务包<span class="fr">友情提示：目前根据机油型号推荐服务包,机油用量请自行确认</span></h2>
                <!--服务包list start-->
                <div class="pack-list clearfix">
                    <input type="checkbox" class="pack-check fl">
                    <div class="pack-content fl">
                        <div class="consume-title clearfix">
                            <h3 class="fl"><span class="recommend">推荐</span>
                                <span class="pack-name font-yahei">D3000</span>
                                <span class="summary">本服务包适用于女性用户，室内清洁一步到位</span></h3>
                            <p class="fr">市场售价：<span class="money">3000.00</span></p>
                        </div>
                        <table>
                            <thead>
                            <tr>
                                <th class="col1">项目名称</th>
                                <th class="col2">型号</th>
                                <th class="col3">单位</th>
                                <th class="col4">市场价</th>
                                <th class="col5">次数</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>小保养</td>
                                <td>SN 5W-40 4L</td>
                                <td>4L</td>
                                <td>&yen;1000</td>
                                <td>1次</td>
                            </tr>
                            <tr>
                                <td>洗车</td>
                                <td>／</td>
                                <td>／</td>
                                <td>&yen;1000</td>
                                <td>0次</td>
                            </tr>
                            <tr>
                                <td>打蜡</td>
                                <td>／</td>
                                <td>／</td>
                                <td>&yen;1000</td>
                                <td>5次</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <!--服务包list end-->

                <!--支付方式 start-->
                <div class="pay-way-box">
                    <h2 class="pay-title">请选择支付方式</h2>
                    <div class="pay-way-list clearfix">
                        <div class="pay-way">
                            <label>
                                <input type="checkbox">
                                <img src="${BASE_PATH}/static/img/page/ax_insurance/alipay.png" alt="alipay">支付宝
                            </label>
                        </div>
                        <div class="pay-way">
                            <label>
                                <input type="checkbox">
                                <img src="${BASE_PATH}/static/img/page/ax_insurance/wxpay.png" alt="wxpay">微信支付
                            </label>
                        </div>
                        <div class="pay-way">
                            <label>
                                <input type="checkbox">
                                <img src="${BASE_PATH}/static/img/page/ax_insurance/bankpay.png" alt="bankpay">银行支付
                            </label>
                        </div>
                    </div>
                </div>
                <!--支付方式 end-->

                <!--车主须知 start-->
                <div class="owner-notice-box">
                    <h2 class="notice-title">车主须知：</h2>
                    <ol class="notice-intro">
                        <li>1、参加【买服务包送保险】活动，车主需先预付服务包总金额的60%。</li>
                        <li>2、车主预付完服务包总金额的60%后，可以获得一张与服务包总金额等值的商业险抵价劵，可在后续投保时抵保费。</li>
                        <li>3、下次投保时，车主需补交一定的差额后才可使用商业险抵价劵，差额的组成包含服务包总金额的40%和部分保费差价。</li>
                        <li>
                            4、差额计算公式如下：
                            <div class="formula-box">
                                <span class="formula-title">场景1：</span>
                                <div class="formula-info">
                                    商业险保费≥服务包总金额<br/>
                                    应付差额=商业险保费 - 服务包预付金额+其他保费
                                </div>
                            </div>
                            <div class="formula-box">
                                <span class="formula-title">场景2：</span>
                                <div class="formula-info">
                                    商业险保费＜服务包总金额<br/>
                                    应付差额=服务包总金额 - 服务包预付金额+其他保费
                                </div>
                            </div>
                        </li>
                    </ol>
                </div>
                <!--车主须知 end-->

                <!--金额计算 start-->
                <div class="amount-summary-box">
                    <ul class="amount-detail">
                        <li>
                            <span class="amount-item">服务包总金额：<strong class="amount">3000</strong>元</span>
                            <span class="amount-item">后续补付金额：<strong class="amount">1200</strong>元</span>
                        </li>
                        <li>
                            <span class="amount-item">本期预付金额：<strong class="amount">1800</strong>元</span>
                            <span class="amount-item">商业险抵价劵面值：<strong class="amount">3000</strong>元</span>
                        </li>
                    </ul>
                    <p class="amount-payable">应付金额：<strong class="amount">1800.00</strong></p>
                    <button class="yqx-btn yqx-btn-2">去支付</button>
                </div>
                <!--金额计算 end-->

            </div>
            <!--服务包 end-->
        </div>
    </div>
</div>
</body>
<#include "yqx/layout/footer.ftl">