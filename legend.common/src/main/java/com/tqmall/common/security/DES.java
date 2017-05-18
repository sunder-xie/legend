package com.tqmall.common.security;

import com.tqmall.legend.common.PayError;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * OK
 * Created by Jeremy on 15/6/16.
 */
@Slf4j
public class DES {

    public final static String DES_KEY = "R3KdjTyu";
    public final static String DES_IV = "20150316";
    public final static String DES_DB_KEY = "pZ67jxhn";

    public static String encryptDES(String encryptString) {
        try {
            return encryptDES(encryptString, DES_KEY);
        } catch (Exception e) {
            log.error("连连支付参数加密失败", e);
            return PayError.ENCRYPT_ERROR;
        }
    }

    public static String encryptDESDB(String encryptString) {
        try {
            return encryptDES(encryptString, DES_DB_KEY);
        } catch (Exception e) {
            log.error("连连支付参数加密失败", e);
            return PayError.DESCRY_ERROR;
        }
    }

    public static String encryptDES(String encryptString, String encryptKey) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(DES_IV.getBytes());
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
        return Base64.encode(encryptedData);
    }

    public static String decryptDES(String decryptString) {
        try {
            return decryptDES(decryptString, DES_KEY);
        } catch (Exception e) {
            log.error("连连支付参数解密失败", e);
            return  PayError.DESCRY_ERROR;
        }
    }

    public static String decryptDESDB(String decryptString) {
        try {
            return decryptDES(decryptString, DES_DB_KEY);
        } catch (Exception e) {
            log.error("连连支付参数解密失败", e);
            return PayError.DESCRY_ERROR;
        }
    }

    public static String decryptDES(String decryptString, String decryptKey) throws Exception {
        byte[] byteMi = Base64.decode(decryptString);
        IvParameterSpec zeroIv = new IvParameterSpec(DES_IV.getBytes());
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte decryptedData[] = cipher.doFinal(byteMi);
        return new String(decryptedData);
    }
}
