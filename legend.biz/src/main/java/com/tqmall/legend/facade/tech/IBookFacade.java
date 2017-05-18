package com.tqmall.legend.facade.tech;

import com.google.common.base.Optional;
import com.tqmall.legend.client.service.dto.BookCarDto;
import com.tqmall.legend.client.service.dto.BookCatalogueDto;
import com.tqmall.legend.client.service.dto.BookDto;
import com.tqmall.legend.client.service.dto.BookPageDto;
import com.tqmall.legend.client.service.dto.BookParam;
import com.tqmall.legend.client.service.dto.PageDto;

import java.util.List;

/**
 * 资料中心-书籍 interface
 */
public interface IBookFacade {


    /**
     * 书籍车型
     *
     * @param bookParam
     * @return List<BookCarDto>
     */
    List<BookCarDto> getBookCars(BookParam bookParam);


    /**
     * 分页获取书籍
     *
     * @param pageable
     * @param bookParam
     * @return PageDto<BookDto>
     */
    PageDto<BookDto> getPage(PageDto pageable, BookParam bookParam);

    /**
     * 获取单个书籍
     *
     * @param id 书籍ID
     * @return Optional<BookDto>
     */
    Optional<BookDto> selectById(Long id);

    /**
     * 获取书籍目录
     *
     * @param id 书籍ID
     * @return List<BookCatalogueDto>
     */
    List<BookCatalogueDto> selectSecondList(Long id);

    /**
     * 分页获取书籍的页张
     *
     * @param pageable
     * @param searchParams
     * @return
     */
    PageDto<BookPageDto> getBookPagePage(PageDto pageable, BookParam searchParams);

    /**
     * 更新数据
     *
     * @param book 数据实体
     */
    void update(BookDto book);
}
