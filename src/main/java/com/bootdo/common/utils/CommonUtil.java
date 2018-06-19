package com.bootdo.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public final class CommonUtil {

    public final static String DEFAULT_PASSWORD = "123456";// 默认登录密码

    // private final static String pattern = "\\A";
    private CommonUtil() {
    }

    /**
     * 32位唯一字符串（十六进制字符）
     *
     * @return
     */
    public static String genNonceStr() {
        Random random = new Random();
        return MD5Utils.encrypt(String.valueOf(random.nextInt(10000)));
    }

    /**
     * 获取IP
     *
     * @return
     */
    public static String getCurrentIp() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return isEmpty(addr) ? "0.0.0.0" : addr.getHostAddress().toString();
    }

    /**
     * 判断空值
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (null == obj) {
            return true;
        }
        if (obj instanceof String) {
            String s = (String) obj;
            return s.trim().length() == 0 || "null".equals(s) || "NULL".equals(s) || "undefined".equals(s);
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection) {
            @SuppressWarnings("rawtypes")
            Collection c = (Collection) obj;
            return c.isEmpty();
        }
        if (obj instanceof Map) {
            @SuppressWarnings("rawtypes")
            Map m = (Map) obj;
            return m.isEmpty();
        }
        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 流转字符串
     *
     * @param content
     * @return
     */
    public static String getStringFromInputStream(InputStream content, String charSet) {
        if (CommonUtil.isEmpty(content))
            return null;
        Scanner scanner = new Scanner(content, CommonUtil.isEmpty(charSet) ? "UTF-8" : charSet);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNext()) {
            sb.append(scanner.next());
        }
        scanner.close();
        try {
            content.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 产生一个6位随机验证码
     *
     * @return
     */
    public static int getRandom6() {
        return (int) ((Math.random() * 9 + 1) * 100000);
    }

    /**
     * 产生一个固定长度的数字字符串
     *
     * @param length
     * @return
     */
    public static String getRandomNumberStr(int length) {
        StringBuffer b = new StringBuffer("1");
        for (int i = 0; i < length - 1; i++) {
            b.append("0");
        }
        long p = Long.parseLong(b.toString());
        return String.valueOf((long) ((Math.random() * 9 + 1) * p));
    }

    /**
     * 8位随机字符串
     *
     * @return
     */
    public static String generateShortUuid() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        Random r = new Random();
        int nextInt = r.nextInt(7);
        return uuid.substring(nextInt, nextInt + 8);
    }

    /**
     * 8位随机数字
     *
     * @return
     */
    public static String generateRandomNumber() {
        return generateRandomNumber(8);
    }

    public static String generateRandomNumber(int length) {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            str.append(random.nextInt(9));
        }
        return str.toString();
    }

    public static void main(String[] args) {
        System.out.println(genNonceStr());
    }
}
