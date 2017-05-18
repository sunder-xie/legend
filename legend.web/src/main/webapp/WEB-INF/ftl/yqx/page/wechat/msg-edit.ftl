<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/reg-base.css?9f840e9f3c3f39c54bcbe9b7350edbe9"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/msg-edit.css?39a391f69896cd7c56eb4dbcd1a1e357"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline">自动回复 - <small id="subtitle" class="hide"><#if templateReply.id>编辑<#else>添加</#if>回复</small></h1>
        </div>
        <div class="order-body">
            <div class="content">
                <div>
                    <input type="hidden" name="id" value="${templateReply.id}">
                    <input type="hidden" name="id" value="${templateReply.id}">
                    <div class="must-box">
                    <fieldset class="position-relative">
                        <i class="fieldset-title">类型<i class="must"></i></i>
                        <div class="form-item">
                            <input class="yqx-input js-reply-status" placeholder="选择类型"
                                   data-v-type="required"
                                   data-label="类型"
                                   value="<#if templateReply.replyStatus == 0>用户消息回复</#if><#if templateReply.replyStatus == 1>关键字回复</#if><#if templateReply.replyStatus == 2>被添加回复</#if>">
                            <i class="fa icon-angle-down"></i>
                            <input type="hidden" name="replyStatus" class="js-hidden-status"
                                   data-v-type="required"
                                   value="${templateReply.replyStatus}"
                                   data-label="类型"
                                    >
                        </div><i class="question-icon margin-left-10 js-show-tips" data-tips=""></i>
                        <div class="msg-tips hide"></div>
                    </fieldset>
                    <fieldset class="keyword-box">
                        <i class="fieldset-title">关键字<i class="must"></i></i>
                        <div class="info-tags">
                            <div id="tagWrapper">
                                <#if templateReply.replyKeywords && templateReply.replyKeywords?size>
                                    <#list templateReply.replyKeywords as keyword>
                                        <div class="info-tag js-tag-edit replyKeyword">
                                            <i>${keyword}</i>
                                        </div>
                                    </#list>
                                </#if>
                            </div>
                            <div class="info-tag tag-add" id="tagAdd">
                                <i class="icon-plus"></i>
                            </div>
                            <div class="info-tag" id="input-box" style="display: none">
                                <input id="tag-input" placeholder="请输入">
                                <i class="tag-btn" id="tagAddBtn">确定</i>
                            </div>
                        </div>
                        <lable id="comment">关键字消息时必填</lable>
                    </fieldset>
                    </div>
                    <fieldset>
                        <i class="fieldset-title">模板</i>
                        <div class="form-item margin-right-36">
                            <input type="radio" data-target=".txt-box" name="tplType"
                                   class="js-type"
                                   <#if templateReply.tplType==0 || templateReply.tplType == "">checked</#if>
                                   value="0" >文字消息
                        </div><div class="form-item margin-right-36">
                            <input type="radio" data-target=".mix-box" name="tplType"
                                   class="js-type"
                                   <#if templateReply.tplType==1>checked</#if>
                                   value="1">图文消息
                        </div><div class="form-item">
                        <input type="radio" data-target=".mix-box" name="tplType"
                               class="js-type"
                               <#if templateReply.tplType==2>checked</#if>
                               value="2">图片消息
                        </div>
                    </fieldset>
                    <div class="txt-box <#if templateReply.tplType==0 || templateReply.tplType == "">type-current<#else>hide</#if>">
                    <fieldset>
                        <i class="fieldset-title">回复消息<i class="must"></i></i>
                        <div class="form-item">
                        <textarea class="yqx-input"
                                  data-v-type="required|maxLength:200"
                                  data-label="回复消息"
                                  name="replyContent"
                                >${templateReply.replyContent}</textarea>
                            </div>
                    </fieldset>
                    </div>
                    <div class="mix-box  <#if templateReply.tplType==1 || templateReply.tplType==2>type-current<#else>hide</#if>">
                    <fieldset>
                        <i class="fieldset-title"> <#if templateReply.tplType==1>图文消息标题<#else>图片消息标题</#if></i>
                        <div class="form-item">
                        <input class="yqx-input yqx-input-mid"
                               name="articleTitle"
                               data-label="图文消息标题"
                               data-v-type="maxLength:100"
                               value="${templateReply.articleTitle}">
                            </div>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">添加图片<i class="must"></i></i>
                        <div class="btn-upload" <#if templateReply.picUrl>style="display: none" </#if>>
                            <input class="yqx-input js-file" type="file" accept="image/*">
                            <input class="img-path" type="hidden"
                                   value="${templateReply.picUrl}" name="picUrl">
                        </div>
                    <#if templateReply.picUrl >
                        <div class="file-show">
                            <img class="btn-upload-img"
                                 src="${templateReply.picUrl}">
                            <i class="file-del js-file-del" title="删除"></i>
                        </div>
                    </#if>
                    </fieldset>
                    <fieldset class="picture-article <#if templateReply.tplType==2>hide</#if>">
                        <i class="fieldset-title">图文消息描述</i>
                        <div class="form-item">
                        <textarea class="yqx-input"
                                  data-v-type="maxLength:200"
                                  data-label="图文消息描述"
                                  name="replyDescription"
                                >${templateReply.replyDescription}</textarea>
                            </div>
                    </fieldset>
                    <fieldset class="picture-article <#if templateReply.tplType==2>hide</#if>">
                        <i class="fieldset-title">图文链接<i class="must"></i></i>
                        <div class="form-item">
                        <input class="yqx-input yqx-input-mid"
                               name="articleUrl"
                               data-v-type="required|maxLength:200|httpUrl"
                               value="${templateReply.articleUrl}">
                        </div>
                    </fieldset>
                    </div>
                    <fieldset>
                        <i class="fieldset-title"></i>
                        <button class="yqx-btn yqx-btn-3 js-save">保存</button><button
                            class="yqx-btn yqx-btn-default js-back">返回</button>
                    </fieldset>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${BASE_PATH}/static/js/page/wechat/msg-edit.js?3db0d7ae2132585435db868768930351"></script>
<#include "yqx/layout/footer.ftl">