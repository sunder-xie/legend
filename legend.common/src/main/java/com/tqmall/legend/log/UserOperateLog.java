package com.tqmall.legend.log;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.tqmall.wheel.lang.Langs;
import com.tqmall.wheel.utils.DateFormatUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by xin on 2017/5/9.
 * 系统访问日志Bean
 */
public class UserOperateLog {

    /**
     * 全站唯一的来源key值
     */
    private String refer;

    /**
     * 下一步触发的来源值（refer）
     */
    private String target;

    /**
     * 门店id
     */
    private Long shop_id;

    /**
     * 用户id
     */
    private Long user_id;

    /**
     * 描述
     */
    private String remark;

    /**
     * 扩展参数
     */
    private UserOperateParams params;

    private UserOperateLog() {}


    public static class UserOperateLogBuild {
        private UserOperateLog userOperateLog = new UserOperateLog();

        public UserOperateLogBuild buildRefer(String refer) {
            userOperateLog.refer = refer;
            return this;
        }

        public UserOperateLogBuild buildTarget(String target) {
            userOperateLog.target = target;
            return this;
        }

        public UserOperateLogBuild buildShopId(Long shop_id) {
            userOperateLog.shop_id = shop_id;
            return this;
        }

        public UserOperateLogBuild buildUserId(Long user_id) {
            userOperateLog.user_id = user_id;
            return this;
        }

        public UserOperateLogBuild buildRemark(String remark) {
            userOperateLog.remark = remark;
            return this;
        }

        public UserOperateLogBuild buildParams(List<String> condition, Map<String, Object> paramMap) {
            userOperateLog.params = new UserOperateParams(condition, paramMap);
            return this;
        }

        public String build() {
            StringBuilder logSb = new StringBuilder();
            logSb.append("[refer:");
            if (Langs.isNotBlank(userOperateLog.refer)) {
                logSb.append(userOperateLog.refer);
            }
            logSb.append("]");
            logSb.append("[target:");
            if (Langs.isNotBlank(userOperateLog.target)) {
                logSb.append(userOperateLog.target);
            }
            logSb.append("]");
            logSb.append("[shop_id:");
            if (userOperateLog.shop_id != null) {
                logSb.append(userOperateLog.shop_id);
            }
            logSb.append("]");
            logSb.append("[user_id:");
            if (userOperateLog.user_id != null) {
                logSb.append(userOperateLog.user_id);
            }
            logSb.append("]");
            logSb.append("[date:").append(DateFormatUtils.toYMDHMS(new Date())).append("]");
            logSb.append("[remark:");
            if (Langs.isNotBlank(userOperateLog.remark)) {
                logSb.append(userOperateLog.remark);
            }
            logSb.append("]");
            logSb.append("[params:");
            if (userOperateLog.params != null && (Langs.isNotEmpty(userOperateLog.params.getCondition()) || Langs.isNotEmpty(userOperateLog.params.getParamMap()))) {
                Map<String, Object> params = Maps.newLinkedHashMap();
                if (Langs.isNotEmpty(userOperateLog.params.getCondition())) {
                    params.put("condition", userOperateLog.params.getCondition());
                }
                if (Langs.isNotEmpty(userOperateLog.params.getParamMap())) {
                    params.putAll(userOperateLog.params.getParamMap());
                }
                logSb.append(JSON.toJSONString(params));
            }
            logSb.append("]");
            return logSb.toString();
        }
    }


}
