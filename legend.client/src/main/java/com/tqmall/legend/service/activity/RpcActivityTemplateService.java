package com.tqmall.legend.service.activity;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.activity.ActivityTemplateBean;
import com.tqmall.legend.object.param.activity.ActivityTemplateParam;
import com.tqmall.legend.object.param.activity.ActivityTemplateScopeRelParam;
import com.tqmall.legend.object.result.activity.ActivityTemplateDTO;
import com.tqmall.legend.object.result.activity.ActivityTemplatePageDTO;
import com.tqmall.legend.object.result.appoint.ShopServiceDTO;
import com.tqmall.legend.object.result.service.ServiceTemplateDTO;
import com.tqmall.legend.object.result.service.ShopServiceInfoDTO;

import java.util.List;

/**
 * Created by zsy on 16/5/30.
 */
public interface RpcActivityTemplateService {
    /**
     * 根据模板活动id获取活动详情
     *
     * @param source
     * @param actTplId
     * @return
     */
    public Result<ActivityTemplateDTO> getActTplInfoByActTplId(String source,Long actTplId);

    /**
     * 根据模板活动id获取模板服务list
     *
     * @param source
     * @param actTplId
     * @return
     */
    public Result<List<ServiceTemplateDTO>> getServiceTplListByActTplId(String source,Long actTplId);

    /**
     * 根据模板活动id、uc的shopId获取门店参加报名的服务列表
     * @param source
     * @param actTplId
     * @param ucShopId
     * @return
     */
    public Result<List<ShopServiceInfoDTO>> getShopServiceInfo(String source,Long actTplId,Integer ucShopId);

    /**
     * 根据模板活动id获取参加此活动的门店
     *
     * @param source
     * @param actTplId
     * @return
     */
    public Result<List<ShopServiceDTO>> getShopByActTplId(String source,Long actTplId,Integer city);

    /**
     * 新增或更新活动模版
     * @param activityTemplateBean
     * @return
     */
    public Result<ActivityTemplateBean> saveActivityTemplate(ActivityTemplateBean activityTemplateBean);

    /**
     * 删除活动模版
     * @param activityTemplateBean
     * @return
     */
    public Result<String> delActivityTemplate(ActivityTemplateBean activityTemplateBean);

    /**
     * 查询活动模版列表
     * @param activityTemplateParam
     * @return
     */
    public Result<ActivityTemplatePageDTO> qryActivityTemplatePage(ActivityTemplateParam activityTemplateParam);

    /**
     * 新增活动模板范围
     * @param param
     * @return
     */
    public Result<String> addActivityTemplateScopeRel(ActivityTemplateScopeRelParam param);

    /**
     * 保存活动模版和服务模版关系(根据模版id全量编辑)
     * <p style='color:red'>注意:会根据服务模版删除此模版和服务模版的全部关系再新增当前的关系</p>
     * @param actTplId
     * @param serviceTplIds
     * @return
     */
    public Result<String> saveActivityTemplateServiceRel(Long actTplId,List<Long> serviceTplIds);
}
