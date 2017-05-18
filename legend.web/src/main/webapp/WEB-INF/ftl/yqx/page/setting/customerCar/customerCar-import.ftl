<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/goods/goods-import.css?8862a2d06344759fb7c3252f1b1874d3"/>

<div class="yqx-wrapper clearfix">
    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="export-contact">
            <img src="${BASE_PATH}/static/img/page/setting/export-ico.png" class="fl"/>
            <div class="fl">
                <p>驰加、博士德等系统</p>
                <p><a href="${BASE_PATH}/shop/help?type=&id=134" class="yqx-link-1">导出指引 > </a></p>
            </div>
        </div>
        <div class="order-head clearfix">
            <h3 class="headline fl">导入数据</h3>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small js-return fr go-back">返回</button>
        </div>
        <div class="content">
            <h3>客户车辆信息可以一键导入，轻松搞定上百条客户车辆信息</h3>
            <div class="step-box">
                <div class="step-title"><i class="radio-box red"></i><span>第一步  准备数据</span></div>
                <p class="step-text">准备好需要导入的客户车辆资料，具体按照模板填写</p>
                <div class="download-box">
                    <a href="${BASE_PATH}/init/import/customerCar/template-download" class="yqx-btn yqx-btn-2">下载模版</a>
                    <div class="matters v-top">
                        <p>注意事项: 1.车牌号必填</p>
                    </div>
                </div>
                <div class="step-title"><i class="radio-box green"></i><span>第二步  导入数据</span></div>
                <p class="prompt">温馨提示:建议您在晚上6点之后进行操作</p>
                <div class="step-form">
                    <div class="form-item">
                        <input type="text" name="" class="yqx-input yqx-input-small js-file-url" disabled value="" placeholder="客户车辆信息文件">
                    </div>
                    <input type="file" class="file" id="fileBtn" name="excelFile" value="">
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small">选择文件</button>
                    <div class="import-box">
                        <button class="yqx-btn yqx-btn-3 yqx-btn-small js-confirm-import" data-url="${BASE_PATH}/init/import/customerCar/import_file" disabled>确认导入</button>
                        <div class="matters">
                            <p>支持多次导入</p>
                            <p>您所导入的所有信息都会经过阿里云云端数据加密处理，任何机构无法访问、查看您所上传数据</p>
                        </div>
                    </div>
                </div>
                <div class="step-title"><i class="radio-box blue"></i><span>第三步  导入结果</span></div>
                <div class="result-box" id="resultBox">

                </div>
            </div>
        </div>
    </div>
</div>
<!--导入提示消息-->
<script type="text/html" id="resultListTpl">
    <%if(json.success && json.data){%>
    <p>成功导入<%=json.data.successNum%>条数据，<br/>
        失败<%=json.data.faildNum%>条数据
        <%if(json.data.faildNum > 0 ){%>
        未成功导入
        <a href="${BASE_PATH}/upload/excel<%=json.data.fileName%>" class="yqx-link-2">客户车辆列表下载 <img src="${BASE_PATH}/static/img/page/setting/dwon-load.png"> </a>
        <%}%>
    </p>
    <div class="member-table">
        <table class="yqx-table">
            <thead>
            <tr>
                <td>车辆类型</td>
                <td>数量（辆）</td>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>无联系方式客户车辆</td>
                <td><%=json.data.notMobileNum%></td>
            </tr>
            <tr>
                <td>有联系方式客户车辆</td>
                <td><%=json.data.hasMobileNum%></td>
            </tr>
            <tr>
                <td class="bold">导入总计</td>
                <td class="bold"><%=json.data.successNum%></td>
            </tr>
            </tbody>
        </table>
    </div>
    <%}%>
</script>

<script src="${BASE_PATH}/static/js/page/setting/goods/goods-import.js?20bd88971378259811dbbfb908e50d53"></script>
<#include "yqx/layout/footer.ftl">
