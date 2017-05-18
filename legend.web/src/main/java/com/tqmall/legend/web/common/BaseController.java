package com.tqmall.legend.web.common;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by guozhiqiang on 14-7-15.
 */
public abstract class BaseController {

	@Autowired
	protected HttpServletRequest request;

}
