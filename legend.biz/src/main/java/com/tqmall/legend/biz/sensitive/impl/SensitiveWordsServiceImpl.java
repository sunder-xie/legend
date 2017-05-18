package com.tqmall.legend.biz.sensitive.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.sensitive.SensitiveWordsService;
import com.tqmall.legend.dao.sensitive.SensitiveWordsDao;
import com.tqmall.legend.entity.sensitive.SensitiveWords;
import com.tqmall.legend.pojo.sensitive.SensitiveWordsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lifeilong on 2015/11/19.
 */
@Service
@Slf4j
public class SensitiveWordsServiceImpl implements SensitiveWordsService {
    @Autowired
    private SensitiveWordsDao sensitiveWordsDao;

    /**
     * 获取敏感词数据列表
     * @return
     */
    @Override
    public List<SensitiveWordsVO> getSensitiveWords() {
        List<SensitiveWords> sensitiveWordsList = new ArrayList<SensitiveWords>();
        Map<String,Object> params = new HashMap<String,Object>();

        sensitiveWordsList = sensitiveWordsDao.select(params);

        List<SensitiveWordsVO> sensitiveWordsVOList = new ArrayList<SensitiveWordsVO>();
        try{
            for(SensitiveWords sensitiveWords : sensitiveWordsList){
                SensitiveWordsVO sensitiveWordsVO = new SensitiveWordsVO();
                BeanUtils.copyProperties(sensitiveWords, sensitiveWordsVO);
                sensitiveWordsVOList.add(sensitiveWordsVO);
            }
        }catch(Exception e){
            log.error("属性复制错误,e",e);
            return null;
        }
        return sensitiveWordsVOList;
    }

    /**
     * 判断一个String数组里是否含有敏感词 ,最后返回敏感词在数组里的位置index
     * @param stringArray
     * @return
     */
    @Override
    public Result<Integer[]> checkStringArrayHasSensitiveWord(final String[] stringArray) {
        return new ApiTemplate<Integer[]>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                if (null == stringArray || stringArray.length < 1) {
                    throw new IllegalArgumentException("");
                }
            }

            @Override
            protected Integer[] process() throws BizException {
                List<SensitiveWordsVO> sensitiveWordsList = getSensitiveWords();
                List<Integer> indexList = new ArrayList<>();
                List<String> stringList = new ArrayList<>(Arrays.asList(stringArray));
                for (SensitiveWordsVO sensitiveWordsVO : sensitiveWordsList) {
                    if (StringUtils.isBlank(sensitiveWordsVO.getContent())) {
                        continue;
                    }
                    for (int i = stringList.size() - 1; i >= 0; i --) {
                        String content = stringList.get(i);
                        if (checkContent(content, sensitiveWordsVO.getContent())) {
                            indexList.add(i);
                            stringList.remove(i);
                        }

                    }
                }
                if (!CollectionUtils.isEmpty(indexList)) {
                    Collections.sort(indexList);
                    Integer size = indexList.size();
                    Integer[] integers = indexList.toArray(new Integer[size]);
                    return integers;
                } else {
                    return null;
                }
            }
        }.execute();

    }

    private Boolean checkContent(String content, String sensitiveWord) {
        if (StringUtils.isBlank(content) || StringUtils.isBlank(sensitiveWord)) {
            return false;
        }
        if (content.contains(sensitiveWord)) {
            return true;
        }
        return false;
    }
}
