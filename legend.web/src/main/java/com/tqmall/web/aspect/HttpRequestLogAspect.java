package com.tqmall.web.aspect;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.annotation.Condition;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.annotation.Param;
import com.tqmall.legend.entity.sys.UserOperateDict;
import com.tqmall.legend.facade.sys.UserOperateDictFacade;
import com.tqmall.legend.log.UserOperateLog;
import com.tqmall.wheel.lang.Langs;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * http请求切面类
 * Created by wushuai on 17/3/2.
 */
@Aspect
@Component
public class HttpRequestLogAspect {

    protected Logger logger = LoggerFactory.getLogger(HttpRequestLogAspect.class);
    @Autowired
    private UserOperateDictFacade userOperateDictFacade;

    /**
     * 环绕通知
     *
     * @param proceedingJoinPoint
     */
    public Object httpRequestLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object object = proceedingJoinPoint.proceed();
        Long shopId = null;
        Long userId = null;
        String userName = null;
        String reqUri = null;
        String querySring = null;

        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            UserInfo userInfo = UserUtils.getUserInfo(request);
            shopId = userInfo.getShopId();
            userId = userInfo.getUserId();
            userName = userInfo.getName();
            reqUri = request.getRequestURI();
            querySring = request.getQueryString();

            Map<String, Object> searchParams = getSearchParam(request);
            // 根据url查refer
            Object referObj = searchParams.get("refer");
            if (referObj != null) {
                String refer = (String) referObj;
                reqUri += "?refer=" + refer;
            }

            UserOperateDict userOperateDict = userOperateDictFacade.getByRefer(reqUri);
            if(userOperateDict != null){
                String methodName = proceedingJoinPoint.getSignature().getName();
                Class<?> classTarget = proceedingJoinPoint.getTarget().getClass();
                Class[] parameterTypes = ((MethodSignature) proceedingJoinPoint.getSignature()).getParameterTypes();
                Method method = classTarget.getMethod(methodName, parameterTypes);
                HttpRequestLog httpRequestLog = method.getAnnotation(HttpRequestLog.class);

                List<String> conditionList = parseConditioin(httpRequestLog.conditions(), searchParams);
                Map<String, Object> paramMap = parseParam(httpRequestLog.params(), searchParams);

                UserOperateLog.UserOperateLogBuild logBuild = new UserOperateLog.UserOperateLogBuild();
                String log = logBuild.buildRefer(userOperateDict.getReferKey())
                        .buildTarget(userOperateDict.getTargetKey())
                        .buildShopId(shopId)
                        .buildUserId(userId)
                        .buildRemark(userOperateDict.getOperateRemark())
                        .buildParams(conditionList, paramMap)
                        .build();
                logger.info(log);
            }
        } catch (Exception e) {
            logger.error("[门店访问记录]记录日志失败,门店ID:{},操作人ID:{},操作人姓名:{},操作URL:{},请求参数:{},异常信息", shopId, userId, userName, reqUri, querySring, e);
        } finally {
            return object;
        }
    }

    private Map<String, Object> getSearchParam(HttpServletRequest request) {
        Map<String, Object> searchParams = Maps.newHashMap();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = request.getParameter(name);
            if (Langs.isNotBlank(value)) {
                searchParams.put(name, value);
            }
        }
        return searchParams;
    }

    private List<String> parseConditioin(Condition[] conditions, Map<String, Object> searchParams) {
        List<String> conditionList = Lists.newArrayList();
        for (Condition condition : conditions) {
            String name = condition.name();
            String aliasName = condition.aliasName();
            String printName = name;
            if (Langs.isNotBlank(aliasName)) {
                printName = aliasName;
            }
            if (Langs.isNotBlank(name)) {
                Object value = searchParams.get(name);
                if (value != null) {
                    conditionList.add(printName);
                }
            }
        }
        return conditionList;
    }

    private Map<String, Object> parseParam(Param[] params, Map<String, Object> searchParams) {
        Map<String, Object> paramMap = Maps.newHashMap();
        for (Param param : params) {
            String name = param.name();
            String defaultValue = param.defaultValue();
            String aliasName = param.aliasName();
            String printName = name;
            if (Langs.isNotBlank(aliasName)) {
                printName = aliasName;
            }
            if (Langs.isNotBlank(name)) {
                Object value = searchParams.get(name);
                if (value != null) {
                    paramMap.put(printName, value);
                } else {
                    if (Langs.isNotBlank(defaultValue)) {
                        paramMap.put(printName, defaultValue);
                    }
                }
            }
        }
        return paramMap;
    }

}
