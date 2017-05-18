<#include "layout/portal-header.ftl">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/join/product_center_detail.css?20150318"/>
<div class="wrapper">
    <p class="pathTree"><a href="${BASE_PATH}/portal">首页</a>　&gt;　<a href="${BASE_PATH}/portal/product">产品</a>　&gt;　<span id="product_title">直营店版</span></p>
    <ul class="storeNav">
        <li class="item current" id="direct" for_table="table1">专业版</li>
        <li class="item" id="join" for_table="table2">基础版</li>
        <#--<li class="item" id="app" for_table="table3">APP版</li>-->
        <input type="hidden" id="productType" value="${productType}">
    </ul>
    <div>
    <div id="table1">
        <div class="storeBox">
            <div class="storeInfo">
                <h1 class="storeHead">六大功能</h1>

                <div class="storeSummary">针对淘汽云修专业版本，共有六大功能：前台接待、维修派工、透明车间、配件仓库、淘汽平台、统计报表。</div>
            </div>
            <div class="storeImg">
                <img class="storeImg" src="${BASE_PATH}/resources/images/join/pro.jpg">
            </div>
        </div>
        <div class="storeContent">
            <ul class="functionalDetail">
                <li>
                    <div class="subHead">
                        <span class="seq">
                            <strong class="seq0">0</strong><strong class="seq1">1</strong>
                        </span>

                        <div class="subtitle">
                            <p class="subHeadCN">前台接待</p>

                            <p class="subHeadENG">Reception</p>
                        </div>
                    </div>
                    <ul class="fd_content">
                        <li>移动接待</li>
                        <li>历史查询</li>
                        <li>客户管理</li>
                        <li>预约服务</li>
                        <li>客户回访</li>
                        <li>会员营销</li>
                    </ul>
                </li>
                <li>
                    <div class="subHead">
                        <span class="seq">
                            <strong class="seq0">0</strong><strong class="seq1">2</strong>
                        </span>

                        <div class="subtitle">
                            <p class="subHeadCN">维修派工</p>

                            <p class="subHeadENG">Service</p>
                        </div>
                    </div>
                    <ul class="fd_content">
                        <li>标准服务菜谱式开单</li>
                        <li>汽配报价车间专业化</li>
                        <li>保险维修业务</li>
                        <li>工单派工</li>
                        <li>完工验工</li>
                        <li>挂账结算</li>
                    </ul>
                </li>
                <li>
                    <div class="subHead">
                        <span class="seq">
                            <strong class="seq0">0</strong><strong class="seq1">3</strong>
                        </span>

                        <div class="subtitle">
                            <p class="subHeadCN">透明车间</p>

                            <p class="subHeadENG">Transparent Workshop</p>
                        </div>
                    </div>
                    <ul class="fd_content">
                        <li>车辆维修进度</li>
                        <li>车间调度</li>
                        <li>工位管理</li>
                        <li>维修全程监控</li>
                        <li>完工验测</li>
                        <li>维修报告</li>
                    </ul>
                </li>
                <li>
                    <div class="subHead">
                        <span class="seq">
                            <strong class="seq0">0</strong><strong class="seq1">4</strong>
                        </span>

                        <div class="subtitle">
                            <p class="subHeadCN">配件仓库</p>

                            <p class="subHeadENG">Parts Warehouse</p>
                        </div>
                    </div>
                    <ul class="fd_content">
                        <li>配件报价</li>
                        <li>工单领料</li>
                        <li>库存查询</li>
                        <li>配件资料</li>
                        <li>采购入库</li>
                        <li>缺货预警</li>
                    </ul>
                </li>
                <li>
                    <div class="subHead">
                        <span class="seq">
                            <strong class="seq0">0</strong><strong class="seq1">5</strong>
                        </span>

                        <div class="subtitle">
                            <p class="subHeadCN">淘汽平台</p>

                            <p class="subHeadENG">Tqmall Platform</p>
                        </div>
                    </div>
                    <ul class="fd_content">
                        <li>门店在售服务</li>
                        <li>客户在线订单</li>
                        <li>淘汽配件采购</li>
                        <li>淘汽库存直联</li>
                        <li>淘汽活动</li>
                        <li>淘汽结算</li>
                    </ul>
                </li>
                <li>
                    <div class="subHead">
                        <span class="seq">
                            <strong class="seq0">0</strong><strong class="seq1">6</strong>
                        </span>

                        <div class="subtitle">
                            <p class="subHeadCN">统计报表</p>

                            <p class="subHeadENG">Statistical Reports</p>
                        </div>
                    </div>
                    <ul class="fd_content">
                        <li>门店营业报表</li>
                        <li>客户流量分析</li>
                        <li>维修服务统计</li>
                        <li>配件出入明细</li>
                        <li>员工业绩统计</li>
                        <li>门店利润报表</li>
                    </ul>
                </li>
            </ul>
        </div>
        <div class="clear"></div>
    </div>
    <div id="table2" style="display: none">
        <div class="storeBox">
            <div class="storeInfo">
                <h1 class="storeHead">六大功能</h1>

                <div class="storeSummary">针对淘汽云修基础版本，共有六大功能：客户管理、工单管理、车间管理、仓库管理、淘汽平台、统计报表。</div>
            </div>
            <div class="storeImg">
                <img class="storeImg" src="${BASE_PATH}/resources/images/join/advanced_detail.png">
            </div>
        </div>
        <div class="storeContent">
            <ul class="functionalDetail">
                <li>
                    <div class="subHead">
                        <span class="seq">
                            <strong class="seq0">0</strong><strong class="seq1">1</strong>
                        </span>

                        <div class="subtitle">
                            <p class="subHeadCN">客户管理</p>

                            <p class="subHeadENG">Customer Management</p>
                        </div>
                    </div>
                    <ul class="fd_content">
                        <li>移动接待</li>
                        <li>历史查询</li>
                        <li>客户管理</li>
                        <li>预约服务</li>
                        <li>客户回访</li>
                        <li>会员营销</li>
                    </ul>
                </li>
                <li>
                    <div class="subHead">
                        <span class="seq">
                            <strong class="seq0">0</strong><strong class="seq1">2</strong>
                        </span>

                        <div class="subtitle">
                            <p class="subHeadCN">工单管理</p>

                            <p class="subHeadENG">Work Order Management</p>
                        </div>
                    </div>
                    <ul class="fd_content">
                        <li>菜谱式标准服务</li>
                        <li>维修开单打印</li>
                        <#--<li>保险维修业务</li>-->
                        <li>工单派工</li>
                        <li>完工验工</li>
                        <li>挂账结算</li>
                    </ul>
                </li>
                <li>
                    <div class="subHead">
                        <span class="seq">
                            <strong class="seq0">0</strong><strong class="seq1">3</strong>
                        </span>

                        <div class="subtitle">
                            <p class="subHeadCN">车间管理</p>

                            <p class="subHeadENG">Shop Management</p>
                        </div>
                    </div>
                    <ul class="fd_content">
                        <li>车辆维修进度</li>
                        <li>车间调度</li>
                        <#--<li>工位管理</li>-->
                        <#--<li>维修全程监控</li>-->
                        <li>完工验测</li>
                        <#--<li>维修报告</li>-->
                    </ul>
                </li>
                <li>
                    <div class="subHead">
                        <span class="seq">
                            <strong class="seq0">0</strong><strong class="seq1">4</strong>
                        </span>

                        <div class="subtitle">
                            <p class="subHeadCN">仓库管理</p>

                            <p class="subHeadENG">Warehouse Management</p>
                        </div>
                    </div>
                    <ul class="fd_content">
                        <#--<li>配件报价</li>-->
                        <li>工单领料</li>
                        <li>库存查询</li>
                        <li>配件资料</li>
                        <li>采购入库</li>
                        <li>缺货预警</li>
                    </ul>
                </li>
                <li>
                    <div class="subHead">
                        <span class="seq">
                            <strong class="seq0">0</strong><strong class="seq1">5</strong>
                        </span>

                        <div class="subtitle">
                            <p class="subHeadCN">淘汽平台</p>

                            <p class="subHeadENG">Tqmall Platform</p>
                        </div>
                    </div>
                    <ul class="fd_content">
                        <li>门店在售服务</li>
                        <li>客户在线订单</li>
                        <li>淘汽配件采购</li>
                        <li>淘汽库存直联</li>
                        <li>淘汽活动</li>
                        <li>淘汽结算</li>
                    </ul>
                </li>
                <li>
                    <div class="subHead">
                        <span class="seq">
                            <strong class="seq0">0</strong><strong class="seq1">6</strong>
                        </span>

                        <div class="subtitle">
                            <p class="subHeadCN">统计报表</p>

                            <p class="subHeadENG">Statistical Reports</p>
                        </div>
                    </div>
                    <ul class="fd_content">
                        <li>门店营业报表</li>
                        <li>客户流量分析</li>
                        <li>维修服务统计</li>
                        <li>配件出入明细</li>
                        <li>员工业绩统计</li>
                        <li>门店利润报表</li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
    <div id="table3" style="display: none">
        <div class="storeBox">
            <div class="storeInfo">
                <h1 class="storeHead">五大功能</h1>

                <div class="storeSummary">针对淘汽云修APP版本，分IOS与Android两个版本，共有五大功能：协同、效率、云修、服务、我的。</div>
            </div>
            <div class="storeImg">
                <img class="storeImg" src="${BASE_PATH}/resources/images/join/app_detail.png">
            </div>
        </div>
        <div class="storeContent">
            <ul class="functionalDetail">
                <li>
                    <div class="subHead">
                        <span class="seq">
                            <strong class="seq0">0</strong><strong class="seq1">1</strong>
                        </span>

                        <div class="subtitle">
                            <p class="subHeadCN">协同</p>

                            <p class="subHeadENG">Joint</p>
                        </div>
                    </div>
                    <ul class="fd_content">
                        <li>移动接待</li>
                        <li>历史查询</li>
                        <li>客户管理</li>
                        <li>预约服务</li>
                        <li>客户回访</li>
                        <li>会员营销</li>
                    </ul>
                </li>
                <li>
                    <div class="subHead">
                        <span class="seq">
                            <strong class="seq0">0</strong><strong class="seq1">2</strong>
                        </span>

                        <div class="subtitle">
                            <p class="subHeadCN">效率</p>

                            <p class="subHeadENG">Efficiency</p>
                        </div>
                    </div>
                    <ul class="fd_content">
                        <li>标准服务菜谱式开单</li>
                        <li>汽配报价车间专业化</li>
                        <li>保险维修业务</li>
                        <li>工单派工</li>
                        <li>完工验工</li>
                        <li>挂账结算</li>
                    </ul>
                </li>
                <li>
                    <div class="subHead">
                        <span class="seq">
                            <strong class="seq0">0</strong><strong class="seq1">3</strong>
                        </span>

                        <div class="subtitle">
                            <p class="subHeadCN">云修</p>

                            <p class="subHeadENG">Cloud Repair</p>
                        </div>
                    </div>
                    <ul class="fd_content">
                        <li>车辆维修进度</li>
                        <li>车间调度</li>
                        <li>工位管理</li>
                        <li>维修全程跟踪</li>
                        <li>完工验测</li>
                        <li>维修报告</li>
                    </ul>
                </li>
                <li>
                    <div class="subHead">
                        <span class="seq">
                            <strong class="seq0">0</strong><strong class="seq1">4</strong>
                        </span>

                        <div class="subtitle">
                            <p class="subHeadCN">服务</p>

                            <p class="subHeadENG">Service</p>
                        </div>
                    </div>
                    <ul class="fd_content">
                        <li>配件报价</li>
                        <li>工单领料</li>
                        <li>库存查询</li>
                        <li>配件资料</li>
                        <li>采购入库</li>
                        <li>出入库报表</li>
                    </ul>
                </li>
                <li>
                    <div class="subHead">
                        <span class="seq">
                            <strong class="seq0">0</strong><strong class="seq1">5</strong>
                        </span>

                        <div class="subtitle">
                            <p class="subHeadCN">我的</p>

                            <p class="subHeadENG">My</p>
                        </div>
                    </div>
                    <ul class="fd_content">
                        <li>门店在售服务</li>
                        <li>客户在线订单</li>
                        <li>淘汽配件采购</li>
                        <li>淘汽库存直联</li>
                        <li>淘汽活动</li>
                        <li>淘汽结算</li>
                    </ul>
                </li>

            </ul>
        </div>
        <div class="clear"></div>
    </div>
    </div>
</div>
<#include "layout/portal-footer.ftl">
<script type="text/javascript" src="${BASE_PATH}/resources/js/portal/product_info.js?20150315"></script>