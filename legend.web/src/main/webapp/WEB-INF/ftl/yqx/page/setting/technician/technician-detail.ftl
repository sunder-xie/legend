<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/technician/technician-detail.css?7592e208a02aab4c1b01ae3aa23ffbd2"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/style/modules/chosen.css?30ab5c0b469dabe167389465b133233f"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <h3 class="headline">个人信息<small>－技师认证</small></h3>
        </div>
        <div class="content" id="formData">
            <div class="show-grid">
                <div class="form-label form-label-must">
                    姓名:
                </div>
                <div class="form-item">
                    <input type="text" name="name" id="name" class="yqx-input yqx-input-small" value="${shopManager.name}" placeholder="" data-v-type="required" disabled>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    性别:
                </div>
                <div class="form-item">
                   <input type="radio" name="gender" <#if shopManager.gender != 0>checked</#if> value="1" /> 男
                   <input type="radio" name="gender" <#if shopManager.gender == 0>checked</#if> value="0"/> 女
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    联系电话:
                </div>
                <div class="form-item w-140">
                    <input type="text" name="mobile" id="mobile" class="yqx-input yqx-input-small" value="${shopManager.mobile}" placeholder="" data-v-type="required" disabled>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    身份证号:
                </div>
                <div class="form-item w-210">
                    <input type="text" name="identityCard" id="identityCard" class="yqx-input yqx-input-small" value="${shopManager.identityCard}" placeholder="" data-v-type="required | idCard">
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    学历:
                </div>
                <div class="form-item">
                    <input type="text" id="education" class="yqx-input yqx-input-icon yqx-input-small js-education" value="" placeholder=""  data-v-type="required">
                    <input type="hidden" name="education">
                    <span class="fa icon-angle-down icon-small"></span>
                    <div class="yqx-select-options" style="width: 140px; display: none;">
                        <dl>
                        <#list educationEnum as education>
                            <dd class="yqx-select-option" data-key="${education.code}" <#if shopManager.education==education.code>selected="selected"</#if> > ${education.message}</dd>
                        </#list>
                        </dl>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label">
                    毕业学校:
                </div>
                <div class="form-item w-210">
                    <input type="text" name="graduateSchool" id="graduateSchool" class="yqx-input yqx-input-small" value="${shopManager.graduateSchool}" placeholder="">
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label">
                    擅长修理:
                </div>
                <div class="repair-item">
                    <select id="allBrand" data-placeholder="请选择品牌" style="height: 100px">
                    </select>
                    <script id="allBrandTpl" type="text/html">
                        <optgroup label="">
                            <option value="">请选择品牌</option>
                        </optgroup>
                        <%for(var cascadeSelectItem in templateData){%>
                        <optgroup label="<%= cascadeSelectItem%>">
                            <%for(var i=0;i< templateData[cascadeSelectItem].length;i++){%>
                            <% var groupcascadeSelectItem = templateData[cascadeSelectItem][i]%>
                            <option value="<%= groupcascadeSelectItem.id%>"><%= groupcascadeSelectItem.name%></option>
                            <%}%>
                        </optgroup>
                        <%}%>
                    </script>
                    <input type="hidden" id="adeptRepair" name="majorCarBrand" value="${technician.adeptRepair}">
                </div>
                <div class="tips">最多只能添加三条</div>

            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    维修工龄:
                </div>
                <div class="form-item">
                    <input type="text" id="seniority" class="yqx-input yqx-input-icon yqx-input-small js-seniority" value="" placeholder="" data-v-type="required">
                    <input type="hidden" name="seniority">
                    <span class="fa icon-angle-down icon-small"></span>
                    <div class="yqx-select-options" style="width: 140px; display: none;">
                        <dl>
                        <#list seniorityEnum as seniorityStatus>
                            <dd class="yqx-select-option" data-key="${seniorityStatus.code}" <#if technician.seniority==seniorityStatus.code>selected="selected"</#if> > ${seniorityStatus.message}</dd>
                        </#list>
                        </dl>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label form-label-must">
                    技师等级:
                </div>
                <div class="form-item">
                    <input type="text" id="technicianLevel" class="yqx-input yqx-input-icon yqx-input-small js-level" value="" placeholder="" data-v-type="required">
                    <input type="hidden" name="technicianLevel">
                    <span class="fa icon-angle-down icon-small"></span>
                    <div class="yqx-select-options" style="width: 140px; display: none;">
                        <dl>
                        <#list levelEnum as levelStatus>
                            <dd class="yqx-select-option" data-key="${levelStatus.code}" <#if technician.technicianLevel==levelStatus.code>selected="selected"</#if> > ${levelStatus.message}</dd>
                        </#list>
                        </dl>
                    </div>
                </div>
            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-save">保存</button>

                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goBack">返回</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${BASE_PATH}/resources/js/lib/jquery.chosen.js"></script>
<script src="${BASE_PATH}/static/js/page/setting/technician/technician-detail.js?f74f0f1d4d74469c8e5523866e3e2324"></script>


<#include "yqx/layout/footer.ftl">