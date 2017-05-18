package com.tqmall.common.util;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;

/**
 * Created by wanghui on 7/11/16.
 */
public class NumUtil {


    /**
     * 数字转换为大写字母
     * 0 -> A
     * 1 -> B
     * 26 -> A
     * 27 -> B
     * @param num
     * @return
     */
    public static char toUpperAlphalbetChar(int num) {
        return (char)((int)'A' + num % 26);
    }

    /**
     * 二位小数,四舍五入
     * @param num
     * @return
     */
    public static BigDecimal roundedBigdecimal(BigDecimal num) {
        Preconditions.checkNotNull(num);
        return num.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
