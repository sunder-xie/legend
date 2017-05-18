package com.tqmall.legend.web.feedback;

import com.google.common.collect.Maps;
import com.tqmall.common.Constants;
import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.feedback.FeedbackAnswerService;
import com.tqmall.legend.biz.feedback.FeedbackService;
import com.tqmall.legend.common.LegendError;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.feedback.Feedback;
import com.tqmall.legend.entity.feedback.FeedbackAnswer;
import com.tqmall.legend.entity.feedback.FeedbackModuleEnum;
import com.tqmall.legend.enums.feedback.AnswerTypeEnum;
import com.tqmall.legend.entity.feedback.TqmallFeedbackModuleEnum;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by lilige on 16/7/18.
 */
@Slf4j
@Controller
@RequestMapping("shop/help/feedback")
public class FeedbackController extends BaseController {
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private FeedbackAnswerService feedbackAnswerService;

    /**
     * 问题反馈列表页
     *
     * @return
     */
    @RequestMapping("feedback-list")
    public String feedbackList(){
        return "yqx/page/help/feedback/feedback-list";
    }

    /**
     * 获取全部的模块列表
     * @return
     */
    @RequestMapping("modules")
    @ResponseBody
    public Result getModule(){
        Integer shopLevel = UserUtils.getShopLevelForSession(request);
        List<String> modules;
        //档口版和云修版反馈内容区分
        if(shopLevel.equals(Constants.SHOP_LEVEL_TQMALL_VERSION)){
            modules = TqmallFeedbackModuleEnum.getModuleNames();
        }else{
            modules = FeedbackModuleEnum.getModuleNames();
        }
        return Result.wrapSuccessfulResult(modules);
    }

    /**
     * 添加一条反馈纪录
     * @param feedback
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public Result saveFeedback(Feedback feedback){
        if(feedback == null){
            return Result.wrapErrorResult(LegendError.PARAM_ERROR);
        }
        if(StringUtils.isBlank(feedback.getFeedbackContent())){
            return Result.wrapErrorResult("","请输入反馈内容");
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        feedback.setShopId(userInfo.getShopId());
        feedback.setCreator(userInfo.getUserId());
        try {
            Result result = feedbackService.saveFeedback(feedback);
            return result;
        } catch (Exception e) {
            log.error("【保存门店问题反馈】出现异常",e);
        }
        return Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(),"反馈保存失败，请稍后再试");
    }

    /**
     * 获取反馈的分页列表
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Result getFeedbackPage(@PageableDefault(page= 1, value= 6, sort= "id", direction= Sort.Direction.DESC) Pageable pageable){
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        Page<Feedback> page = feedbackService.getFeedbackPage(pageable, searchParams);
        return Result.wrapSuccessfulResult(page);
    }

    /**
     * 问题反馈详情页
     * @return
     */
    @RequestMapping("detail")
    public String feedbackDetail(@RequestParam(value = "id")Long id,Model model){
        Long shopId = UserUtils.getShopIdForSession(request);
        Feedback feedback = feedbackService.getFeedbackByIdAndShopId(id, shopId);
        if(feedback == null){
            return "redirect:feedback-list";
        }
        model.addAttribute("feedback",feedback);
        //查询对应的回答、追问信息
        Map<String,Object> searchMap = Maps.newHashMap();
        searchMap.put("feedbackId", id);
        searchMap.put("sorts", new String[] { "gmt_create desc" });
        List<FeedbackAnswer> feedbackAnswerList = feedbackAnswerService.select(searchMap);
        model.addAttribute("feedbackAnswerList", feedbackAnswerList);
        return "yqx/page/help/feedback/feedback-detail";
    }

    /**
     * 添加一条反馈纪录
     * @param feedbackAnswer
     * @return
     */
    @RequestMapping(value = "feedback-answer-save",method = RequestMethod.POST)
    @ResponseBody
    public Result saveFeedbackAnswer(FeedbackAnswer feedbackAnswer){
        if(feedbackAnswer == null){
            return Result.wrapErrorResult(LegendErrorCode.APP_PARAM_ERROR.getCode(),"追问数据为空，请检查数据");
        }
        String answerContent = feedbackAnswer.getAnswerContent();
        if(StringUtils.isBlank(answerContent)){
            return Result.wrapErrorResult(LegendErrorCode.APP_PARAM_ERROR.getCode(),"请输入追问信息");
        }
        if(answerContent.length() > 5000){
            return Result.wrapErrorResult(LegendErrorCode.APP_PARAM_ERROR.getCode(),"一次反馈信息最多5000个字，请删减后提交");
        }
        UserInfo userInfo = UserUtils.getUserInfo(request);
        feedbackAnswer.setCreator(userInfo.getUserId());
        feedbackAnswer.setAnswerType(AnswerTypeEnum.ASK.getCode());
        feedbackAnswer.setOperatorName(userInfo.getName());
        try {
            log.info("【门店反馈追问问题】信息为：{}",feedbackAnswer);
            feedbackAnswerService.insertFeedBackAnswer(feedbackAnswer);
            return Result.wrapSuccessfulResult(true);
        } catch (Exception e) {
            log.error("【门店反馈追问问题】出现异常",e);
        }
        return Result.wrapErrorResult(LegendErrorCode.SYSTEM_ERROR.getCode(),"追问失败，请稍后再试");
    }

    /**
     * 获取最近一条历史反馈内容
     * @return
     */
    @RequestMapping("get-new-feedback")
    @ResponseBody
    public Result getNewFeedback(){
        Long shopId = UserUtils.getShopIdForSession(request);
        Feedback feedback = feedbackService.getNewFeedback(shopId);
        if(feedback != null){
           return Result.wrapSuccessfulResult(feedback);
        }
        return Result.wrapErrorResult(LegendErrorCode.EMPTY_CONTENT.getCode(),"没有反馈数据");
    }
}
