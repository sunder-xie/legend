package com.tqmall.legend.search;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 搜索条件
 * 
 * @author wanghui@changdai.cn
 * 
 */
public class SearchCondition {

	public static final String GENERIC_SEARCH_CONDITIONS = "_g_search_condition";

	private String leftDispalyName;

	private String displayName;

	private String leftName;

	private String name;
	/**
	 * 搜索左值
	 */
	private String leftValue;
	/**
	 * 搜索右值
	 */
	private String value;

	/**
	 * 搜索条件
	 */
	private SearchOption searchOption;

	public SearchCondition() {
	}

	public SearchCondition(String name, SearchOption so) {
		this.name = name;
		this.searchOption = so;
	}

	public SearchCondition(String name, SearchOption so, String rValue) {
		this(name, so);
		this.value = rValue;
	}

	public SearchCondition(String name, String leftName, SearchOption so) {
		this.name = name;
		this.leftName = leftName;
		this.searchOption = so;
	}
	public SearchCondition(String name, String leftName, SearchOption so, String value, String leftValue) {
		this.name = name;
		this.leftName = leftName;
		this.searchOption = so;
		this.value = value;
		this.leftValue = leftValue;
	}

	public String getSqlSnippet() {
		String sqlSnippet = null;
		if (searchOption != null) {
			switch (this.searchOption) {
			case GT:
				sqlSnippet = String.format("%s>#{%s}", name, name);
				break;
			case LT:
				sqlSnippet = String.format("%s<#{%s}", name, name);
				break;
			case GE:
				sqlSnippet = String.format("%s>=#{%s}", name, name);
				break;
			case LE:
				sqlSnippet = String.format("%s<=#{%s}", name, name);
				break;
			case EQ:
				sqlSnippet = String.format("%s=#{%s}", name, name);
				break;
			case NQ:
				sqlSnippet = String.format("%s!=#{%s}", name, name);
				break;
			case LIKE:
				sqlSnippet = name+ " LIKE CONCAT('%',#{"+name+"},'%')";
			}
		}
		return sqlSnippet;
	}

	public String getLeftDispalyName() {
		return leftDispalyName;
	}

	public String getName() {
		return name;
	}

	public String getLeftValue() {
		return leftValue;
	}

	public String getValue() {
		return value;
	}

	public SearchOption getSearchOption() {
		return searchOption;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this).toString();
	}
}
