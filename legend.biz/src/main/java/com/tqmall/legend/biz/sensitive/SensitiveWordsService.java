package com.tqmall.legend.biz.sensitive;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.pojo.sensitive.SensitiveWordsVO;

import java.util.List;

/**
 * Created by lifeilong on 2015/11/19.
 */
public interface SensitiveWordsService {
    /**
     * 获取敏感词
     * @return
     */
    public List<SensitiveWordsVO> getSensitiveWords();

    /**
     * 判断一个String数组里是否含有敏感词 ,最后返回敏感词在数组里的位置index
      * @param stringArray
     * @return
     */
    public Result<Integer[]> checkStringArrayHasSensitiveWord(String[] stringArray);
}
