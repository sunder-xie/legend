<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/notice.css?b84c4018f893e38ca543793752259edc"/>
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
            <h3 class="headline">提醒设置</h3>
        </div>
        <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
        <div class="content">
            <ul id="formData">
                <li>
                    <h3>预约提醒</h3>
                    <div class="form-label">
                        到期前 第几天提醒:
                    </div>
                    <div class="form-item">
                        <input type="text"
                               name="appointNoteFirstValue"
                               class="yqx-input yqx-input-icon yqx-input-small"
                               value="${appointConf.firstValue}"
                               data-v-type="integer"/>
                        <span class="fa icon-small">天</span>
                    </div>
                    <div class="form-label">
                        过期后 第几天自动删除:
                    </div>
                    <div class="form-item">
                        <input type="text"
                               name="appointNoteInvalidValue"
                               class="yqx-input yqx-input-icon yqx-input-small"
                               value="${appointConf.invalidValue}"
                               placeholder=""
                               data-v-type="integer"/>
                        <span class="fa icon-small">天</span>
                    </div>
                </li>
                <li>
                    <h3>回访提醒</h3>
                    <div class="form-label">
                        工单完成后 第几天提醒:
                    </div>
                    <div class="form-item">
                        <input type="text"
                               name="visitNoteFirstValue"
                               class="yqx-input yqx-input-icon yqx-input-small"
                               value="${visitConf.firstValue}"
                               data-v-type="integer"/>
                        <span class="fa icon-small">天</span>
                    </div>
                    <div class="form-label">
                        过期后 第几天自动删除:
                    </div>
                    <div class="form-item">
                        <input type="text"
                               name="visitNoteInvalidValue"
                               class="yqx-input yqx-input-icon yqx-input-small"
                               value="${visitConf.invalidValue}"
                               data-v-type="integer"/>
                        <span class="fa icon-small">天</span>
                    </div>
                </li>
                <li>
                    <h3>保险提醒</h3>
                    <div class="form-label">
                        到期前 第几天提醒:
                    </div>
                    <div class="form-item">
                        <input type="text"
                               name="insuranceNoteFirstValue"
                               class="yqx-input yqx-input-icon yqx-input-small"
                               value="${insuranceConf.firstValue}"
                               placeholder=""
                               data-v-type="integer"/>
                        <span class="fa icon-small">天</span>
                    </div>
                    <div class="form-label">
                        过期后 第几天自动删除:
                    </div>
                    <div class="form-item">
                        <input type="text"
                               name="insuranceNoteInvalidValue"
                               class="yqx-input yqx-input-icon yqx-input-small"
                               value="${insuranceConf.invalidValue}"
                               placeholder=""
                               data-v-type="integer"/>
                        <span class="fa icon-small">天</span>
                    </div>
                </li>
                <li>
                    <h3>年检提醒</h3>
                    <div class="form-label">
                        到期前 第几天提醒:
                    </div>
                    <div class="form-item">
                        <input type="text"
                               name="auditNoteFirstValue"
                               class="yqx-input yqx-input-icon yqx-input-small"
                               value="${auditConf.firstValue}"
                               data-v-type="integer"/>
                        <span class="fa icon-small">天</span>
                    </div>
                    <div class="form-label">
                        过期后 第几天自动删除:
                    </div>
                    <div class="form-item">
                        <input type="text"
                               name="auditNoteInvalidValue"
                               class="yqx-input yqx-input-icon yqx-input-small"
                               value="${auditConf.invalidValue}"
                               data-v-type="integer"/>
                        <span class="fa icon-small">天</span>
                    </div>
                </li>
                <li>
                    <h3>保养提醒</h3>
                    <div class="div_twoline">
                        <div class="form-label">
                            到期前 第几天第一次提醒:
                        </div>
                        <div class="form-item">
                            <input type="text"
                                   name="keepupNoteFirstValue"
                                   class="yqx-input yqx-input-icon yqx-input-small"
                                   value="${keepupConf.firstValue}"
                                   data-v-type="integer"/>
                            <span class="fa icon-small">天</span>
                        </div>
                        <div class="form-label m-top">
                            到期前 第几天第二次提醒:
                        </div>
                        <div class="form-item m-top">
                            <input type="text"
                                   name="keepupNoteSecondValue"
                                   class="yqx-input yqx-input-icon yqx-input-small"
                                   value="${keepupConf.secondValue}"
                                   data-v-type="integer"/>
                            <span class="fa icon-small">天</span>
                        </div>
                    </div>
                    <div class="form-label">
                        过期后 第几天自动删除:
                    </div>
                    <div class="form-item">
                        <input type="text"
                               name="keepupNoteInvalidValue"
                               class="yqx-input yqx-input-icon yqx-input-small"
                               value="${keepupConf.invalidValue}"
                               data-v-type="integer"/>
                        <span class="fa icon-small">天</span>
                    </div>
                </li>
                <li>
                    <h3>客户生日提醒</h3>
                    <div class="form-label">
                        到期前 第几天提醒:
                    </div>
                    <div class="form-item">
                        <input type="text"
                               name="birthdyNoteFirstValue"
                               class="yqx-input yqx-input-icon yqx-input-small"
                               value="${birthdyConf.firstValue}"
                               data-v-type="integer"/>
                        <span class="fa icon-small">天</span>
                    </div>
                    <div class="form-label">
                        过期后自动删除
                    </div>
                </li>
                <li>
                    <h3>流失客户</h3>
                    <div class="form-label">
                        到期前 第几天提醒:
                    </div>
                    <div class="form-item">
                        <input type="text"
                               name="lostCustomerNoteFirstValue"
                               class="yqx-input yqx-input-icon yqx-input-small"
                               value="${lostCustomerConf.firstValue}"
                               data-v-type="integer"/>
                        <span class="fa icon-small">天</span>
                    </div>
                    <div class="form-label">
                        过期后第几天自动删除:
                    </div>
                    <div class="form-item">
                        <input type="text"
                               name="lostCustomerNoteInvalidValue"
                               class="yqx-input yqx-input-icon yqx-input-small"
                               value="${lostCustomerConf.invalidValue}"
                               data-v-type="integer"/>
                        <span class="fa icon-small">天</span>
                    </div>
                </li>
            </ul>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-save">保存</button>
            </div>
        </div>
        </#if>

    </div>

    <!-- 右侧内容区 end -->
</div>

<script src="${BASE_PATH}/static/js/page/setting/notice.js?77f55ae49c0ccd4b38d718efa308b3be"></script>
<#include "yqx/layout/footer.ftl">
