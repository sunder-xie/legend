package com.tqmall.legend.web.tech.book;

import com.google.common.base.Optional;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.client.service.dto.BookDto;
import com.tqmall.legend.client.service.dto.BookPageDto;
import com.tqmall.legend.client.service.dto.BookParam;
import com.tqmall.legend.client.service.dto.PageDto;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.book.BookStatusEnum;
import com.tqmall.legend.facade.tech.IBookFacade;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 技术中心——维修资料库
 */
@Slf4j
@Controller
@RequestMapping("shop/tech/book")
public class BookController extends BaseController {

    @Autowired
    IBookFacade bookFacade;

    /**
     * 维修资料库首页
     *
     * @param model
     * @param keywords
     * @return
     */
    @HttpRequestLog
    @RequestMapping
    public String index(Model model, @RequestParam(value = "keywords", required = false) String keywords) {
        if (keywords != null) {
            model.addAttribute("keywords", keywords);
        }
        model.addAttribute("moduleUrl", "tech");
        model.addAttribute("techTab", "book");

        return "tech/book/book_list";
    }

    /**
     * 书籍列表
     *
     * @param pageable
     * @param request
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public Object bookList(@PageableDefault(page = 1, value = 4, sort = "gmt_modified", direction = Sort.Direction.DESC) Pageable pageable,
                           HttpServletRequest request) {

        // 当前门店ID
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        PageDto<BookDto> page = null;

        // 页参数(页码\每页条数\排序)
        PageDto pageableRequest = new PageDto();
        pageableRequest.setPageNum(pageable.getPageNumber());
        pageableRequest.setPageSize(pageable.getPageSize());
        // gmt_modified
        Set<String> sortSet = new HashSet<String>(1);
        sortSet.add("gmt_modified desc");
        pageableRequest.setSort(sortSet);

        // 查询参数
        BookParam bookParam = new BookParam();
        // 品牌
        if (searchParams.containsKey("carBrand")) {
            bookParam.setCarBrand(searchParams.get("carBrand") + "");
        }
        // 系列
        if (searchParams.containsKey("carSeries")) {
            bookParam.setCarBrand(searchParams.get("carSeries") + "");
        }
        // 年款
        if (searchParams.containsKey("carYear")) {
            bookParam.setCarBrand(searchParams.get("carYear") + "");
        }
        bookParam.setStatus(BookStatusEnum.PUBLISH.getCode());
        page = bookFacade.getPage(pageableRequest, bookParam);
        long totalNumLong = Long.parseLong(page.getTotalNum() + "");
        DefaultPage defaultPage = new DefaultPage<BookDto>(page.getRecordList(), pageable, totalNumLong);

        return Result.wrapSuccessfulResult(defaultPage);
    }

    /**
     * 书籍详情页
     *
     * @param id    书籍ID
     * @param model
     * @return
     */
    @RequestMapping(value = "info", method = RequestMethod.GET)
    public String info(@RequestParam(value = "id", required = true) Long id,
                       Model model) {
        Optional<BookDto> BookDtoOptional = bookFacade.selectById(id);
        if (!BookDtoOptional.isPresent()) {
            return "redirect:";
        }

        model.addAttribute("moduleUrl", "tech");
        model.addAttribute("book", BookDtoOptional.get());
        model.addAttribute("bookCatalogueList", bookFacade.selectSecondList(id));

        return "tech/book/book_info";
    }

    /**
     * 书籍书页列表
     *
     * @param pageable
     * @param request
     * @return
     */
    @RequestMapping(value = "page_list", method = RequestMethod.GET)
    @ResponseBody
    public Object bookPageList(@PageableDefault(page = 1, value = 1, sort = "page", direction = Sort.Direction.ASC) Pageable pageable,
                               HttpServletRequest request) {
        Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
        PageDto<BookPageDto> page = null;
        // 页参数(页码\每页条数\排序)
        PageDto pageableRequest = new PageDto();
        pageableRequest.setPageNum(pageable.getPageNumber());
        pageableRequest.setPageSize(pageable.getPageSize());
        Set<String> sortSet = new HashSet<String>(1);
        sortSet.add("page asc");
        pageableRequest.setSort(sortSet);

        // 请求参数
        BookParam bookParam = new BookParam();
        // 书籍ID
        if (searchParams.containsKey("bookId")) {
            bookParam.setBookId(Long.parseLong(searchParams.get("bookId") + ""));
        }
        page = bookFacade.getBookPagePage(pageableRequest, bookParam);
        long totalNumLong = Long.parseLong(page.getTotalNum() + "");
        DefaultPage defaultPage = new DefaultPage<BookPageDto>(page.getRecordList(), pageable, totalNumLong);

        return Result.wrapSuccessfulResult(defaultPage);
    }

    /**
     * 喜欢
     *
     * @param id 书籍ID
     * @return
     */
    @RequestMapping(value = "doLike", method = RequestMethod.GET)
    @ResponseBody
    public Result doLike(@RequestParam(value = "id", required = true) Long id) {

        Optional<BookDto> bookDtoOptional = bookFacade.selectById(id);
        if (!bookDtoOptional.isPresent()) {
            return Result.wrapErrorResult("", "操作失败,书籍不存在");
        }
        BookDto book = bookDtoOptional.get();
        long likeNum = book.getLikeNum() + 1;
        book.setLikeNum(likeNum);

        try {
            bookFacade.update(book);
            return Result.wrapSuccessfulResult(likeNum);
        } catch (Exception e) {
            log.error("书籍ID:{} 点击喜欢操作失败。错误信息：{} ", id, e);
            return Result.wrapErrorResult("", "操作失败");
        }
    }

    /**
     * 不喜欢
     *
     * @param id 书籍ID
     * @return
     */
    @RequestMapping(value = "doUnlike", method = RequestMethod.GET)
    @ResponseBody
    public Result doUnlike(@RequestParam(value = "id", required = true) Long id) {

        Optional<BookDto> bookDtoOptional = bookFacade.selectById(id);
        if (!bookDtoOptional.isPresent()) {
            return Result.wrapErrorResult("", "操作失败,书籍不存在");
        }
        BookDto book = bookDtoOptional.get();
        long unlikeNum = book.getUnlikeNum() + 1;
        book.setUnlikeNum(unlikeNum);

        try {
            bookFacade.update(book);
            return Result.wrapSuccessfulResult(unlikeNum);
        } catch (Exception e) {
            log.error("书籍ID:{} 点击不喜欢操作失败。错误信息：{} ", id, e);
            return Result.wrapErrorResult("", "操作失败");
        }
    }

    /**
     * 书籍全局预览
     *
     * @param model
     * @param id
     * @param page
     * @return
     */
    @RequestMapping(value = "preview_full", method = RequestMethod.GET)
    public String previewFull(Model model,
                              @RequestParam(value = "id", required = true) Long id,
                              @RequestParam(value = "page", defaultValue = "1") Integer page) {
        model.addAttribute("moduleUrl", "tech");
        Optional<BookDto> bookDtoOptional = bookFacade.selectById(id);
        if (!bookDtoOptional.isPresent()) {
            return "redirect:";
        }
        BookDto book = bookDtoOptional.get();
        Integer status = book.getStatus();
        if (status != null && status.equals(BookStatusEnum.COMPLETEDRAFT.getCode())) {
            return "redirect:";
        }
        model.addAttribute("page", page);
        model.addAttribute("book", book);

        return "tech/book/book_preview_full";
    }

    /**
     * 获取书籍车辆级别
     *
     * @param parentId 父节点ID
     * @return
     */
    @RequestMapping(value = "get_bookcar_level", method = RequestMethod.GET)
    @ResponseBody
    public Result getBookcarLevel(@RequestParam(value = "parentId", required = true) Long parentId) {
        BookParam bookParam = new BookParam();
        bookParam.setParentId(parentId);
        return Result.wrapSuccessfulResult(bookFacade.getBookCars(bookParam));
    }


}
