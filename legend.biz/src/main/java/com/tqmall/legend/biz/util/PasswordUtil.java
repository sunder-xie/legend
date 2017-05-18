package com.tqmall.legend.biz.util;

import org.apache.commons.lang3.RandomUtils;

import java.util.UUID;

/**
 * Created by lilige on 15/11/6.
 */
public class PasswordUtil {
    /**
     * 获取8位随机数
     * 本算法利用36+31个可打印字符，通过随机生成32位UUID，由于UUID都为十六进制，所以将UUID分成8组，每4个为一组，然后通过模36，31，13操作，结果作为索引取出字符，
     * 请放120个心，你得到的密码是不会重复的，除非你运气极好极好的
     * @return
     */
    public static String genePassword() {
        String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
                "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
                "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9"};
        String[] fuhao = new String[]{ "~", "!", "@", "#", "$", "%", "^", "&",
                "*", "(", ")", "-", "_", "=", "+", "{", "}", "[", "]",
                ":", ";", "\"", "<",">","?"};
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String char1 = "";
        String char2 = "";
        for (int i = 0; i < 7; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 36]);
            char1 = fuhao[x % 25];
            char2 = fuhao[x % 20];
        }
        int i = RandomUtils.nextInt(0, 7);
        shortBuffer.replace(i,i,char1);
        if (i%2 == 0){
            int j = RandomUtils.nextInt(0, 7);
            shortBuffer.replace(j,j+1,char2);
        }
        return shortBuffer.toString();
    }

    /**
     * 获取8位随机密码
     * @return
     */
    public static Boolean checkPassword(String password){
        String reg = "(?=.*[\\d]+)(?=.*[a-zA-Z]+).{6,12}";
        return password.matches(reg);
    }

    public static void main(String[] args) {
        System.out.println(checkPassword("s11111s%"));
    }
}
