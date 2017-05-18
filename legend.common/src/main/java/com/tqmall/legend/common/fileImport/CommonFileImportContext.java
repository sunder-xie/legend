package com.tqmall.legend.common.fileImport;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by twg on 16/12/7.
 */
@Getter
@Setter
public class CommonFileImportContext<T> implements Serializable {

    private Long shopId;
    private Long userId;
    private String userName;
    private int faildNum;//导入失败的数
    private int successNum;//导入成功的数
    private String fileName;
    /*解析后的excel内容列表*/
    private List<T> excelContents;
    /*错误信息集合*/
    private List<String> faildMessage = Lists.newArrayList();
    /*某行对应的错误信息集合*/
    private Map<Integer, List<String>> rowFaildMessages = Maps.newTreeMap();

}
