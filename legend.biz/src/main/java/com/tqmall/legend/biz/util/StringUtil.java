package com.tqmall.legend.biz.util;

import com.tqmall.common.Constants;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by sirong.zhusr on 14-7-17.
 */
public class StringUtil {
    public static boolean isStringEmpty(String str) {
        if (null == str || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    public static boolean isNotStringEmpty(String str) {
        if (null == str || "".equals(str.trim())) {
            return false;
        }
        return true;
    }

    public static boolean isNull(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static Integer[] getIntItemsFromString(String srcString, String separator) {
        if (isNull(srcString) || isNull(separator)) {
            return new Integer[0];
        }
        String[] items = srcString.split(separator);
        Integer[] itemsArray = new Integer[items.length];
        for (int i = 0; i < items.length; i++) {
            itemsArray[i] = Integer.parseInt(items[i]);
        }
        return itemsArray;
    }

    private static String[] getStringItemsFromString(String srcString, String separator) {
        if (isNull(srcString) || isNull(separator)) {
            return new String[0];
        }
        String[] strings = srcString.split(separator);
        for (int i = 0; i < strings.length; i++) {
            strings[i] = strings[i].trim();
        }
        return strings;
    }

    public static List<Integer> getIntListFromString(String srcString, String separator) {
        Integer[] intItems = getIntItemsFromString(srcString, separator);
        return Arrays.asList(intItems);
    }

    public static List<String> getStringListFromString(String srcString, String separator) {
        String[] intItems = getStringItemsFromString(srcString, separator);
        return Arrays.asList(intItems);
    }

    public static String appendList(String srcStr, List<?> list, String separator) {
        if (list == null || list.size() == 0) {
            return srcStr != null ? srcStr : "";
        }
        for (Object item : list) {
            srcStr = appendItem(srcStr, separator, item);
        }
        return srcStr != null ? srcStr : "";
    }

    public static String appendItem(String srcStr, String separator, Object item) {
        if (isNull(srcStr)) {
            srcStr = String.valueOf(item);
        } else {
            srcStr = srcStr + separator + String.valueOf(item);
        }
        return srcStr;
    }

    public static String appendList(String srcStr, List<?> list, String fieldName, String separator) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (list == null || list.size() == 0) {
            return srcStr;
        }
        String first = fieldName.substring(0, 1);
        String getterName = "get" + fieldName.replaceFirst(first, first.toUpperCase());
        Method getter = list.get(0).getClass().getMethod(getterName);
        for (Object item : list) {
            srcStr = appendItem(srcStr, separator, getter.invoke(item, (Object[]) null));
        }
        return srcStr;
    }

    /**
     * 判断手机号码
     *
     * @param mobile
     * @return
     */
    public static boolean isMobileNO(String mobile) {
        if (!StringUtils.isEmpty(mobile) && mobile.length() == 11) {
            Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
            Matcher m = p.matcher(mobile);
            return m.matches();
        }
        return false;
    }

    /**
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        if (!StringUtils.isEmpty(phone)) {
            Pattern p = Pattern.compile("(^0\\d{2,3}-*\\d{7,8}$)|(^0\\d{2,3}-*\\d{7,8}-\\d+$)");
            Matcher m = p.matcher(phone);
            return m.matches();
        }
        return false;
    }

    /**
     * 校验是否15或18位身份证号码
     *
     * @param idCard
     * @return
     */
    public static boolean isIdCard(String idCard) {
        if (!StringUtils.isEmpty(idCard)) {
            Pattern p = Pattern.compile("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$");
            Matcher m = p.matcher(idCard);
            return m.matches();
        }
        return false;
    }


    /**
     * 是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        if (!StringUtils.isEmpty(str)) {
            Pattern pat = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher matcher = pat.matcher(str);
            return matcher.find();
        }
        return false;
    }

    /**
     * 验证是否含有非法字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isSpecialString(String str) throws PatternSyntaxException {

        if (!StringUtils.isEmpty(str)) {
            String regEx = "^[0-9a-zA-Z-]+$";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            return m.matches();
        }
        return true;
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     *
     * @param s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static int length(String s) {
        if (StringUtils.isEmpty(s)) {
            return 0;
        }
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    /**
     * @param c
     * @return
     */
    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    /**
     * 匹配是否是全数字组成[0-9]
     *
     * @param numberString
     * @return
     */
    public static boolean isNumberOnly(String numberString) {
        if (StringUtils.isEmpty(numberString)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher isNum = pattern.matcher(numberString);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * @param vin
     * @return
     */
    public static boolean isVinString(String vin) {
        if (!StringUtils.isEmpty(vin) && length(vin) == Constants.VIN_LENGTH_FLAG) {
            // 只允许字母和数字
            String regEx = "^[A-Za-z0-9]+$";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(vin);
            return m.matches();
        }
        return false;
    }

    /**
     * 匹配是否数字
     * 包含正负整数小数
     *
     * @param numberString
     * @return
     */
    public static boolean isNumeric(String numberString) {
        Pattern pattern = Pattern.compile("(^-?[1-9]\\d*$)|(^-?[1-9]\\d*\\.\\d+|-?^0\\.\\d*[1-9]\\d+$)");
        Matcher isNum = pattern.matcher(numberString);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 处理不同格式的数据
     *
     * @param time
     * @return
     */
    public static String formatTime(String time) {
        if (time.indexOf("/") > 0) {
            time = time.replace("/", "-");
        }
        if (time.indexOf(".") > 0) {
            time = time.replace(".", "-");
        }
        return time;
    }

    public static boolean checkCarLicense(String license) {
        if (StringUtils.isBlank(license)) {
            return false;
        }
        if (Constants.STRING_LENGTH_MIN > license.length() || license.length() > Constants.STRING_LENGTH_MIN_LICENSE) {
            return false;
        }
        return true;
    }

    public static boolean isCarLicense(String license) {
        if (StringUtils.isEmpty(license)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[\\u4e00-\\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$");
        Matcher isNum = pattern.matcher(license);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断时间是否符合00:00:00或者00:00的格式
     *
     * @param time
     * @return
     */
    public static boolean isPatternTime(String time) {
        if (StringUtils.isEmpty(time)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^(\\d{1,2}:\\d{1,2}:\\d{1,2})||(\\d{1,2}:\\d{1,2})$");
        Matcher isNum = pattern.matcher(time);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 检验时间是否符合00:00:00
     *
     * @param time
     * @return
     */
    public static String checkTime(String time) {
        if (StringUtils.isEmpty(time)) {
            return "";
        }
        String[] times = StringUtils.split(time.trim(), ":");
        if (times.length >= 2 && times.length < 3) {
            time = time + ":00";
        } else if (times.length < 2) {
            time = time + ":00:00";
        }
        return time;
    }

    /**
     * 全角字符串转换半角字符串
     *
     * @param fullWidthStr 非空的全角字符串
     * @return 半角字符串
     */
    public static String fullWidth2halfWidth(String fullWidthStr) {
        if (isStringEmpty(fullWidthStr)) {
            return "";
        }
        char[] charArray = fullWidthStr.toCharArray();
        //对全角字符转换的char数组遍历
        for (int i = 0; i < charArray.length; ++i) {
            int charIntValue = (int) charArray[i];
            //如果符合转换关系,将对应下标之间减掉偏移量65248;如果是空格的话,直接做转换
            if (charIntValue >= 65281 && charIntValue <= 65374) {
                charArray[i] = (char) (charIntValue - 65248);
            } else if (charIntValue == 12288) {
                charArray[i] = (char) 32;
            }
        }
        return new String(charArray);
    }


    /**
     * 解析字符串成数组
     *
     * @param ids
     * @return
     */
    public static Long[] parseIds(String ids) {
        String[] idList = ids.split(",");
        int size = idList.length;
        Long[] proxyIds = new Long[size];
        for (int i = 0; i < size; i++) {
            proxyIds[i] = Long.valueOf(idList[i]);
        }
        return proxyIds;
    }

    /**
     * 银行账号格式化
     *
     * @param cardNumber
     * @return
     */
    public static String formateBankCardNumber(String cardNumber) {
        if (StringUtils.isBlank(cardNumber)) {
            return "";
        }
        if (cardNumber.length() < 5) {
            return cardNumber;
        }
        return String.format("%s**********%s", cardNumber.substring(0, 3), cardNumber.substring(cardNumber.length() - 4));
    }


    /**
     * 身份证格式校验
     *
     * @param idCard 身份证号码
     * @return
     */
    public static Boolean validateIDCard(String idCard) {
        String card = "";
        // 判断号码的长度 15位或18位
        if (idCard.length() != 15 && idCard.length() != 18) {
            return false;
        }

        // 18位身份证前17位位数字，如果是15位的身份证则所有号码都为数字
        if (idCard.length() == 18) {
            card = idCard.substring(0, 17);
        } else if (idCard.length() == 15) {
            card = idCard.substring(0, 6) + "19" + idCard.substring(6, 15);
        }
        if (!hasNumeric(card)) {
            return false;
        }
        // 判断出生年月是否有效
        String strYear = card.substring(6, 10);// 年份
        String strMonth = card.substring(10, 12);// 月份
        String strDay = card.substring(12, 14);// 日期
        if (!isDate(strYear + "-" + strMonth + "-" + strDay)) {
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            return false;
        }

        // 判断地区码是否有效
        Hashtable areacode = GetAreaCode();
        //如果身份证前两位的地区码不在Hashtable，则地区码有误
        if (areacode.get(card.substring(0, 2)) == null) {
            return false;
        }

        if (!isVarifyCode(card, idCard)) {
            return false;
        }
        return true;
    }


    /**
     * 判断字符串是否为数字,0-9重复0次或者多次
     *
     * @return
     */
    private static boolean hasNumeric(String strnum) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(strnum);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：判断字符串出生日期是否符合正则表达式：包括年月日，闰年、平年和每月31天、30天和闰月的28天或者29天
     *
     * @return
     */
    private static boolean isDate(String strDate) {
        Pattern pattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断第18位校验码是否正确
     * 第18位校验码的计算方式：
     * 1. 对前17位数字本体码加权求和
     * 公式为：S = Sum(Ai * Wi), i = 0, ... , 16
     * 其中Ai表示第i个位置上的身份证号码数字值，Wi表示第i位置上的加权因子，其各位对应的值依次为： 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     * 2. 用11对计算结果取模
     * Y = mod(S, 11)
     * 3. 根据模的值得到对应的校验码
     * 对应关系为：
     * Y值：     0  1  2  3  4  5  6  7  8  9  10
     * 校验码： 1  0  X  9  8  7  6  5  4  3   2
     */
    private static boolean isVarifyCode(String Ai, String IDStr) {
        String[] VarifyCode = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
        String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum = sum + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = sum % 11;
        String strVerifyCode = VarifyCode[modValue];
        Ai = Ai + strVerifyCode;
        if (IDStr.length() == 18) {
            if (!Ai.equals(IDStr)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将所有地址编码保存在一个Hashtable中
     *
     * @return Hashtable 对象
     */

    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }


    public static void main(String[] args) throws ParseException {

        //从控制端输入用户身份证
        Scanner s = new Scanner(System.in);
        System.out.println("请输入你的身份证号码：");
        String IdCard = new String(s.next());
        //将身份证最后一位的x转换为大写，便于统一
        IdCard = IdCard.toUpperCase();
        System.out.println(validateIDCard(IdCard));
    }
}
