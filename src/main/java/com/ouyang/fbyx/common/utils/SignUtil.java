package com.ouyang.fbyx.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @description: 校验微信签名工具类
 * @author: ouyangxingjie
 * @create: 2021/7/1 21:28
 **/
public class SignUtil {
    //微信公众号接口中配置的token
    private static String token = "ouyangxingjie";

    /**
     * 公众号接入验证
     * @param signature 微信签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @return 是否是微信服务器验证消息
     */
    public static boolean checkSignature(String signature, String timestamp, String nonce) {
        //将token timestamp nonce三个参数字典序排序
        String[] arr = new String[] {token, timestamp, nonce};
        Arrays.sort(arr);
        //三个字符串拼接成一个警醒sha1加密
        StringBuilder sb = new StringBuilder();
        for (String s: arr) {
            sb.append(s);
        }
        MessageDigest md = null;
        String tmpStr = null; //加密后字符串
        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(sb.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        sb = null;
        System.out.println("加密后的字符串是:" + tmpStr);
        //将加密后的字符串与传过来的签名对比,如果一样则是微信的请求
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
    }

    /**
     * 将字节数组转为十六进制字符串
     * @param bytes 需要转换的字节数组
     * @return 转换后的字符串
     */
    private static String byteToStr(byte[] bytes) {
        String strDigest = "";
        for (int i = 0; i < bytes.length; i++) {
            strDigest += byteToHexStr(bytes[i]);
        }
        return strDigest;
    }

    /**
     * 将字符转换为16进制字符串
     * @param bt
     * @return
     */
    private static String byteToHexStr(byte bt) {
        char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = digit[(bt >>> 4) & 0X0F];
        tempArr[1] = digit[bt & 0X0F];
        String s = new String(tempArr);
        return s;
    }
}
