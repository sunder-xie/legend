package com.tqmall.legend.common;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by litan on 14-11-4.
 */
public class CommonUtils {

    private static List<String> list;

    static{
        list = new ArrayList<String>();
        list.add("严重损坏");
        list.add("严重");
        list.add("偏低");
        list.add("偏高");
        list.add("破损");
        list.add("立即更换");
        list.add("C.凹陷");
        list.add("D.撕(碎)裂");
        list.add("E.褶皱");
    }

    /**
     * 返回值是否应标红
     * @param value
     * @return
     */
    public static boolean isRed(String value)
    {
        if (!StringUtils.isBlank(value))
        {
            return list.contains(value);
        }
        return false;
    }

    public static String convertMoney(BigDecimal bigDecimal) {
        if (bigDecimal == null)
            return "0.00";
        String tmp = bigDecimal.toPlainString();
        return tmp.substring(0, tmp.indexOf(".") + 3);
    }

    /**
     * list相等判断
     * @param list1
     * @param list2
     * @return
     */
    public static boolean isListContainsAll(List list1,List list2){
        if (null != list1 && null != list2){
            if (list1.containsAll(list2) && list2.containsAll(list1)){
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * excel导入校验 list第一行校验是否完全相等
     *
     * @param list1
     * @param list2
     * @return
     */
    public static boolean isListEquals(List list1, List list2) {
        if (null != list1 && null != list2) {
            boolean flags = true;
            for (int i = 0; i < list1.size(); i++) {
                if (!list1.get(i).equals(list2.get(i))) {
                    return false;
                }
            }
            return flags;
        }
        return false;
    }

    /**
     * 文件大小单位适配,支持GB,MB和KB
     * @param size
     * @return
     */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1fGB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0fMB" : "%.1fMB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0fKB" : "%.1fKB", f);
        } else
            return String.format("%dB", size);
    }


    public static String delHTMLTag(String htmlStr){
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script= Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(htmlStr);
        htmlStr=m_script.replaceAll(""); //过滤script标签

        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(htmlStr);
        htmlStr=m_style.replaceAll(""); //过滤style标签

        Pattern p_html=Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(htmlStr);
        htmlStr=m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }
}
