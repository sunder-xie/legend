package com.tqmall.legend.search;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 搜索条件构建类
 * 
 * @author wanghui@changdai.cn
 * 
 */
public class SearchConditionBuilder {

	private List<SearchCondition> conds;

	public SearchConditionBuilder() {
		conds = new LinkedList<SearchCondition>();
	}

	public SearchConditionBuilder(List<SearchCondition> conds) {
		this.conds = conds;
	}

	public static SearchConditionBuilder newBuilder(List<SearchCondition> conds) {
		if (conds != null) {
			return new SearchConditionBuilder(conds);
		} else {
			return newBuilder();
		}
	}

	public static SearchConditionBuilder newBuilder() {
		return new SearchConditionBuilder();
	}

	public SearchConditionBuilder addCondition(String name, SearchOption so) {
		conds.add(new SearchCondition(name, so));
		return this;
	}

	public SearchConditionBuilder addCondition(String name, SearchOption so, String value) {
		conds.add(new SearchCondition(name, so, value));
		return this;
	}

	public SearchConditionBuilder addCondition(String name, String leftName, SearchOption so) {
		conds.add(new SearchCondition(name, leftName, so));
		return this;
	}

	public SearchConditionBuilder addCondition(String name, String leftName, SearchOption op, String value,
			String leftValue) {
		conds.add(new SearchCondition(name, leftName, op, value, leftValue));
		return this;
	}

	public List<SearchCondition> getConditions() {
		return conds;
	}

	/**
	 * 构建查询条件Map
	 * 
	 * @return
	 */
	public Map<String, Object> getParamMap() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(SearchCondition.GENERIC_SEARCH_CONDITIONS, conds);
		for (SearchCondition sc : conds) {
			if (sc.getValue() != null) {
				paramMap.put(sc.getName(), sc.getValue());
			}
		}
		return paramMap;
	}
}
