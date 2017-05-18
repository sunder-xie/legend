package com.tqmall.legend.web.notice;

import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.notice.NoticeService;
import com.tqmall.legend.entity.notice.NoticeEntity;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.common.Result;
import com.tqmall.legend.web.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/shop/notice")
@Slf4j
public class NoticeController extends BaseController{
	@Autowired
	private NoticeService noticeService;

	
	@RequestMapping("list")
    @ResponseBody
    public Result<DefaultPage<NoticeEntity>> listNotice(@PageableDefault(page = 1, value = 10, direction = Sort.Direction.DESC) Pageable pageable) {
        Result result = null;
        try {
            DefaultPage<NoticeEntity> page = noticeService.getNoticeByPage(pageable, ServletUtils.getParametersMapStartWith(request));
            result = Result.wrapSuccessfulResult(page);
        } catch (Exception e) {
            log.error("[公告列表] 获取公告列表信息出错.错误异常为：{}",e);
            result = Result.wrapErrorResult("","获取公告列表数据出错");
        }
        return result;
    }
	
	@RequestMapping("last")
    @ResponseBody
    public Result<NoticeEntity> getLastNotice() {
        Result result = null;
        try {
        	NoticeEntity notice = noticeService.getLastNotice(ServletUtils.getParametersMapStartWith(request));
            result = Result.wrapSuccessfulResult(notice);
        } catch (Exception e) {
            log.error("[公告列表] 获取最新公告出现异常，异常信息为{}",e);
            result = Result.wrapErrorResult("","获取最新公告信息出错");
        }
        return result;
    }

}
