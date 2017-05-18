package com.tqmall.legend.object.param.activity;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

import java.util.Date;

/**
 * Created by wushuai on 16/07/28.
 */
@Data
public class ActivityTemplateBean extends BaseRpcParam {

    private static final long serialVersionUID = 5669722255736139891L;

    private Long id;//活动模板id
    private Date gmtCreate;
    private Date gmtModified;

    private String actName;//活动名称
    private String keywords;//活动关键字(标签)
    private String summary;//活动摘要
    private String remark;//活动详细描述
    private String imgUrl;//活动banner图url
    private Date startTime;//活动开始时间
    private Date endTime;//活动结束时间
    private Integer actStatus;//活动状态:0关闭,1草稿,2发布
    private String agreement;//活动协议
    private Integer isNeedAudit;//是否需要审核:0不需要,1需要
    private Integer actType;//活动模板类型，1报名参加，2强制参加
    private Integer actScope;//活动范围:0全部门店都展示,1具体到门店,2市级范围
    private Long sort;//排序，默认降序
    private Long channel;//活动渠道
    private String byName;//活动开服务单入口别名
    private String pageTitle;//页面title
    private Integer isShowHms;//是否显示时分秒
    private String operatorRemark;//运营备注，仅供运营查看
    private Integer isNeedReimbursed;//是否需要核销,0需要，1不需要
    private String articleImg;//图文消息的图片
    private String articleTitle;//图文消息的标题
    private String articleDesc;//图文消息的描述
}
