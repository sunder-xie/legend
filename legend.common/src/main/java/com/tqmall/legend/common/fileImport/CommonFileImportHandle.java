package com.tqmall.legend.common.fileImport;

import java.io.InputStream;

/**
 * Created by twg on 16/12/7.
 */
public interface CommonFileImportHandle {

    /**
     * 校验上传的文件内容
     *
     * @return
     */
    CommonFileImportContext validation(InputStream inputStream, CommonFileImportContext fileImportContext);

    /**
     * 校验、解析上传的文件内容
     *
     * @param sourceFilePath    上传的文件路径
     * @param fileImportContext
     * @return
     */
    CommonFileImportContext validation(String sourceFilePath, CommonFileImportContext fileImportContext);

    /**
     * 处理失败的数据，生成excel文件
     *
     * @param failedIndex       添加错误信息的列索引
     * @param sourceFilePath    上传的文件路径
     * @param targetFilePath    生成excel文件路径
     * @param fileImportContext
     * @return
     */
    void handleFailedMessage(int failedIndex, String sourceFilePath, String targetFilePath, CommonFileImportContext fileImportContext);

    /**
     * 处理上传的文件内容
     *
     * @return
     */
    void handle(CommonFileImportContext fileImportContext);

}
