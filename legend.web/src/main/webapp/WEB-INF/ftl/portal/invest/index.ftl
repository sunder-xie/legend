<#include "layout/portal-header.ftl">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/join/merchants.css?a462819295c6e35511d93649b57bba58"/>

<div class="tab">
    <div class="taber active">
        <img src="${BASE_PATH}/resources/images/portal/car_h.png">
        <span>云修乘用车招商手册</span>
    </div>
    <span class="middle_line"></span>
    <div class="taber">
        <img src="${BASE_PATH}/resources/images/portal/truck.png">
        <span>云修商用车招商手册</span>
    </div>
</div>
<div class="banner">
</div>
<div class="join_hotline_part">
    <div class="telephone"></div>
    <p class="join_hotline">合作热线 ${join_phone}</p>
</div>
<div id="content"></div>
<div class="join_us_box">
    <div class="wrapper">
        <div class="join_us_title">
            <p class="join_us_cn">加入我们</p>

            <p class="join_us_eng">Join Us</p>
        </div>
        <div class="join_us_link">
            <span class="hotline">合作热线 ${join_phone}</span>
        </div>
    </div>
</div>

<ul class="top_nav">
    <li class="_join_mode" onclick="window.location.href='#join_mode'" class="cur">合作模式</li>
    <li class="_join_advantage border_t" onclick="window.location.href='#join_advantage'">合作优势</li>
    <li class="_join_conditions border_t" onclick="window.location.href='#join_conditions'">合作条件</li>
    <li class="_joining_process border_t" onclick="window.location.href='#joining_process'">合作流程</li>
    <li class="top" onclick="window.location.href='#'">TOP</li>
</ul>
<script id="passengerVehicleTpl" type="text/html">
    <div class="wrapper merchants-box join_mode" id="join_mode">
        <div class="subHead">
            <h2 class="cn">合作模式</h2>

            <h2 class="eng">Join Mode</h2>
        </div>
        <div class="container">
            <ul class="join_mode">
                <li>
                    <div class="bgImage proImg"></div>
                    <h3 class="sublittle">快保店</h3>

                    <p class="mode_summary">店面形象好<#--<br/>交通便利-->（主干道路边／近小区／单位）</p>
                    <ul class="store_requirement">
                        <li>面积：200-500m<sup>2</sup>以上</li>
                        <li>工位：2个及以上</li>
                        <li>年营业额：120万以上</li>
                        <li>位置及环境：店面形象好、服务环境好、交通便利。</li>
                        <li>员工要求：良好服务理念、良好品牌经营意识、高级技师1人以上、1名以上专科学历管理人员。</li>
                    </ul>
                </li>
                <li>
                    <div class="bgImage stdImg"></div>
                    <h3 class="sublittle">服务店</h3>

                    <p class="mode_summary">县级市／地级市／大中城市地区<#--<br/>同品牌间隔：3-5公里--></p>
                    <ul class="store_requirement">
                        <li>面积：500-1500m<sup>2</sup>以上</li>
                        <li>工位：4-8个及以上</li>
                        <li>年营业额：400万以上</li>
                        <li>位置及环境：店面形象好、服务环境好、交通便利。</li>
                        <li>员工要求：良好服务理念、良好品牌经营意识、有创建服务网络的意愿、高级技师3人以上、不低于3名专科学历以上的管理人员。</li>
                    </ul>
                </li>
                <li>
                    <div class="bgImage ctrImg"></div>
                    <h3 class="sublittle">旗舰店</h3>

                    <p class="mode_summary">地级以上城市／中心城市地区<#--<br/>同品牌间隔：8-10公里--></p>
                    <ul class="store_requirement">
                        <li>面积：1500-3000m<sup>2</sup>以上</li>
                        <li>工位：8-10个及以上</li>
                        <li>年营业额：800万以上</li>
                        <li>位置及环境：4S店的建店标准、店面形象优秀、业内影响力大、客户服务环境好。</li>
                        <li>员工要求：良好服务理念、经营者具备良好的品牌经营意识，并具清晰发展思路、高级技师6人以上、不低于5名以上专科学历管理人员。</li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>

    <div class="join_advantage_box merchants-box" id="join_advantage">
        <div class="wrapper">
            <div class="subHead mb-74">
                <h2 class="cn">合作优势</h2>

                <h2 class="eng color_three">Join Advantage</h2>
            </div>
            <ul class="join_adv_list">
                <li>
                    <div class="join_adv_bk">
                        <img style="margin-top: 10px" src="${BASE_PATH}/resources/images/join/join_advantage_5.png">
                    </div>
                    <div class="join_adv_subtitle">
                        <p class="adv_cn">品牌信誉</p>
                        <p class="adv_eng">“淘汽云修”全球汽车人连锁品牌</p>
                    </div>
                    <#--<div class="join_adv_detail">-->
                        <#--<p class="adv_cn">品牌信誉</p>-->
                        <#--<p class="adv_eng">为全球汽车人服务的连锁品牌</p>-->
                        <#--<ul>-->
                            <#--<li>淘汽云修将投入5亿巨资，打造最强大服务平台；</li>-->
                            <#--<li>广告、公关、地推等全方位出击，打造最强势服务品牌；</li>-->
                            <#--<li>淘汽云修未来两年内将合作店扩大至20000家；</li>-->
                            <#--<li>首批合作者，均可免费使用淘汽云修品牌。</li>-->
                        <#--</ul>-->
                    <#--</div>-->
                </li>
                <li class="adv_sep"></li>
                <li>
                    <div class="join_adv_bk">
                        <img src="${BASE_PATH}/resources/images/join/join_advantage_1.png">
                    </div>
                    <div class="join_adv_subtitle">
                        <p class="adv_cn">品质保证</p>
                        <p class="adv_eng">与全球数千家品牌制造商及直供代理合作</p>
                    </div>
                    <#--<div class="join_adv_detail">-->
                        <#--<p class="adv_cn">品质保证</p>-->
                        <#--<p class="adv_eng">与数千家零部件厂商和直供代理合作</p>-->
                        <#--<ul>-->
                            <#--<li>集中采购价格更具优势，确保同区域最低，不惧价格战；</li>-->
                            <#--<li>保证所有进货品质和质量，承诺质量问题退货换货；</li>-->
                            <#--<li>淘汽云修自建仓储物流系统，确保最快捷、最省心。</li>-->
                        <#--</ul>-->
                    <#--</div>-->
                </li>
                <li class="adv_sep"></li>
                <li>
                    <div class="join_adv_bk">
                        <img src="${BASE_PATH}/resources/images/join/join_advantage_3.png">
                    </div>
                    <div class="join_adv_subtitle">
                        <p class="adv_cn">系统支持</p>

                        <p class="adv_eng">“云”管理系统平台 量身定制的门店运营后台</p>
                    </div>
                    <#--<div class="join_adv_detail">-->
                        <#--<p class="adv_cn">系统支持</p>-->

                        <#--<p class="adv_eng">采用“云”管理系统平台</p>-->
                        <#--<ul>-->
                            <#--<li>斥资5千万人民币开发“云”管理系统，专业功能赶超品牌4S店；</li>-->
                            <#--<li>合作店可免费使用，且提供培训指导、营销管理等诸多功能； </li>-->
                            <#--<li>大数据客情分析，可根据维修情况预测下次维修项目及节点等；</li>-->
                            <#--<li>大数据仓储管理，可清晰准确掌握仓储情况，高效有序采购下单；</li>-->
                            <#--<li>维修服务监控，管理人员可远程监控车辆维修，以促进管理升级、服务升级、效率升级及操作规范升级。</li>-->
                        <#--</ul>-->
                    <#--</div>-->
                </li>
                <li class="adv_sep"></li>
                <li>
                    <div class="join_adv_bk">
                        <img src="${BASE_PATH}/resources/images/join/join_advantage_2.png">
                    </div>
                    <div class="join_adv_subtitle">
                        <p class="adv_cn">客源渠道</p>

                        <p class="adv_eng">多渠道引入客源 以品牌及规模优势拓展客群</p>
                    </div>
                    <#--<div class="join_adv_detail">-->
                        <#--<p class="adv_cn">客源渠道</p>-->

                        <#--<p class="adv_eng">以品牌及规模优势拓展客群</p>-->
                        <#--<ul>-->
                            <#--<li>以大规模广宣推广，聚焦关注，增加客源线索；</li>-->
                            <#--<li>保险公司等优势资源深度合作，引入客源；</li>-->
                            <#--<li>规模效应 & 互联网手段客源分流与导流，扩大客群。</li>-->
                        <#--</ul>-->
                    <#--</div>-->
                </li>
                <li>
                    <div class="join_adv_bk">
                        <img style="margin-top: 15px" src="${BASE_PATH}/resources/images/join/join_advantage_6.png">
                    </div>
                    <div class="join_adv_subtitle">
                        <p class="adv_cn">技术保障</p>

                        <p class="adv_eng">提供完善的技术培训支持保障</p>
                    </div>
                    <#--<div class="join_adv_detail">-->
                        <#--<p class="adv_cn">技术保障</p>-->

                        <#--<p class="adv_eng">提供完善的技术培训支持保障</p>-->
                        <#--<ul>-->
                            <#--<li>提供各类专项技术支持及定期培训；</li>-->
                            <#--<li>提供远程技术咨询及服务支持；</li>-->
                            <#--<li>车主定向培训及上门知识普及。</li>-->
                        <#--</ul>-->
                    <#--</div>-->
                </li>
                <li class="adv_sep"></li>
                <li>
                    <div class="join_adv_bk">
                        <img style="margin-top: 10px" src="${BASE_PATH}/resources/images/join/join_advantage_7.png">
                    </div>
                    <div class="join_adv_subtitle">
                        <p class="adv_cn">营销支持</p>

                        <p class="adv_eng">提升销量的解决方案 庞大资源的营销方案</p>
                    </div>
                    <#--<div class="join_adv_detail">-->
                        <#--<p class="adv_cn">金融后盾</p>-->

                        <#--<p class="adv_eng">银行作后盾，专属信用卡随意“购”</p>-->
                        <#--<ul>-->
                            <#--<li>与各大银行合作，未来打造云修专属信用卡，免息账期内，银行垫付，资金周转无压力；</li>-->
                            <#--<li>资金使用更高效、更便捷，客户满意度更高。</li>-->
                        <#--</ul>-->
                    <#--</div>-->
                </li>
                <li class="adv_sep"></li>
                <li>
                    <div class="join_adv_bk">
                        <img style="margin-top: 12px" src="${BASE_PATH}/resources/images/join/join_advantage_8.png">
                    </div>
                    <div class="join_adv_subtitle">
                        <p class="adv_cn">创新支持</p>

                        <p class="adv_eng">适合新时代的项目</p>
                    </div>
                    <#--<div class="join_adv_detail">-->
                        <#--<p class="adv_cn">信誉保证</p>-->

                        <#--<p class="adv_eng">完善的客户服务跟踪系统</p>-->
                        <#--<ul>-->
                            <#--<li>利用互联网技术及优势，建立健全客户服务跟踪系统，并通过多维度系统性分析，优化管理与服务，提升客户满意度和口碑转化。</li>-->
                        <#--</ul>-->
                    <#--</div>-->
                </li>
                <li class="adv_sep"></li>
                <li>
                    <div class="join_adv_bk">
                        <img src="${BASE_PATH}/resources/images/join/join_advantage_4.png">
                    </div>
                    <div class="join_adv_subtitle">
                        <p class="adv_cn">人才培养</p>

                        <p class="adv_eng">定期培养与择优并举，聚合行业精英</p>
                    </div>
                    <#--<div class="join_adv_detail">-->
                        <#--<p class="adv_cn">人才培养</p>-->

                        <#--<p class="adv_eng">定向培养与择优并举，聚合行业精英</p>-->
                        <#--<ul>-->
                            <#--<li>大专院校合作：定向人才培养，为我所用；</li>-->
                            <#--<li>专项培训支持：淘汽云修提供管理及技术培训和辅助支持；</li>-->
                            <#--<li>行业精英聚合：依托淘汽云修品牌势能，聚合行业优秀人才。</li>-->
                        <#--</ul>-->
                    <#--</div>-->
                </li>
            </ul>
        </div>
    </div>
    <div class="join_conditions_box merchants-box" id="join_conditions">
        <div class="wrapper">
            <div class="subHead conditions_subhead mb-74">
                <h2 class="cn">合作条件</h2>

                <h2 class="eng">Join Conditions</h2>
            </div>
            <ul class="join_cond_list">
                <li class="mr-10">
                    <img class="join_cond_img" src="${BASE_PATH}/resources/images/join/join_conditions_3.jpg">

                    <div class="join_cond_info">
                        <h2 class="join_cond_subtitle">认同与共赢</h2>

                        <p class="cond_summary">有志于合作淘汽档口合作连锁体系的经营，认同合作品牌文化，愿意为之推广，遵守相关规定并有一定经济实力的投资者</p>
                    </div>
                </li>
                <li class="mr-10">
                    <img class="join_cond_img" src="${BASE_PATH}/resources/images/join/join_conditions_4.jpg">

                    <div class="join_cond_info">
                        <h2 class="join_cond_subtitle">自备经营场所</h2>

                        <p class="cond_summary">非自有店面欲加入淘汽档口合作者，承租期限不低于3年</p>
                    </div>
                </li>
                <li>
                    <img class="join_cond_img" src="${BASE_PATH}/resources/images/join/join_conditions_5.jpg">

                    <div class="join_cond_info">
                        <h2 class="join_cond_subtitle">人脉与团队</h2>

                        <p class="cond_summary">开店区域内，良好的人脉资源影响力和独立的淘汽档口合作店服务团队</p>
                    </div>
                </li>
            <#--<li class="mr-10">-->
            <#--<img class="join_cond_img" src="${BASE_PATH}/resources/images/join/join_conditions_1.jpg">-->

            <#--<div class="join_cond_info">-->
            <#--<h2 class="join_cond_subtitle">合作服务费</h2>-->

            <#--<p class="cond_item">专业店：5万</p>-->

            <#--<p class="cond_item">标准店：15万</p>-->

            <#--<p class="cond_item">中心店：30万</p>-->
            <#--</div>-->
            <#--</li>-->
            <#--<li class="mr-10">-->
            <#--<img class="join_cond_img" src="${BASE_PATH}/resources/images/join/join_conditions_2.jpg">-->

            <#--<div class="join_cond_info">-->
            <#--<h2 class="join_cond_subtitle">服务质量保证金</h2>-->

            <#--<p class="cond_item">专业店：1万</p>-->

            <#--<p class="cond_item">标准店：5万</p>-->

            <#--<p class="cond_item">中心店：10万</p>-->
            <#--</div>-->
            <#--</li>-->
                <li class="mr-10">
                    <img class="join_cond_img" src="${BASE_PATH}/resources/images/join/join_conditions_6.jpg">

                    <div class="join_cond_info">
                        <h2 class="join_cond_subtitle">良好的信誉</h2>

                        <p class="cond_summary">具备良好的个人资信和商誉公信力，服从淘汽档口合作总部管理，与淘汽档口合作团队建立良好的合作沟通，并完成年度销售额</p>
                    </div>
                </li>
                <li class="mr-10">
                    <img class="join_cond_img" src="${BASE_PATH}/resources/images/join/join_conditions_7.jpg">

                    <div class="join_cond_info">
                        <h2 class="join_cond_subtitle">拓展市场</h2>

                        <p class="cond_summary">清楚市场拓展不仅仅靠合作总部提供，还要经营者用心去开拓</p>
                    </div>
                </li>
                <li>
                    <img class="join_cond_img" src="${BASE_PATH}/resources/images/join/join_conditions_8.jpg">

                    <div class="join_cond_info">
                        <h2 class="join_cond_subtitle">经营能力</h2>

                        <p class="cond_summary">具备一定的经营分析能力、抗压能力，开店需要过程和周期</p>
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <div class="join_process_box merchants-box" id="joining_process">
        <div class="wrapper">
            <div class="subHead mb-74">
                <h2 class="cn">合作流程</h2>

                <h2 class="eng">Joining process</h2>
            </div>
            <div class="join_process_content">
                <div class="process_img">
                    <img src="${BASE_PATH}/resources/images/join/join_process_1.jpg">

                    <div class="process_step p_step1">
                        <p class="step_num">1</p>

                        <p class="step_cn">第一步</p>
                    </div>
                </div>
                <ul class="process_info p_info1 step_1">
                    <li>
                        <label><em class="point"></em>投资咨询</label>

                        <p>通过电话、传真、网上留言等方式向淘汽档口合作总部初步了解淘汽档口项目信息</p>
                    </li>
                    <li>
                        <label><em class="point"></em>签订意向书并交纳意向金</label>

                        <p>在后期审核中双方如果对合作持否定意见，意向金可以退还</p>
                    </li>
                    <li>
                        <label><em class="point"></em>实地考察</label>

                        <p>到淘汽档口合作总部进行项目考察，参观门店、确认项目、提交申请</p>
                    </li>
                </ul>
            </div>
            <div class="join_process_content">
                <ul class="process_info p_info2 step_2">
                    <li>
                        <label>资格审核<em class="point"></em></label>

                        <p>淘汽档口合作总部对投资者进行审核，确认投资者的合作资格</p>
                    </li>
                    <li>
                        <label>签订合同<em class="point"></em></label>

                        <p>双方确定考察结果无争议，正式签订淘汽档口合作合同</p>
                    </li>
                    <li>
                        <label>缴纳费用<em class="point"></em></label>

                        <p>合作商按所选择的投资类型向总部缴纳淘汽档口合作费等相关费用</p>
                    </li>
                </ul>
                <div class="process_img">
                    <img src="${BASE_PATH}/resources/images/join/join_process_2.jpg">

                    <div class="process_step p_step2">
                        <p class="step_num">2</p>

                        <p class="step_cn">第二步</p>
                    </div>
                </div>
            </div>
            <div class="join_process_content">
                <div class="process_img">
                    <img src="${BASE_PATH}/resources/images/join/join_process_3.jpg">

                    <div class="process_step p_step1">
                        <p class="step_num">3</p>

                        <p class="step_cn">第三步</p>
                    </div>
                </div>
                <ul class="process_info p_info1 step_3">
                    <li>
                        <label><em class="point"></em>店面装修</label>

                        <p>淘汽云修为合作店提供淘汽档口合作店装修指导、设计指导以及形象装修支持</p>
                    </li>
                    <li>
                        <label><em class="point"></em>系统安装及数据导入</label>

                        <p>淘汽云修自主开发的软件管理系统，需将客户原有客户信息进行导入，并帮助新客户进行引流管理</p>
                    </li>
                    <li>
                        <label><em class="point"></em>总部培训</label>

                        <p>总部安排投资者进行技术培训(标准操作流程SOP)、管理培训（维修厂8S管理）、店经理、销售及技师培训等</p>
                    </li>
                </ul>
            </div>
            <div class="join_process_content">
                <ul class="process_info p_info2 step_4">
                    <li>
                        <label>货物配送<em class="point"></em></label>

                        <p>优先货物和物料配送，淘汽档口合作总部协助投资人试营业，提供营销方案</p>
                    </li>
                    <li>
                        <label>试营业<em class="point"></em></label>

                        <p>人员招聘，服务推广等</p>
                    </li>
                </ul>
                <div class="process_img">
                    <img src="${BASE_PATH}/resources/images/join/join_process_4.jpg">

                    <div class="process_step p_step2">
                        <p class="step_num">4</p>

                        <p class="step_cn">第四步</p>
                    </div>
                </div>
            </div>

        </div>
    </div>
</script>
<script id="commercialVehicleTpl" type="text/html">
    <div class="wrapper_other merchants-box join_mode_other" id="join_mode">
        <div class="subHead">
            <h2 class="cn">合作模式</h2>

            <h2 class="eng">Join Mode</h2>
        </div>
        <div class="container">
            <ul class="join_mode">
                <li>
                    <h3 class="sublittle">快保店</h3>

                    <p class="mode_summary">县／县级市／地级市／大中城市<br/>同品牌间隔：2公里</p>
                    <ul class="store_requirement">
                        <li>面积：3000m<sup>2</sup>以上</li>
                        <li>工位：2个及以上</li>
                        <li>年营业额：300万以上</li>
                        <li>位置及环境：店面形象好，交通便利（主干道路边／近小区／单位）</li>
                        <li>员工要求：良好服务理念，良好品牌经营意识，高级技师2人以上，服务接待＋配件销售1人</li>
                    </ul>
                </li>
                <li>
                    <h3 class="sublittle">服务店</h3>

                    <p class="mode_summary">县级市／地级市／大中城市<br/>物流主干道／物流集散地<br/>服务半径：50公里</p>
                    <ul class="store_requirement">
                        <li>面积：6000m<sup>2</sup>以上；其中配件仓库100m<sup>2</sup></li>
                        <li>工位：4个及以上</li>
                        <li>年营业额：800万以上</li>
                        <li>位置及环境：店面形象好、服务环境好、交通便利。</li>
                        <li>员工要求：良好服务理念，良好品牌经营意识，有创建服务网络的意愿，高级技师5人以上，服务接待1人，配件销售1人</li>
                    </ul>
                </li>
                <li style="margin-right: 0">
                    <h3 class="sublittle">旗舰店</h3>

                    <p class="mode_summary">地级以上城市／中心城市<br/>物流集散地<br/>服务半径：100公里</p>
                    <ul class="store_requirement">
                        <li>面积：9000m<sup>2</sup>以上；其中配件仓库300m<sup>2</sup></li>
                        <li>工位：8个及以上</li>
                        <li>年营业额：2000万以上</li>
                        <li>位置及环境：4s店的建店标准，店面形象优秀，业内影响力大，客户服务环境好</li>
                        <li>员工要求：良好服务理念，经营者具备良好的品牌经营意识，并具清晰发展思路，高级技师8人以上，服务接待1人，配件销售1人</li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>

    <div class="join_advantage_box merchants-box" id="join_advantage">
        <div class="wrapper">
            <div class="subHead mb-74">
                <h2 class="cn">合作优势</h2>

                <h2 class="eng">Join Advantage</h2>
            </div>
            <ul class="join_adv_list">
                <li>
                    <div class="join_adv_bk">
                        <img style="margin-top: 10px" src="${BASE_PATH}/resources/images/join/join_advantage_5.png">
                    </div>
                    <div class="join_adv_subtitle">
                        <p class="adv_cn">品牌信誉</p>
                        <p class="adv_eng">“淘汽云修”全球汽车人连锁品牌</p>
                    </div>
                    <div class="join_adv_detail">
                        <p class="adv_cn">品牌信誉</p>
                        <p class="adv_eng">“淘汽云修”全球汽车人连锁品牌</p>
                        <ul>
                            <li>淘汽云修将投入5亿巨资，打造最强大服务平台；</li>
                            <li>广告、公关、地推等全方位出击，打造最强势服务品牌；</li>
                            <li>淘汽云修未来两年内将合作店扩大至20000家；</li>
                            <li>首批合作者，均可免费使用淘汽云修品牌。</li>
                        </ul>
                    </div>
                </li>
                <li class="adv_sep"></li>
                <li>
                    <div class="join_adv_bk">
                        <img src="${BASE_PATH}/resources/images/join/join_advantage_1.png">
                    </div>
                    <div class="join_adv_subtitle">
                        <p class="adv_cn">品质保证</p>
                        <p class="adv_eng">与全球数千家品牌制造商及直供代理合作</p>
                    </div>
                    <div class="join_adv_detail">
                        <p class="adv_cn">品质保证</p>
                        <p class="adv_eng">与全球数千家品牌制造商及直供代理合作</p>
                        <ul>
                            <li>集中采购价格更具优势，确保同区域最低，不惧价格战；</li>
                            <li>保证所有进货品质和质量，承诺质量问题退货换货；</li>
                            <li>淘汽云修自建仓储物流系统，确保最快捷、最省心。</li>
                        </ul>
                    </div>
                </li>
                <li class="adv_sep"></li>
                <li>
                    <div class="join_adv_bk">
                        <img src="${BASE_PATH}/resources/images/join/join_advantage_3.png">
                    </div>
                    <div class="join_adv_subtitle">
                        <p class="adv_cn">系统支持</p>

                        <p class="adv_eng">“云”管理系统平台 量身定制的门店运营后台</p>
                    </div>
                    <div class="join_adv_detail">
                        <p class="adv_cn">系统支持</p>

                        <p class="adv_eng">“云”管理系统平台 量身定制的门店运营后台</p>
                        <ul>
                            <li>斥资5千万人民币开发“云”管理系统，专业功能赶超品牌4S店；</li>
                            <li>合作店可免费使用，且提供培训指导、营销管理等诸多功能； </li>
                            <li>大数据客情分析，可根据维修情况预测下次维修项目及节点等；</li>
                            <li>大数据仓储管理，可清晰准确掌握仓储情况，高效有序采购下单；</li>
                            <li>维修服务监控，管理人员可远程监控车辆维修，以促进管理升级、服务升级、效率升级及操作规范升级。</li>
                        </ul>
                    </div>
                </li>
                <li class="adv_sep"></li>
                <li>
                    <div class="join_adv_bk">
                        <img src="${BASE_PATH}/resources/images/join/join_advantage_2.png">
                    </div>
                    <div class="join_adv_subtitle">
                        <p class="adv_cn">客源渠道</p>

                        <p class="adv_eng">多渠道引入客源 以品牌及规模优势拓展客群</p>
                    </div>
                    <div class="join_adv_detail">
                        <p class="adv_cn">客源渠道</p>

                        <p class="adv_eng">多渠道引入客源 以品牌及规模优势拓展客群</p>
                        <ul>
                            <li>以大规模广宣推广，聚焦关注，增加客源线索；</li>
                            <li>保险公司等优势资源深度合作，引入客源；</li>
                            <li>规模效应 & 互联网手段客源分流与导流，扩大客群。</li>
                        </ul>
                    </div>
                </li>
                <li>
                    <div class="join_adv_bk">
                        <img style="margin-top: 15px" src="${BASE_PATH}/resources/images/join/join_advantage_6.png">
                    </div>
                    <div class="join_adv_subtitle">
                        <p class="adv_cn">技术保障</p>

                        <p class="adv_eng">提供完善的技术培训支持保障</p>
                    </div>
                    <div class="join_adv_detail">
                        <p class="adv_cn">技术保障</p>

                        <p class="adv_eng">提供完善的技术培训支持保障</p>
                        <ul>
                            <li>提供各类专项技术支持及定期培训；</li>
                            <li>提供远程技术咨询及服务支持；</li>
                            <li>车主定向培训及上门知识普及。</li>
                        </ul>
                    </div>
                </li>
                <li class="adv_sep"></li>
                <li>
                    <div class="join_adv_bk">
                        <img style="margin-top: 10px" src="${BASE_PATH}/resources/images/join/join_advantage_7.png">
                    </div>
                    <div class="join_adv_subtitle">
                        <p class="adv_cn">营销支持</p>

                        <p class="adv_eng">提升销量的解决方案 庞大资源的营销方案</p>
                    </div>
                    <div class="join_adv_detail">
                        <p class="adv_cn">营销支持</p>

                        <p class="adv_eng">提升销量的解决方案 庞大资源的营销方案</p>
                        <ul>
                            <li></li>
                            <li></li>
                        </ul>
                    </div>
                </li>
                <li class="adv_sep"></li>
                <li>
                    <div class="join_adv_bk">
                        <img style="margin-top: 12px" src="${BASE_PATH}/resources/images/join/join_advantage_8.png">
                    </div>
                    <div class="join_adv_subtitle">
                        <p class="adv_cn">创新支持</p>

                        <p class="adv_eng">适合新时代的项目</p>
                    </div>
                    <div class="join_adv_detail">
                        <p class="adv_cn">创新支持</p>

                        <p class="adv_eng">适合新时代的项目</p>
                        <ul>
                            <li></li>
                        </ul>
                    </div>
                </li>
                <li class="adv_sep"></li>
                <li>
                    <div class="join_adv_bk">
                        <img src="${BASE_PATH}/resources/images/join/join_advantage_4.png">
                    </div>
                    <div class="join_adv_subtitle">
                        <p class="adv_cn">人才培养</p>

                        <p class="adv_eng">定期培养与择优并举，聚合行业精英</p>
                    </div>
                    <div class="join_adv_detail">
                        <p class="adv_cn">人才培养</p>

                        <p class="adv_eng">定向培养与择优并举，聚合行业精英</p>
                        <ul>
                            <li>大专院校合作：定向人才培养，为我所用；</li>
                            <li>专项培训支持：淘汽云修提供管理及技术培训和辅助支持；</li>
                            <li>行业精英聚合：依托淘汽云修品牌势能，聚合行业优秀人才。</li>
                        </ul>
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <div class="join_conditions_box merchants-box" id="join_conditions">
        <div class="wrapper">
            <div class="subHead conditions_subhead mb-74">
                <h2 class="cn">合作条件</h2>

                <h2 class="eng">Join Conditions</h2>
            </div>
            <ul class="join_cond_list">
                <li class="mr-10">
                    <img class="join_cond_img" src="${BASE_PATH}/resources/images/join/join_conditions_3.jpg">

                    <div class="join_cond_info">
                        <h2 class="join_cond_subtitle">认同与共赢</h2>

                        <p class="cond_summary">有志于合作淘汽档口合作连锁体系的经营，认同合作品牌文化，愿意为之推广，遵守相关规定并有一定经济实力的投资者</p>
                    </div>
                </li>
                <li class="mr-10">
                    <img class="join_cond_img" src="${BASE_PATH}/resources/images/join/join_conditions_4.jpg">

                    <div class="join_cond_info">
                        <h2 class="join_cond_subtitle">自备经营场所</h2>

                        <p class="cond_summary">非自有店面欲加入淘汽档口合作者，承租期限不低于3年</p>
                    </div>
                </li>
                <li>
                    <img class="join_cond_img" src="${BASE_PATH}/resources/images/join/join_conditions_5.jpg">

                    <div class="join_cond_info">
                        <h2 class="join_cond_subtitle">人脉与团队</h2>

                        <p class="cond_summary">开店区域内，良好的人脉资源影响力和独立的淘汽档口合作店服务团队</p>
                    </div>
                </li>
            <#--<li class="mr-10">-->
            <#--<img class="join_cond_img" src="${BASE_PATH}/resources/images/join/join_conditions_1.jpg">-->

            <#--<div class="join_cond_info">-->
            <#--<h2 class="join_cond_subtitle">合作服务费</h2>-->

            <#--<p class="cond_item">专业店：5万</p>-->

            <#--<p class="cond_item">标准店：15万</p>-->

            <#--<p class="cond_item">中心店：30万</p>-->
            <#--</div>-->
            <#--</li>-->
            <#--<li class="mr-10">-->
            <#--<img class="join_cond_img" src="${BASE_PATH}/resources/images/join/join_conditions_2.jpg">-->

            <#--<div class="join_cond_info">-->
            <#--<h2 class="join_cond_subtitle">服务质量保证金</h2>-->

            <#--<p class="cond_item">专业店：1万</p>-->

            <#--<p class="cond_item">标准店：5万</p>-->

            <#--<p class="cond_item">中心店：10万</p>-->
            <#--</div>-->
            <#--</li>-->
                <li class="mr-10">
                    <img class="join_cond_img" src="${BASE_PATH}/resources/images/join/join_conditions_6.jpg">

                    <div class="join_cond_info">
                        <h2 class="join_cond_subtitle">良好的信誉</h2>

                        <p class="cond_summary">具备良好的个人资信和商誉公信力，服从淘汽档口合作总部管理，与淘汽档口合作团队建立良好的合作沟通，并完成年度销售额</p>
                    </div>
                </li>
                <li class="mr-10">
                    <img class="join_cond_img" src="${BASE_PATH}/resources/images/join/join_conditions_7.jpg">

                    <div class="join_cond_info">
                        <h2 class="join_cond_subtitle">拓展市场</h2>

                        <p class="cond_summary">清楚市场拓展不仅仅靠合作总部提供，还要经营者用心去开拓</p>
                    </div>
                </li>
                <li>
                    <img class="join_cond_img" src="${BASE_PATH}/resources/images/join/join_conditions_8.jpg">

                    <div class="join_cond_info">
                        <h2 class="join_cond_subtitle">经营能力</h2>

                        <p class="cond_summary">具备一定的经营分析能力、抗压能力，开店需要过程和周期</p>
                    </div>
                </li>
            </ul>
        </div>
    </div>
    <div class="join_process_box merchants-box" id="joining_process">
        <div class="wrapper">
            <div class="subHead mb-74">
                <h2 class="cn">合作流程</h2>

                <h2 class="eng">Joining process</h2>
            </div>
            <div class="join_process_content">
                <div class="process_img">
                    <img src="${BASE_PATH}/resources/images/join/join_process_1_2.jpg">

                    <div class="process_step p_step1">
                        <p class="step_num">1</p>

                        <p class="step_cn">第一步</p>
                    </div>
                </div>
                <ul class="process_info p_info1 step_1">
                    <li>
                        <label><em class="point"></em>投资咨询</label>

                        <p>通过电话、传真、网上留言等方式向淘汽档口合作总部初步了解淘汽档口项目信息</p>
                    </li>
                    <li>
                        <label><em class="point"></em>签订意向书并交纳意向金</label>

                        <p>在后期审核中双方如果对合作持否定意见，意向金可以退还</p>
                    </li>
                    <li>
                        <label><em class="point"></em>实地考察</label>

                        <p>到淘汽档口合作总部进行项目考察，参观门店、确认项目、提交申请</p>
                    </li>
                </ul>
            </div>
            <div class="join_process_content">
                <ul class="process_info p_info2 step_2">
                    <li>
                        <label>资格审核<em class="point"></em></label>

                        <p>淘汽档口合作总部对投资者进行审核，确认投资者的合作资格</p>
                    </li>
                    <li>
                        <label>签订合同<em class="point"></em></label>

                        <p>双方确定考察结果无争议，正式签订淘汽档口合作合同</p>
                    </li>
                    <li>
                        <label>缴纳费用<em class="point"></em></label>

                        <p>合作商按所选择的投资类型向总部缴纳淘汽档口合作费等相关费用</p>
                    </li>
                </ul>
                <div class="process_img">
                    <img src="${BASE_PATH}/resources/images/join/join_process_2_2.jpg">

                    <div class="process_step p_step2">
                        <p class="step_num">2</p>

                        <p class="step_cn">第二步</p>
                    </div>
                </div>
            </div>
            <div class="join_process_content">
                <div class="process_img">
                    <img src="${BASE_PATH}/resources/images/join/join_process_3_2.jpg">

                    <div class="process_step p_step1">
                        <p class="step_num">3</p>

                        <p class="step_cn">第三步</p>
                    </div>
                </div>
                <ul class="process_info p_info1 step_3">
                    <li>
                        <label><em class="point"></em>店面装修</label>

                        <p>淘汽云修为合作店提供淘汽档口合作店装修指导、设计指导以及形象装修支持</p>
                    </li>
                    <li>
                        <label><em class="point"></em>系统安装及数据导入</label>

                        <p>淘汽云修自主开发的软件管理系统，需将客户原有客户信息进行导入，并帮助新客户进行引流管理</p>
                    </li>
                    <li>
                        <label><em class="point"></em>总部培训</label>

                        <p>总部安排投资者进行技术培训(标准操作流程SOP)、管理培训（维修厂8S管理）、店经理、销售及技师培训等</p>
                    </li>
                </ul>
            </div>
            <div class="join_process_content">
                <ul class="process_info p_info2 step_4">
                    <li>
                        <label>货物配送<em class="point"></em></label>

                        <p>优先货物和物料配送，淘汽档口合作总部协助投资人试营业，提供营销方案</p>
                    </li>
                    <li>
                        <label>试营业<em class="point"></em></label>

                        <p>人员招聘，服务推广等</p>
                    </li>
                </ul>
                <div class="process_img">
                    <img src="${BASE_PATH}/resources/images/join/join_process_4_2.jpg">

                    <div class="process_step p_step2">
                        <p class="step_num">4</p>

                        <p class="step_cn">第四步</p>
                    </div>
                </div>
            </div>

        </div>
    </div>
</script>
<#include "layout/portal-footer.ftl">
<script type="text/javascript" src="${BASE_PATH}/resources/js/portal/invest/index.js?d6ec43027a1be22e7cca97a2d92ac716"></script>