<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/reg-base.css?9f840e9f3c3f39c54bcbe9b7350edbe9"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/reg-edit.css?4aa8b2d8fff628e3fd3c9ce0dfbb5858"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline fl">微信公众号注册</h1>

            <div class="order-process clearfix fr">
                <div class="order-step">
                    <div class="order-step-circle">1</div>
                    <p class="order-step-title">支付</p>
                </div>
                <div class="order-step order-step-finish">
                    <div class="order-step-circle">2</div>
                    <p class="order-step-title">相关资料填写及上传</p>
                </div>
                <div class="order-step">
                    <div class="order-step-circle">3</div>
                    <p class="order-step-title">受理中</p>
                </div>
            </div>
        </div>
    <#if payFlow.payInfo==1>
    <div class="order-body">
        <h2>资料填写<i class="headline-info">（微信公众号必须为认证服务号）</i></h2>
        <div class="content">
            <form>
                 <fieldset>
                    <i class="fieldset-title">微信公众号登录邮箱<i class="must"></i></i>
                     <div class="form-item">
                         <input type="hidden" value="${shopWechatVo.id}" name="id">
                         <input value="1" name="opSubmitType" type="hidden">
                    <input class="yqx-input input-long"
                           data-v-type="required|email|maxLength:32"
                           data-label="登录邮箱"
                           value="${shopWechatVo.shopEmail}"
                           name="shopEmail">
                     </div>
                    <label>注册微信公众号时使用的邮箱</label>
                </fieldset>
                <fieldset>
                    <i class="fieldset-title">微信公众号登录密码<i class="must"></i></i>
                    <div class="form-item">
                    <input class="yqx-input input-long"
                           type="password"
                           data-v-type="required|maxLength:30"
                           data-label="登录密码"
                           value="${shopWechatVo.accountPassword}"
                           name="accountPassword">
                        </div>
                </fieldset>
                <fieldset class="file-box">
                    <i class="fieldset-title">请上传支付凭证</i>
                    <div class="btn-upload" <#if payFlow.payVoucher> style="display: none" </#if>>
                        <input class="yqx-input js-file" type="file" accept="image/*" >
                        <input class="img-path" type="hidden" value="${payFlow.payVoucher}" name="payVoucher">
                    </div>
                    <#if payFlow.payVoucher>
                        <div class="file-show">
                            <img class="btn-upload-img" src="${payFlow.payVoucher}">
                            <i class="file-del js-file-del" title="删除"></i>
                        </div>
                    </#if>
                    <label>
                        <p>转账记录</p>
                        <p>支持.jpg .jpeg .bmp .gif .png格式照片，</p>
                        <p>大小不超过5M</p>
                    </label>
                </fieldset>
            </form>
        </div>

        <div class="confirm-line">
            <button class="yqx-btn yqx-btn-3 js-save">完成并提交审核</button>
        </div>
        </div>
    <#else>

        <div class="order-body">
            <h2>资料填写<i class="warning">微信公众号认证所需资料，请按实填写完整</i>(<i style="color:red">*</i>必填)</h2>

            <div class="content">
                <form class="basic">
                    <input type="hidden" value="${shopWechatVo.id}" name="id">
                    <fieldset>
                        <i class="fieldset-title">邮箱<i class="must"></i></i>
                        <div class="form-item">
                            <input value="1" name="opSubmitType" type="hidden">
                        <input class="yqx-input input-long" value="${shopWechatVo.shopEmail}" name="shopEmail"
                               data-v-type="required|email|maxLength:32"
                               data-label="邮箱"
                               name="shopEmail">
                            </div>
                        <label>用来登陆公众平台，接受到激活邮件才能完成注册</label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">姓名<i class="must"></i></i>
                        <div class="form-item">
                        <input class="yqx-input" value="${shopWechatVo.userName}" name="userName"
                               data-v-type="required|maxLength:30"
                               data-label="姓名"
                               name="userName">
                            </div>
                        <label>请填写老板或者相关人员姓名，后续需要该人员配合账号开通</label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">手机号码<i class="must"></i></i>
                        <div class="form-item">
                        <input class="yqx-input" value="${shopWechatVo.shopMobile}" name="shopMobile" class="mobile"
                               data-v-type="required|phone"
                               data-label="手机号码"
                               name="shopMobile">
                            </div>
                        <label>请输入老板或相关人员手机号码，后续需要该人员配合发送验证码等验证信息</label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">座机号码</i>
                        <div class="form-item">
                        <input class="yqx-input"
                               data-v-type="tel|maxLength:18"
                               data-label="座机号码"
                               value="${shopWechatVo.shopTelephone}"  name="shopTelephone">
                            </div>
                        <label>请输入老板或运营人员的座机号码，包括：区号-电话-分机号</label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">邮寄地址</i>
                        <div class="form-item">
                            <input class="yqx-input js-address"
                                   placeholder="选择省份"
                                   id="province" data-target="#city">
                            <input name="shopProvince" type="hidden"
                                   value="${shopWechatVo.shopProvince}">
                            <span class="fa">省</span>
                        </div>
                        <div class="form-item">
                            <input class="yqx-input js-address"
                                   placeholder="选择城市"
                                   id="city" data-target="#district">
                            <input name="shopCity" type="hidden"
                                value="${shopWechatVo.shopCity}">
                            <span class="fa">市</span>
                        </div>
                        <div class="form-item">
                            <input name="shopDistrict" class="yqx-input js-address"
                                   placeholder="选择县/区"
                                   id="district" data-target="#street" >
                            <input name="shopDistrict" type="hidden" value="${shopWechatVo.shopDistrict}">
                            <span class="fa">县/区</span>
                        </div>
                        <div class="form-item">
                            <input class="yqx-input js-address"
                                   placeholder="选择街道"
                                   id="street">
                            <input name="shopStreet" type="hidden"
                                   value="${shopWechatVo.shopStreet}">
                            <span class="fa">街道</span>
                        </div>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">详细地址</i>
                        <div class="form-item">
                        <input class="yqx-input"
                               data-v-tyle="maxLength:100"
                               data-label="详细地址"
                               style="width: 338px;" value="${shopWechatVo.shopAddress}" name="shopAddress">
                            </div>
                        <label>请输入老板或运营人员所在的地址</label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">邮政编码</i>
                        <div class="form-item">
                        <input class="yqx-input" value="${shopWechatVo.shopPostcode}" name="shopPostcode"
                               name="postcode"
                               data-label="邮政编码"
                               data-v-type="zip|maxLength:10">
                            </div>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">个人微信号</i>
                        <div class="form-item">
                        <input class="yqx-input"
                               data-v-type="maxLength:20"
                               data-label="个人微信号"
                               value="${shopWechatVo.perWechatid}" name="perWechatid">
                            </div>
                        <label>请输入老板或运营人员手机号码绑定的微信号，后续需要扫描验证</label>
                    </fieldset>
                    <div class="dash-line"></div>
                    <fieldset>
                        <i class="fieldset-title">身份证号</i>
                        <div class="form-item">
                        <input class="yqx-input input-long"
                               data-v-type="idCard"
                               data-label="身份证号"
                               value="${shopWechatVo.ownerIdcard}" name="ownerIdcard">
                        </div>
                        <label>请输入上述姓名的身份证号</label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">身份证照片(正面)</i>
                        <div class="btn-upload" <#if shopWechatVo.idcardPimg>style="display: none"</#if>>
                            <input class="yqx-input js-file"
                                   type="file" accept="image/*">
                            <input class="img-path" type="hidden"
                                   value="${shopWechatVo.idcardPimg}" name="idcardPimg">
                        </div>
                        <#if shopWechatVo.idcardPimg >
                            <div class="file-show idcar">
                                <img class="btn-upload-img" src="${shopWechatVo.idcardPimg}">
                                <i class="file-del js-file-del" title="删除"></i>
                            </div>
                        </#if>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">身份证照片(反面)</i>
                        <div class="btn-upload" <#if shopWechatVo.idcardRimg>style="display: none"</#if>>
                            <input class="yqx-input js-file"
                                   type="file" accept="image/*">
                            <input class="img-path" type="hidden"
                                   value="${shopWechatVo.idcardRimg}" name="idcardRimg">
                        </div>
                        <#if shopWechatVo.idcardRimg >
                            <div class="file-show idcar">
                                <img class="btn-upload-img" src="${shopWechatVo.idcardRimg}">
                                <i class="file-del js-file-del" title="删除"></i>
                            </div>
                        </#if>
                    </fieldset>
                    <div class="idcard-box">
                        <div class="idcard">
                            <img disabled class="idcard-img" id="idcardFront"
                                 src="${BASE_PATH}/static/img/page/wechat/reg-edit/idcard-front.jpg">
                            <p class="color-999">身份证（正面）</p>
                        </div>
                        <div class="idcard">
                            <img disabled class="idcard-img" id="idcardBack"
                                 src="${BASE_PATH}/static/img/page/wechat/reg-edit/idcard-back.jpg">
                            <p class="color-999">身份证（反面）</p>
                        </div>
                    </div>
                 <div class="dash-line"></div>
                <fieldset class="check-radio">
                    <i class="fieldset-title">公众号类型</i>
                    <div><input type="radio" class="js-radio" name="companyType" value="1"
                                <#if shopWechatVo.companyType==1>checked</#if>
                                <#if !shopWechatVo.companyType>checked</#if>
                                 data-target="#person">
                        <i>个体工商户</i>
                    </div>
                    <div><input type="radio" name="companyType" value="2"
                                <#if shopWechatVo.companyType==2>checked</#if>
                                class="js-radio"
                                data-target="#company">
                        <i>企业法人</i>
                    </div>
                </fieldset>

                </form>
               <form id="person"
                    <#if shopWechatVo.companyType==1 || !shopWechatVo.companyType>
                       class="form-current"
                      <#else> class="hide"</#if>
                        >
                   <input value="1" name="opSubmitType" type="hidden">
                    <fieldset>
                        <i class="fieldset-title">个体工商户名称</i>
                        <div class="form-item">
                        <input class="yqx-input input-long"
                               data-v-type="maxLength:62"
                               value="${shopWechatVo.companyName}" name="companyName">
                            </div>
                        <label>请填写工商营业执照的名称，注意提交后不允许修改</label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">工商执照注册号</i>
                        <div class="form-item">
                        <input class="yqx-input input-long"
                               data-v-type="maxLength:20"
                               data-label="工商执照注册号"
                               value="${shopWechatVo.bizLicense}" name="bizLicense">
                            </div>
                        <label>请填写工商营业执照上的注册号</label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">经营者姓名</i>
                        <div class="form-item">
                        <input class="yqx-input"
                               data-v-type="maxLength:30"
                               data-label="经营者姓名"
                               value="${shopWechatVo.chargePerson}" name="chargePerson">
                            </div>
                        <label>请填写工商营业执照上的姓名</label>
                    </fieldset>
                    <fieldset>
                        <div class="fieldset-title">
                            <p>经营范围</p>
                            <p class="color-999">(一般经营范围)</p>
                        </div>
                        <div class="form-item">
                        <input class="yqx-input input-long"
                               data-v-type="maxLength:100"
                               data-label="经营范围"
                               value="${shopWechatVo.bizScopeCommon}" name="bizScopeCommon">
                            </div>
                    <#--maxLength:100-->
                        <label>与企业工商营业执照上一致</label>
                    </fieldset>
                    <fieldset>
                        <div class="fieldset-title">
                            <p>经营范围</p>
                            <p class="color-999">(前置许可经营范围)</p>
                        </div>
                        <div class="form-item">
                        <input class="yqx-input input-long"
                               data-v-type="maxLength:100"
                               data-label="经营范围"
                               value="${shopWechatVo.bizScopeBefore}" name="bizScopeBefore">
                            </div>
                        <label>没有则写“无”</label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">开户名称</i>
                        <div class="form-item">
                        <input class="yqx-input"
                               data-v-type="maxLength:62"
                               data-label="开户名称"
                               value="${shopWechatVo.companyAccountName}" name="companyAccountName">
                            </div>
                        <label><p>若有对公账户，填写个体工商户对公账户名称。</p>
                            <p>若无对公账户，填写营业执照上经营者个人开户名称。</p></label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">银行账户</i>
                        <div class="form-item">
                        <input class="yqx-input input-long"
                               data-v-type="maxLength:25"
                               data-label="经营范围(前置)"
                               value="${shopWechatVo.bankAccount}" name="bankAccount">
                        <label>请务必填写正确，填写错误会造成打款验证失败，会导致账号冻结。</label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">开户银行</i>
                        <div class="form-item">
                        <input class="yqx-input input-long bank js-address"
                               placeholder="请选择开户银行"
                               value="${shopWechatVo.bankName}" name="bankName">
                            <span class="fa icon-angle-down"></span>
                        </div>
                        <label></label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">开户地点</i>
                        <div class="form-item">
                            <input class="yqx-input js-address"
                                   id="bankProvince2" data-target="#bankCity2"
                                   placeholder="选择省份"
                                   >
                            <input type="hidden"
                                   name="bankProvince"
                                   value="${shopWechatVo.bankProvince}">
                            <span class="fa">省</span>
                        </div>
                        <div class="form-item">
                            <input class="yqx-input js-address"
                                   placeholder="选择城市"
                                   id="bankCity2">
                            <input type="hidden"
                                   name="bankCity"
                                   value="${shopWechatVo.bankCity}">
                            <span class="fa">市</span>
                        </div>
                        <label></label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">个体工商营业执照</i>
                        <div class="btn-upload" <#if shopWechatVo.bizLicenseImg>style="display: none;"</#if>>
                            <input class="yqx-input js-file" type="file" accept="image/*">
                            <input class="img-path" type="hidden" value="${shopWechatVo.bizLicenseImg}" name="bizLicenseImg">
                        </div>
                        <#if shopWechatVo.bizLicenseImg >
                            <div class="file-show">
                                <img class="btn-upload-img" src="${shopWechatVo.bizLicenseImg}">
                                <i class="file-del js-file-del" title="删除"></i>
                            </div>
                        </#if>
                        <div class="file-show template-img license-box">
                            <img class="btn-upload-img"
                                 src="${BASE_PATH}/static/img/page/wechat/reg-edit/orglicense.jpg">
                        </div>
                        <label>
                            <p>只支持中国大陆工商局或市场监督管理局颁发的工商营</p>
                            <p>业执照，且必须在有效期内。</p>
                            <p>格式要求：原件照片、扫描件或者加盖公章的复印件，</p>
                            <p>支持.jpg.jpeg.bmp.gif.png格式照片，大小不超过5M</p>
                        </label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">申请公函</i>
                        <div class="btn-upload" <#if shopWechatVo.applyLetterImg> style="display: none;"</#if>>
                            <input class="yqx-input js-file" type="file" accept="image/*">
                            <input class="img-path" type="hidden" value="${shopWechatVo.applyLetterImg}" name="applyLetterImg">
                        </div>
                        <#if shopWechatVo.applyLetterImg >
                            <div class="file-show">
                                <img class="btn-upload-img" src="${shopWechatVo.applyLetterImg}">
                                <i class="file-del js-file-del" title="删除"></i>
                            </div>
                        </#if>
                        <div class="file-show template-img apply-letter-box">
                            <img disabled class="btn-upload-img"
                                 src="${BASE_PATH}/static/img/page/wechat/reg-edit/applyLetter.jpg">
                        </div>
                        <label>
                            <p>下载申请公函模板，</p>
                            <p>按照右侧提示填写必填项，盖章后上传</p>
                            <p>支持.jpg .jpeg .bmp .gif .png格式照片，大小不超过5M</p>
                            <a href="http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/wechat/申请公函.doc"
                               class="margin-top-10 btn-download yqx-btn yqx-btn-default">下载模板</a>
                        </label>
                    </fieldset>
                   <fieldset class="file-box">
                       <i class="fieldset-title">请上传支付凭证</i>
                       <div class="btn-upload" <#if payFlow.payVoucher> style="display: none" </#if>>
                           <input class="yqx-input js-file" type="file" accept="image/*" >
                           <input class="img-path" type="hidden" value="${payFlow.payVoucher}" name="payVoucher">
                       </div>
                       <#if payFlow.payVoucher>
                           <div class="file-show">
                               <img class="btn-upload-img" src="${payFlow.payVoucher}">
                               <i class="file-del js-file-del" title="删除"></i>
                           </div>
                       </#if>
                       <label>
                           <p>转账记录</p>
                           <p>支持.jpg .jpeg .bmp .gif .png格式照片，</p>
                           <p>大小不超过5M</p>
                       </label>
                   </fieldset>
                </form>
                <form id="company"
                    <#if shopWechatVo.companyType==2> class="form-current"
                    <#else> class="hide"</#if>
                     >
                    <fieldset>
                        <i class="fieldset-title">企业全称</i>
                        <div class="form-item">
                        <input class="yqx-input input-long"
                               data-v-type="maxLength:62"
                               data-label="企业全称"
                               value="${shopWechatVo.companyName}" name="companyName">
                            </div>
                        <label>请填写工商营业执照的名称，注意提交后不允许修改</label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">组织结构代码</i>
                        <div class="form-item">
                        <input class="yqx-input input-long"
                               data-v-type="maxLength:20"
                               data-label="组织结构代码"
                               value="${shopWechatVo.orgCode}" name="orgCode">
                            </div>
                        <label>能与组织机构代码证匹配</label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">工商执照注册号</i>
                        <div class="form-item">
                        <input class="yqx-input input-long"
                               data-v-type="maxLength:20"
                               data-label="工商执照注册号"
                               value="${shopWechatVo.bizLicense}" name="bizLicense">
                            </div>
                        <label>请填写工商营业执照上的注册号</label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">法定代表人<p>或企业负责人姓名</p></i>
                        <div class="form-item">
                        <input class="yqx-input"
                               data-v-type="maxLength:30"
                               data-label="法定代表人活企业负责人姓名"
                               value="${shopWechatVo.chargePerson}" name="chargePerson">
                            </div>
                        <label>如果属于分公司则填写工商营业执照上明确的负责人</label>
                    </fieldset>
                    <fieldset>
                        <div class="fieldset-title">
                            <p>经营范围</p>
                            <p class="color-999">(一般经营范围)</p>
                        </div>
                        <div class="form-item">
                        <input class="yqx-input input-long"
                               data-v-type="maxLength:100"
                               data-label="经营范围"
                               value="${shopWechatVo.bizScopeCommon}" name="bizScopeCommon">
                            </div>
                        <label>与企业工商营业执照上一致</label>
                    </fieldset>
                    <fieldset>
                        <div class="fieldset-title">
                            <p>经营范围</p>
                            <p class="color-999">(前置许可经营范围)</p>
                        </div>
                        <div class="form-item">
                        <input class="yqx-input input-long"
                               data-v-type="maxLength:100"
                               data-label="经营范围"
                               value="${shopWechatVo.bizScopeBefore}" name="bizScopeBefore">
                            </div>
                        <label>没有则写“无”</label>
                    </fieldset>
                    <fieldset>
                        <div class="fieldset-title">
                            <p>企业规模</p>
                            <p class="color-999">(选填)</p>
                        </div>
                        <div class="form-item">
                        <input class="yqx-input"
                               data-v-type="maxLength:100"
                               data-label="企业规模"
                               value="${shopWechatVo.enterpriseScale}" name="enterpriseScale">
                            </div>
                        <label>企业员工人数</label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">企业开户名称</i>
                        <div class="form-item">
                        <input class="yqx-input"
                               data-v-type="maxLength:62"
                               data-label="经营范围(前置)"
                               value="${shopWechatVo.companyAccountName}" name="companyAccountName">
                            </div>
                        <label>
                            <p>若有对公账户，填写个体工商户对公账户名称。</p>
                            <p>若无对公账户，填写营业执照上经营者个人开户名称。</p></label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">企业银行账户</i>
                        <div class="form-item">
                        <input class="yqx-input input-long"
                               data-v-type="maxLength:25"
                               data-label="企业银行账户"
                               value="${shopWechatVo.bankAccount}" name="bankAccount">
                        <label>请务必填写正确，填写错误会造成打款验证失败，会导致账号冻结。</label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">开户银行</i>
                        <div class="form-item">
                        <input class="yqx-input input-long js-address bank"
                               placeholder="选择开户银行"
                               value="${shopWechatVo.bankName}" name="bankName">
                            <span class="fa icon-angle-down"></span>
                            </div>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">开户地点</i>

                        <div class="form-item">
                            <input class="yqx-input js-address"
                                   placeholder="选择省份"
                                   id="bankProvince" data-target="#bankCity"
                                   >
                            <input type="hidden"
                                   name="bankProvince"
                                   value="${shopWechatVo.bankProvince}">
                            <span class="fa">省</span>
                        </div>
                        <div class="form-item">
                            <input class="yqx-input js-address"
                                   placeholder="选择城市"
                                   id="bankCity">
                            <input type="hidden"
                                   name="bankCity"
                                   value="${shopWechatVo.bankCity}" >
                            <span class="fa">市</span>
                        </div>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">组织结构代码照</i>

                        <div class="btn-upload" <#if shopWechatVo.orgCodeImg> style="display:none"</#if>>
                            <input class="yqx-input js-file" type="file" accept="image/*">
                            <input class="img-path" type="hidden"
                                   data-v-type="maxLength:20"
                                   data-label="组织结构代码"
                                   value="${shopWechatVo.orgCodeImg}" name="orgCodeImg">
                        </div>
                        <#if shopWechatVo.orgCodeImg >
                            <div class="file-show">
                                <img class="btn-upload-img" src="${shopWechatVo.orgCodeImg}">
                                <i class="file-del js-file-del" title="删除"></i>
                            </div>
                        </#if>
                        <div class="file-show template-img orgcode-box">
                            <img class="btn-upload-img"
                                 src="${BASE_PATH}/static/img/page/wechat/reg-edit/orgcode.jpg">
                        </div>
                        <label>
                            <p>组织机构代码证必须在有效期范围内。</p>
                            <p>格式要求：原件照片、扫描件或加盖公章的复印件</p>
                            <p>支持.jpg .jpeg .bmp .gif .png格式照片，大小不超过5M</p>
                        </label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">企业工商营业执照</i>
                        <div class="btn-upload" <#if shopWechatVo.bizLicenseImg> style="display:none"</#if>>
                            <input class="yqx-input js-file" type="file" accept="image/*">
                            <input class="img-path" type="hidden" value="${shopWechatVo.bizLicenseImg}" name="bizLicenseImg">
                        </div>
                        <#if shopWechatVo.bizLicenseImg >
                            <div class="file-show">
                                <img class="btn-upload-img" src="${shopWechatVo.bizLicenseImg}">
                                <i class="file-del js-file-del" title="删除"></i>
                            </div>
                        </#if>
                        <div class="file-show template-img license-box">
                            <img class="btn-upload-img"
                                 src="${BASE_PATH}/static/img/page/wechat/reg-edit/orglicense.jpg">
                        </div>

                        <label>
                            <p>只支持中国大陆工商局或市场监督管理局颁发的工商营</p>
                            <p>业执照，且必须在有效期内。</p>
                            <p>格式要求：原件照片、扫描件或者加盖公章的复印件，</p>
                            <p>支持.jpg.jpeg.bmp.gif.png格式照片，大小不超过5M</p>
                        </label>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">申请公函</i>
                        <div class="btn-upload"<#if shopWechatVo.applyLetterImg> style="display: none"</#if>>
                            <input class="yqx-input js-file" type="file" accept="image/*">
                            <input class="img-path" type="hidden" value="${shopWechatVo.applyLetterImg}" name="applyLetterImg">
                        </div>
                        <#if shopWechatVo.applyLetterImg >
                            <div class="file-show">
                                <img class="btn-upload-img" src="${shopWechatVo.applyLetterImg}">
                                <i class="file-del js-file-del" title="删除"></i>
                            </div>
                        </#if>
                        <div class="file-show template-img apply-letter-box">
                            <img disabled class="btn-upload-img" src="${BASE_PATH}/static/img/page/wechat/reg-edit/applyLetter.jpg">
                        </div>
                        <label>
                            <p>下载申请公函模板，</p>
                            <p>按照右侧提示填写必填项，盖章后上传</p>
                            <p>支持.jpg .jpeg .bmp .gif .png格式照片，大小不超过5M</p>
                            <a href="http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/wechat/申请公函.doc"
                               class="margin-top-10 btn-download yqx-btn yqx-btn-default">下载模板</a>
                        </label>
                    </fieldset>
                    <fieldset class="file-box">
                        <i class="fieldset-title">请上传支付凭证</i>
                        <div class="btn-upload" <#if payFlow.payVoucher> style="display: none" </#if>>
                            <input class="yqx-input js-file" type="file" accept="image/*" >
                            <input class="img-path" type="hidden" value="${payFlow.payVoucher}" name="payVoucher">
                        </div>
                        <#if payFlow.payVoucher>
                            <div class="file-show">
                                <img class="btn-upload-img" src="${payFlow.payVoucher}">
                                <i class="file-del js-file-del" title="删除"></i>
                            </div>
                        </#if>
                        <label>
                            <p>转账记录</p>
                            <p>支持.jpg .jpeg .bmp .gif .png格式照片，</p>
                            <p>大小不超过5M</p>
                        </label>
                    </fieldset>
                </form>
            </div>
            <div class="confirm-line">
                <button class="yqx-btn yqx-btn-3 js-next">完成并提交审核</button>
            </div>
        </div>
    </#if>
    </div>
    </div>
    <script src="${BASE_PATH}/static/js/page/wechat/reg-edit.js?45af810faf8d3861e6a9db3e01c2a6d4"></script>
<#include "yqx/layout/footer.ftl" >