package com.tqmall.web.filter;

import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 跨站脚本请求包装器，将html脚本转译成普通字符串
 *
 * @author jefferyyang
 *
 */
public class XssRequestWrapper extends HttpServletRequestWrapper {

	public XssRequestWrapper(HttpServletRequest request) {
		super(request);
	}

//TODO 表单组件有json数据，不能转
//	@Override
//	public String[] getParameterValues(String parameter) {
//		String[] values = super.getParameterValues(parameter);
//		if (values == null) {
//			return null;
//		}
//		int count = values.length;
//		String[] encodedValues = new String[count];
//		for (int i = 0; i < count; i++) {
//			encodedValues[i] = cleanXSS(values[i]);
//		}
//		return encodedValues;
//	}

	@Override
	/**
	 * 转译get入参，防止站点脚本注入
	 */
	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		if (value == null) {
			return null;
		}
		value = cleanXSS(value);
		return value;
	}

//TODO 头不用转
//	@Override
//	public String getHeader(String name) {
//		String value = super.getHeader(name);
//		if (value == null)
//			return null;
//		return cleanXSS(value);
//	}

	private String cleanXSS(String value) {
		return HtmlUtils.htmlEscape(value);
	}

}
