package com.tqmall.legend.biz.util;

import com.tqmall.common.Constants;
import com.tqmall.legend.common.CommonUtils;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public class ExcelReader {
    private final static Integer ROW_LIMIT = Constants.EXCEL_ROW_LIMIT;

    /**
     * 对外提供读取excel 的方法
     */
    public static List<List<Object>> readExcel(File file) throws IOException {
        String fileName = file.getName();
        String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
        if ("xls".equals(extension)) {
            return read2003Excel(file, null);
        } else if ("xlsx".equals(extension)) {
            return read2007Excel(file, null);
        } else {
            throw new IOException("不支持的文件类型");
        }
    }

    /**
     * 读取 office 2003 excel
     *
     * @throws IOException
     * @throws FileNotFoundException
     */
    private static List<List<Object>> read2003Excel(File file, Integer colspan) throws IOException {
        List<List<Object>> list = new LinkedList<List<Object>>();
        HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheet = hwb.getSheetAt(0);
        Object value = null;
        HSSFRow row = null;
        HSSFCell cell = null;
        for (int i = sheet.getFirstRowNum(); i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            List<Object> linked = new LinkedList<Object>();
            if (row.getFirstCellNum() < 0) {
                break;
            }

            if (colspan == null) {
                colspan = Integer.valueOf(row.getLastCellNum());
            }
            for (int j = row.getFirstCellNum(); j < colspan; j++) {
                //            for (int j = row.getFirstCellNum(); j <= 40; j++) {

                cell = row.getCell(j);
                if (cell == null) {
                    value = "";

                    //    continue;
                } else {
                    DecimalFormat df = new DecimalFormat("0.00000000");// 格式化 number String 字符
                    DecimalFormat nf = new DecimalFormat("0");// 格式化数字
                    switch (cell.getCellType()) {
                        case XSSFCell.CELL_TYPE_STRING:
                            //    System.out.println(i+"行"+j+" 列 is String type");
                            value = cell.getStringCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_NUMERIC:
                            //    System.out.println(i+"行"+j+" 列 is Number type ; DateFormt:"+cell.getCellStyle().getDataFormatString());
                            /*if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                                value = df.format(cell.getNumericCellValue());
                            } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                                value = nf.format(cell.getNumericCellValue());
                            } else {
                                //    value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                                value = nf.format(cell.getNumericCellValue());
                            }*/

                            String str = "" + cell.getNumericCellValue();
                            if (!org.apache.commons.lang3.StringUtils.isBlank(str)) {
                                int length = str.length() - str.indexOf(".") - 1;
                                if (str.contains(".0") && length == 1) {
                                    value = nf.format(cell.getNumericCellValue());
                                } else {
                                    value = df.format(cell.getNumericCellValue());
                                }
                                if (value.toString().contains(".00000000")) {
                                    value = value.toString().replace(".00000000", "");
                                }
                            }
                            break;
                        case XSSFCell.CELL_TYPE_BOOLEAN:
                            //   System.out.println(i+"行"+j+" 列 is Boolean type");
                            value = cell.getBooleanCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_BLANK:
                            //    System.out.println(i+"行"+j+" 列 is Blank type");
                            value = "";
                            break;
                        default:
                            //    System.out.println(i+"行"+j+" 列 is default type");
                            value = cell.toString();
                    }
                    if (value == null || ((String) value).trim().equals("")) {
                        //    continue;
                        // 	   System.out.println(i+"行"+j+" 列 is null type");
                        value = "";
                    }

                }
                linked.add(value);
            }

            list.add(linked);
        }
        return list;
    }

    /**
     * 读取Office 2007 excel
     */
    private static List<List<Object>> read2007Excel(File file, Integer colspan) throws IOException {
        List<List<Object>> list = new LinkedList<List<Object>>();
        // 构造 XSSFWorkbook 对象，strPath 传入文件路径
        XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
        // 读取第一章表格内容
        XSSFSheet sheet = xwb.getSheetAt(0);
        Object value = null;
        XSSFRow row = null;
        XSSFCell cell = null;
        int size = sheet.getPhysicalNumberOfRows();
        for (int i = sheet.getFirstRowNum(); i < size; i++) {

            row = sheet.getRow(i);
            //   if (row == null || isBlankRow(row)) {
            if (row == null) {
                continue;
            }
            List<Object> linked = new LinkedList<Object>();
            if (row.getFirstCellNum() < 0) {
                break;
            }
            if (colspan == null) {
                colspan = Integer.valueOf(row.getLastCellNum());
            }
            for (int j = row.getFirstCellNum(); j < colspan; j++) {
                //            for (int j = row.getFirstCellNum(); j < 40; j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    value = "0";

                    //     continue;
                } else {


                    DecimalFormat df = new DecimalFormat("0.00000000");// 格式化 number String 字符
                    DecimalFormat nf = new DecimalFormat("0");// 格式化数字
                    switch (cell.getCellType()) {
                        case XSSFCell.CELL_TYPE_STRING:
                            //   System.out.println(i+"行"+j+" 列 is String type");
                            value = cell.getStringCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_NUMERIC:
                            //   System.out.println(i+"行"+j+" 列 is Number type ; DateFormt:"+cell.getCellStyle().getDataFormatString());
                            /*if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                                value = df.format(cell.getNumericCellValue());
                            } else if ("General"
                                .equals(cell.getCellStyle().getDataFormatString())) {
                                value = nf.format(cell.getNumericCellValue());
                            } else {
                                //   value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                                value = nf.format(cell.getNumericCellValue());
                            }*/
                            String str = "" + cell.getNumericCellValue();
                            if (!org.apache.commons.lang3.StringUtils.isBlank(str)) {
                                int length = str.length() - str.indexOf(".") - 1;
                                if (str.contains(".0") && length == 1) {
                                    value = nf.format(cell.getNumericCellValue());
                                } else {
                                    value = df.format(cell.getNumericCellValue());
                                }
                                if (value.toString().contains(".00000000")) {
                                    value = value.toString().replace(".00000000", "");
                                }
                            }
                            break;
                        case XSSFCell.CELL_TYPE_BOOLEAN:
                            //   System.out.println(i+"行"+j+" 列 is Boolean type");
                            value = cell.getBooleanCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_BLANK:
                            //      System.out.println(i+"行"+j+" 列 is Blank type");
                            value = "0";
                            break;
                        default:
                            //   System.out.println(i+"行"+j+" 列 is default type");
                            value = cell.toString();
                    }
                    if (value == null || ((String) value).trim().equals("")) {
                        //     continue;
                        value = "0";
                    }

                }
                linked.add(value);
            }
            list.add(linked);
        }
        return list;
    }

    /**
     * 读取 office 2003 excel
     *
     * @throws IOException
     * @throws FileNotFoundException
     */

    /**
     * 分段解析excel(不包含header行)
     *
     * @param file
     * @param colspan
     * @param index
     * @return
     */
    public static List<List<Object>> subReadExcel(File file, Integer colspan, Integer index) throws IOException {
        String fileName = file.getName();
        String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
        Integer startIndex = ROW_LIMIT * (index - 1) + 1;
        Integer endIndex = ROW_LIMIT * index;
        if ("xls".equals(extension)) {
            return subRead2003Excel(file, colspan, startIndex, endIndex);
        } else if ("xlsx".equals(extension)) {
            return subRead2007Excel(file, colspan, startIndex, endIndex);
        } else {
            throw new IOException("不支持的文件类型");
        }
    }

    /**
     * 读取 office 2003 excel
     *
     * @param file
     * @param colspan
     * @return
     * @throws IOException
     */
    private static List<List<Object>> subRead2003Excel(File file, Integer colspan, Integer startIndex, Integer endIndex) throws IOException {
        List<List<Object>> list = new LinkedList<List<Object>>();
        HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheet = hwb.getSheetAt(0);
        Object value = null;
        HSSFRow row = null;
        HSSFCell cell = null;
        endIndex = endIndex > sheet.getPhysicalNumberOfRows() ? sheet.getPhysicalNumberOfRows() : endIndex;
        for (int i = startIndex; i <= endIndex; i++) {
            row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            List<Object> linked = new LinkedList<Object>();
            if (row.getFirstCellNum() < 0) {
                break;
            }

            if (colspan == null) {
                colspan = Integer.valueOf(row.getLastCellNum());
            }
            for (int j = row.getFirstCellNum(); j < colspan; j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    value = "";
                } else {
                    DecimalFormat df = new DecimalFormat("0.00000000");// 格式化 number String 字符
                    DecimalFormat nf = new DecimalFormat("0");// 格式化数字
                    switch (cell.getCellType()) {
                        case XSSFCell.CELL_TYPE_STRING:
                            value = cell.getStringCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_NUMERIC:
                            String str = "" + cell.getNumericCellValue();
                            if (!org.apache.commons.lang3.StringUtils.isBlank(str)) {
                                int length = str.length() - str.indexOf(".") - 1;
                                if (str.contains(".0") && length == 1) {
                                    value = nf.format(cell.getNumericCellValue());
                                } else {
                                    value = df.format(cell.getNumericCellValue());
                                }
                                if (value.toString().contains(".00000000")) {
                                    value = value.toString().replace(".00000000", "");
                                }
                            }
                            break;
                        case XSSFCell.CELL_TYPE_BOOLEAN:
                            value = cell.getBooleanCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_BLANK:
                            value = "";
                            break;
                        default:
                            value = cell.toString();
                    }
                    if (value == null || ((String) value).trim().equals("")) {
                        value = "";
                    }

                }
                linked.add(value);
            }
            list.add(linked);
        }
        return list;
    }

    /**
     * 读取Office 2007 excel
     */
    private static List<List<Object>> subRead2007Excel(File file, Integer colspan, Integer startIndex, Integer endIndex) throws IOException {
        List<List<Object>> list = new LinkedList<List<Object>>();
        // 构造 XSSFWorkbook 对象，strPath 传入文件路径
        XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
        // 读取第一章表格内容
        XSSFSheet sheet = xwb.getSheetAt(0);
        Object value = null;
        XSSFRow row = null;
        XSSFCell cell = null;
        endIndex = endIndex > sheet.getPhysicalNumberOfRows() ? sheet.getPhysicalNumberOfRows() : endIndex;
        for (int i = startIndex; i <= endIndex; i++) {

            row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            List<Object> linked = new LinkedList<Object>();
            if (row.getFirstCellNum() < 0) {
                break;
            }
            if (colspan == null) {
                colspan = Integer.valueOf(row.getLastCellNum());
            }
            for (int j = row.getFirstCellNum(); j < colspan; j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    /**
                     *
                     */
                    value = "0";

                } else {
                    DecimalFormat df = new DecimalFormat("0.00000000");// 格式化 number String 字符
                    DecimalFormat nf = new DecimalFormat("0");// 格式化数字
                    switch (cell.getCellType()) {
                        case XSSFCell.CELL_TYPE_STRING:
                            value = cell.getStringCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_NUMERIC:
                            String str = "" + cell.getNumericCellValue();
                            if (!org.apache.commons.lang3.StringUtils.isBlank(str)) {
                                int length = str.length() - str.indexOf(".") - 1;
                                if (str.contains(".0") && length == 1) {
                                    value = nf.format(cell.getNumericCellValue());
                                } else {
                                    value = df.format(cell.getNumericCellValue());
                                }
                                if (value.toString().contains(".00000000")) {
                                    value = value.toString().replace(".00000000", "");
                                }
                            }
                            break;
                        case XSSFCell.CELL_TYPE_BOOLEAN:
                            value = cell.getBooleanCellValue();
                            break;
                        case XSSFCell.CELL_TYPE_BLANK:
                            value = "0";
                            break;
                        default:
                            value = cell.toString();
                    }
                    if (value == null || ((String) value).trim().equals("")) {
                        value = "0";
                    }
                }
                linked.add(value);
            }
            list.add(linked);
        }
        return list;
    }

    /**
     * 校验导入模板
     *
     * @param file
     * @param colspan
     * @param colums
     * @return
     * @throws IOException
     */
    public static Boolean checkHeader(File file, Integer colspan, List colums) throws IOException {
        String fileName = file.getName();
        String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
        List<List<Object>> excelList = null;
        if ("xls".equals(extension)) {
            excelList = subRead2003Excel(file, colspan, 0, 1);
        } else if ("xlsx".equals(extension)) {
            excelList = subRead2007Excel(file, colspan, 0, 1);
        } else {
            //// TODO: 16/8/25 非IOexception
            throw new IOException("不支持的文件类型");
        }
        if (CollectionUtils.isEmpty(excelList)) {
            return false;
        }
        if (CommonUtils.isListEquals(colums, excelList.get(0))) {
            return true;
        }
        return false;
    }

    public static Boolean readHeader(InputStream inputStream, Class recordClass) {
        boolean flag = false;
        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(inputStream);
            Sheet sheet = wb.getSheetAt(0);
            Row row = sheet.getRow(0);
            Field[] field = recordClass.getDeclaredFields();

            for (int i = 0; i < field.length; i++) {
                Field f = field[i];
                if (f.isAnnotationPresent(ExcelCol.class)) {
                    int v = f.getAnnotation(ExcelCol.class).value();
                    String title = f.getAnnotation(ExcelCol.class).title();
                    Cell cell = row.getCell(v);
                    if (cell == null) {
                        flag = false;
                        break;
                    }
                    String cellValue = cell.getStringCellValue();
                    if (title.equals(cellValue)) {
                        flag = true;
                    } else {
                        flag = false;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                if (wb != null) {
                    wb.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;

    }

    public static List<List<Object>> readExcel(File file, Integer colspan) throws IOException {
        String fileName = file.getName();
        String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
        if ("xls".equals(extension)) {
            return read2003Excel(file, colspan);
        } else if ("xlsx".equals(extension)) {
            return read2007Excel(file, colspan);
        } else {
            throw new IOException("不支持的文件类型");
        }
    }

    private static Boolean isBlankRow(XSSFRow row) {
        if (row == null) {
            return true;
        }
        try {
            XSSFCell cell = null;
            for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }

                switch (cell.getCellType()) {
                    case XSSFCell.CELL_TYPE_STRING:
                        String value = cell.getStringCellValue();
                        if (!StringUtils.isEmpty(value) && !StringUtils.isEmpty(value.trim())) {
                            return false;
                        }
                        break;
                    case XSSFCell.CELL_TYPE_BOOLEAN:
                    case XSSFCell.CELL_TYPE_NUMERIC:
                        return false;
                    case XSSFCell.CELL_TYPE_BLANK:
                        break;
                }
            }
        } catch (Exception e) {
            return true;
        }
        return true;
    }

    /**
     * 获取excel要遍历多少次
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Integer getSize(File file) throws IOException {
        Integer rowSize = getRowSize(file);
        Integer indexSize = (rowSize - 1) % ROW_LIMIT == 0 ? rowSize / ROW_LIMIT : rowSize / ROW_LIMIT + 1;
        return indexSize;
    }

    /**
     * 获取excel要遍历多少次
     *
     * @param filePath 文件路径
     * @return 页数
     * @throws IOException
     */
    public static Integer getSize(String filePath) throws IOException, InvalidFormatException {
        Integer rowSize = getRowSize(filePath);
        Integer indexSize = (rowSize - 1) % ROW_LIMIT == 0 ? rowSize / ROW_LIMIT : rowSize / ROW_LIMIT + 1;
        return indexSize;
    }

    /**
     * 获取excel行数
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Integer getRowSize(File file) throws IOException {
        HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheet = hwb.getSheetAt(0);
        return sheet.getPhysicalNumberOfRows();
    }

    /**
     * 获取excel行数
     *
     * @param filePath 文件路径
     * @return 行数
     * @throws IOException
     */
    public static Integer getRowSize(String filePath) throws InvalidFormatException, IOException {
        Workbook workbook = WorkbookFactory.create(new File(filePath));
        Sheet sheet = workbook.getSheetAt(0);
        return sheet.getPhysicalNumberOfRows();
    }

}
