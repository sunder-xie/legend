package com.tqmall.legend.common.fileImport;

import com.google.common.collect.Lists;
import com.tqmall.common.exception.BizException;
import com.tqmall.wheel.component.excel.ExcelExceptionHandler;
import com.tqmall.wheel.component.excel.copy.ExcelCopy;
import com.tqmall.wheel.component.excel.copy.RowCallback;
import com.tqmall.wheel.component.excel.loader.ExcelLoader;
import com.tqmall.wheel.exception.DecryptionException;
import com.tqmall.wheel.exception.WheelException;
import com.tqmall.wheel.helper.ExcelHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by twg on 16/12/7.
 */
@Slf4j
public class DefaultFileImportManager implements CommonFileImportHandle {
    private List<FileImportProcess> fileImportProcesses;

    @Override
    public CommonFileImportContext validation(InputStream inputStream, final CommonFileImportContext fileImportContext) {
        ExcelLoader excelLoader = null;
        final Map<Integer, List<String>> rowFaildMessages = fileImportContext.getRowFaildMessages();
        try {
            excelLoader = ExcelHelper.createExcelLoader(inputStream);
            excelLoader.setExceptionHandler(new ExcelExceptionHandler() {
                @Override
                public String handler(int i, int i1, int i2, String s, DecryptionException e) throws WheelException {
                    String failMessage = String.format(ImportFailedMessages.DEFAULT_FAILED_MESSAGE, i1 + 1, i2 + 1, s);
                    fileImportContext.getFaildMessage().add(failMessage);
                    if (rowFaildMessages.containsKey(i1 + 1)) {
                        List<String> faildMessage = rowFaildMessages.get(i1 + 1);
                        faildMessage.add(failMessage);
                    } else {
                        List<String> faildMessages = Lists.newArrayList();
                        faildMessages.add(failMessage);
                        rowFaildMessages.put(i1 + 1, faildMessages);
                    }
                    return null;
                }

                @Override
                public void validateError(int i, int i1, int i2, String s) {
                    String failMessage = String.format(ImportFailedMessages.DEFAULT_FAILED_MESSAGE, i1 + 1, i2 + 1, s);
                    fileImportContext.getFaildMessage().add(failMessage);
                    if (rowFaildMessages.containsKey(i1 + 1)) {
                        List<String> faildMessage = rowFaildMessages.get(i1 + 1);
                        faildMessage.add(failMessage);
                    } else {
                        List<String> faildMessages = Lists.newArrayList();
                        faildMessages.add(failMessage);
                        rowFaildMessages.put(i1 + 1, faildMessages);
                    }
                }
            });
            Type type = fileImportContext.getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) type).getActualTypeArguments();
            Class entityClass = (Class) params[0];
            fileImportContext.setExcelContents(excelLoader.readRecords(1, entityClass));
            if (!CollectionUtils.isEmpty(fileImportContext.getExcelContents())) {
                this.handle(fileImportContext);
            }
            fileImportContext.setFaildNum(rowFaildMessages.size());
        } catch (Exception e) {
            log.error("Loader excel file is error.", e);
        } finally {
            try {
                if (excelLoader != null) {
                    ExcelHelper.closeQuiet(excelLoader);
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("InputStream close error");
            } catch (Exception e) {
                log.error("Close excel error.", e);
            }
        }
        return fileImportContext;
    }

    @Override
    public CommonFileImportContext validation(String filePath, final CommonFileImportContext fileImportContext) {
        Long sTime = System.currentTimeMillis();
        ExcelLoader excelLoader = null;
        final Map<Integer, List<String>> rowFaildMessages = fileImportContext.getRowFaildMessages();
        try {
            excelLoader = ExcelHelper.createExcelLoader(filePath);
            excelLoader.setExceptionHandler(new ExcelExceptionHandler() {
                @Override
                public String handler(int i, int i1, int i2, String s, DecryptionException e) throws WheelException {
                    String failMessage = String.format(ImportFailedMessages.DEFAULT_FAILED_MESSAGE, i1 + 1, i2 + 1, s);
                    fileImportContext.getFaildMessage().add(failMessage);
                    if (rowFaildMessages.containsKey(i1 + 1)) {
                        List<String> faildMessage = rowFaildMessages.get(i1 + 1);
                        faildMessage.add(failMessage);
                    } else {
                        List<String> faildMessages = Lists.newArrayList();
                        faildMessages.add(failMessage);
                        rowFaildMessages.put(i1 + 1, faildMessages);
                    }
                    throw new WheelException("excel解析异常");
                }

                @Override
                public void validateError(int i, int i1, int i2, String s) {
                    String failMessage = String.format(ImportFailedMessages.DEFAULT_FAILED_MESSAGE, i1 + 1, i2 + 1, s);
                    fileImportContext.getFaildMessage().add(failMessage);
                    if (rowFaildMessages.containsKey(i1 + 1)) {
                        List<String> faildMessage = rowFaildMessages.get(i1 + 1);
                        faildMessage.add(failMessage);
                    } else {
                        List<String> faildMessages = Lists.newArrayList();
                        faildMessages.add(failMessage);
                        rowFaildMessages.put(i1 + 1, faildMessages);
                    }
                }
            });
            Type type = fileImportContext.getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) type).getActualTypeArguments();
            Class entityClass = (Class) params[0];
            fileImportContext.setExcelContents(excelLoader.readRecords(1, entityClass));
            if (!CollectionUtils.isEmpty(fileImportContext.getExcelContents())) {
                this.handle(fileImportContext);
                log.info("门店id：{}，用户id：{}，导入对象{}数据耗时：{} ms", fileImportContext.getShopId(), fileImportContext.getUserId(), entityClass.getSimpleName(), System.currentTimeMillis() - sTime);
            }
            fileImportContext.setFaildNum(rowFaildMessages.size());
        } catch (Exception e) {
            log.error("Loader excel file is error.", e);
            throw new BizException(e);
        } finally {
            try {
                if (excelLoader != null) {
                    ExcelHelper.closeQuiet(excelLoader);
                }
            } catch (Exception e) {
                log.error("Close excel error.", e);
            }
        }
        return fileImportContext;
    }

    @Override
    public void handleFailedMessage(final int failedIndex, String sourceFilePath, String targetFilePath, CommonFileImportContext fileImportContext) {
        ExcelCopy excelCopy = null;
        try {
            excelCopy = ExcelHelper.createExcelCopy(sourceFilePath, targetFilePath);
            Map<Integer, List<String>> rowFailedMessage = fileImportContext.getRowFaildMessages();
            List<Integer> keys = Lists.newArrayList();
            for (Map.Entry<Integer, List<String>> integerListEntry : rowFailedMessage.entrySet()) {
                keys.add(integerListEntry.getKey() - 1);
            }
            Integer[] key = keys.toArray(new Integer[] { });
            String fileName = targetFilePath.substring(targetFilePath.lastIndexOf("/"));
            final Iterator<Map.Entry<Integer, List<String>>> ltr = rowFailedMessage.entrySet().iterator();

            excelCopy.copyRow(0, new RowCallback() {
                @Override
                public void callback(int i, Row row) {
                    Cell cell = row.createCell(failedIndex);
                    cell.setCellValue("失败原因");
                }
            }).copyRows(ArrayUtils.toPrimitive(key), new RowCallback() {
                public void callback(int targetRowNumber, Row row) {
                    Cell cell = row.createCell(failedIndex);
                    cell.setCellValue(StringUtils.join(ltr.next().getValue(), "；"));
                }
            });
            fileImportContext.setFileName(fileName);
            fileImportContext.setRowFaildMessages(null);
        } catch (IOException e) {
            log.error("ExcelCopy is error.", e);
        } catch (InvalidFormatException e) {
            log.error("handleFailedMessage is error.", e);
        } finally {
            try {
                if (excelCopy != null) {
                    excelCopy.close();
                }
            } catch (IOException e) {
                log.error("ExcelCopy close is error.", e);
            }
        }
    }

    @Override
    public void handle(final CommonFileImportContext fileImportContext) {
        if (CollectionUtils.isEmpty(fileImportProcesses)) {
            throw new BizException("The fileImportProcesses property is null");
        }
        for (FileImportProcess fileImportProcess : fileImportProcesses) {
            fileImportProcess.process(fileImportContext);
        }
    }

    public void setFileImportProcesses(List<FileImportProcess> fileImportProcesses) {
        this.fileImportProcesses = fileImportProcesses;
    }

}
