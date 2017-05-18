package com.tqmall.legend.facade.tech.impl;

import com.google.common.base.Optional;
import com.tqmall.common.util.ObjectUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.client.service.BookCarServiceRPC;
import com.tqmall.legend.client.service.BookCatalogueServiceRPC;
import com.tqmall.legend.client.service.BookPageServiceRPC;
import com.tqmall.legend.client.service.BookServiceRPC;
import com.tqmall.legend.client.service.dto.BookCarDto;
import com.tqmall.legend.client.service.dto.BookCatalogueDto;
import com.tqmall.legend.client.service.dto.BookDto;
import com.tqmall.legend.client.service.dto.BookPageDto;
import com.tqmall.legend.client.service.dto.BookParam;
import com.tqmall.legend.client.service.dto.PageDto;
import com.tqmall.legend.facade.tech.IBookFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 资料中心_书籍接口 实现类
 *
 */
@Slf4j
@Service
public class BookFacadeImpl implements IBookFacade {

    @Resource
    BookServiceRPC bookServiceRPC;
    @Resource
    BookCatalogueServiceRPC bookCatalogueServiceRPC;
    @Resource
    BookPageServiceRPC bookPageServiceRPC;
    @Resource
    BookCarServiceRPC bookCarServiceRPC;

    @Override
    public List<BookCarDto> getBookCars(BookParam bookParam) {
        log.info("[velly平台]dubbo获取书籍车型,入参:{}", ObjectUtils.objectToJSON(bookParam));
        Result<List<BookCarDto>> result = null;
        try {
            result = bookCarServiceRPC.select(bookParam);
        } catch (Exception e) {
            log.error("[velly平台]dubbo获取书籍车型 异常:", e);
            return new ArrayList<BookCarDto>();
        }
        if (!result.isSuccess()) {
            log.error("[velly平台]dubbo获取书籍车型 失败,失败原因:{}", result.getMessage());
            return new ArrayList<BookCarDto>();
        }
        return result.getData();
    }

    @Override
    public PageDto<BookDto> getPage(PageDto pageable, BookParam bookParam) {

        log.info("[velly平台]dubbo获取书籍列表,入参:{}", ObjectUtils.objectToJSON(bookParam));
        Result<PageDto<BookDto>> result = null;
        try {
            result = bookServiceRPC.getPage(pageable, bookParam);
        } catch (Exception e) {
            log.error("[velly平台]dubbo获取书籍列表 异常:", e);
            PageDto<BookDto> pageDto = new PageDto<BookDto>();
            pageDto.setRecordList(new ArrayList<BookDto>());
            return pageDto;
        }
        if (!result.isSuccess()) {
            log.error("[velly平台]dubbo获取书籍列表 失败,失败原因:{}", result.getMessage());
            PageDto<BookDto> pageDto = new PageDto<BookDto>();
            pageDto.setRecordList(new ArrayList<BookDto>());
            return pageDto;
        }

        return result.getData();
    }

    @Override
    public Optional<BookDto> selectById(@NotNull Long id) {
        log.info("[velly平台]dubbo获取单个书籍,书籍ID:{}", id);
        Result<BookDto> result = null;
        try {
            result = bookServiceRPC.selectById(id);
        } catch (Exception e) {
            log.error("[velly平台]dubbo获取单个书籍 异常,原因:", e);
            return Optional.absent();
        }
        if (!result.isSuccess()) {
            log.error("[velly平台]dubbo获取单个书籍 失败,失败原因: {}", result.getMessage());
            return Optional.absent();
        }
        return Optional.fromNullable(result.getData());
    }

    @Override
    public List<BookCatalogueDto> selectSecondList(@NotNull Long id) {
        log.info("[velly平台]dubbo获取书籍目录,书籍ID:{}", id);
        Result<List<BookCatalogueDto>> result = null;
        try {
            result = bookCatalogueServiceRPC.selectSecondList(id);
        } catch (Exception e) {
            log.error("[velly平台]dubbo获取书籍目录 异常:", e);
            return new ArrayList<BookCatalogueDto>();
        }
        if (!result.isSuccess()) {
            log.error("[velly平台]dubbo获取书籍目录 失败,失败原因:{}", result.getMessage());
            return new ArrayList<BookCatalogueDto>();
        }

        return result.getData();
    }

    @Override
    public PageDto<BookPageDto> getBookPagePage(PageDto pageable, BookParam searchParams) {

        log.info("[velly平台]dubbo获取书籍的页列表,入参:{}", ObjectUtils.objectToJSON(searchParams));
        Result<PageDto<BookPageDto>> result = null;
        try {
            result = bookPageServiceRPC.getPage(pageable, searchParams);
        } catch (Exception e) {
            log.error("[velly平台]dubbo获取书籍的页列表 异常:", e);
            PageDto<BookPageDto> pageDto = new PageDto<BookPageDto>();
            pageDto.setRecordList(new ArrayList<BookPageDto>());
            return pageDto;
        }
        if (!result.isSuccess()) {
            log.error("[velly平台]dubbo获取书籍的页列表 失败,失败原因:{}", result.getMessage());
            PageDto<BookPageDto> pageDto = new PageDto<BookPageDto>();
            pageDto.setRecordList(new ArrayList<BookPageDto>());
            return pageDto;
        }

        return result.getData();

    }

    @Override
    public void update(BookDto book) {
        bookServiceRPC.update(book);
    }
}
