package com.bootdo.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.Collator;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static final String EMPTY = "";

    private static final ThreadLocal<Locale> threadLocal = new ThreadLocal<Locale>() {
        protected synchronized Locale initialValue() {
            return Locale.getDefault();
        }
    };

    private static Map<String, Locale> localeMap = new ConcurrentHashMap<String, Locale>(300);
    static {
        for (Locale l : Locale.getAvailableLocales()) {
            localeMap.put(l.toString(), l);
        }
    }

    /**
     * 根据Locale的名称获取Locale
     * @param name
     * @return
     */
    public static Locale localeFromString(String name) {
        return localeMap.get(name);
    }

    /**
     * 设置当前线程的Locale
     * @param l
     */
    public static void setLocale(Locale l) {
        threadLocal.set(l);
    }

    /**
     * 获取当前线程的Locale
     * @return
     */
    public static Locale getLocale() {
        return threadLocal.get();
    }

    /**
     * 获取当前Locale的字符串
     * @return
     */
    public static String getLocaleString() {
        return getLocale().toString();
    }

    public static int compare(String o1, String o2) {
        if (o1 == null) {
            return 1;
        }
        if (o2 == null) {
            return -1;
        }
        return Collator.getInstance(StringUtil.getLocale()).compare(o1, o2);
    }

    public static Collator getComparator() {
        return Collator.getInstance(StringUtil.getLocale());
    }

    /**
     * 如果字符串长度不够，在前面部0，如果长度超过，则截取前面的
     * @param num
     * @param length
     * @return
     */
    public static String prefix0(long num, int length) {
        return prefix0(String.valueOf(num), length);
    }

    public static String prefix0(int num, int length) {
        return prefix0(String.valueOf(num), length);
    }

    /**
     * 如果字符串长度不够，在前面部0，如果长度超过，则截取前面的
     * @param value
     * @param length
     * @return
     */
    public static String prefix0(String value, int length) {
        if (value == null) {
            value = "";
        }
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < length - value.length(); i++) {
            result.append('0');
        }
        result.append(value);

        if (result.length() > length) {
            return result.substring(0, length);
        }

        return result.toString();
    }

    private static String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int number = ThreadLocalRandom.current().nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 产生大写UUID
     * @return
     */
    public static String getUpperUUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    /**
     * 生成格式化的信息
     * 使用参数替换模板中的占位符
     * @param templateMessage
     * @param paras
     * @return
     */
    public static String getFormatedMessage(String templateMessage, Object... paras) {
        if (templateMessage == null) {
            return "";
        }
        // 使用传入的参数替换模板中的占位符
        String formatedMsg = MessageFormat.format(templateMessage, paras);
        // 将未处理的占位符替换，用于处理少传参数的情况
        String regex = "\\{[0|[1-9]\\d*]\\}";
        String replacement = "";
        formatedMsg = formatedMsg.replaceAll(regex, replacement);
        return formatedMsg;
    }

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        if (str == null || "".equals(str.trim()))
            return true;
        else
            return false;
    }
    
    
    /** * 判断字符串是否为空
    * @param str
    * @return
    */
   public static boolean isBlankOrNull(String str) {
       if (str == null || "".equals(str.trim())|| "null".equals(str.trim()))
           return true;
       else
           return false;
   }

    /**
     * 判断字符串是否为不为空
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        if (str != null && !"".equals(str))
            return true;
        else
            return false;
    }

    /**
     * 产生一个流水编号
     * @param pre
     * @return
     */
    public static String getFlowingCode(String pre) {
        return pre + DateUtil.format("yyyyMMddHHmmssSSS", new Date()) + CommonUtil.getRandom6();
    }
    
    public static String getCustomerNo(String customType ){
    	if(customType==null || "".equals(customType))return null;
    	String prefix=customType.substring(customType.length()-1);
    	
    	
    	return prefix+CommonUtil.getRandomNumberStr(8);
    }

    /**
     * 数组已分隔符方式转字符串
     * @param list
     * @param sign
     * @return
     */
    public static String array2String(Object[] list, String sign) {
        StringBuilder sbd = new StringBuilder();
        for (Object o : list) {
            sbd.append(String.valueOf(o));
            sbd.append(sign);
        }
        if (sbd.length() > 0) {
            return sbd.substring(0, sbd.length() - 1);
        }
        return EMPTY;
    }

    /**
     * null字符串返回""
     * @param param
     * @return
     */
    public static String toNonNull(String param) {
        return param == null ? EMPTY : param;
    }
    
    /**
     * 证件脱敏
     * @param cardNo
     * @return
     */
    public static String cardNoHide(String cardNo){
    	if(CommonUtil.isEmpty(cardNo)){
    		return null;
    	}
    	if(cardNo.length()>=15){
    		cardNo=cardNo.replace(cardNo.substring(1, cardNo.length()-4), "*************");
    		
    	}
			
    	return cardNo;
    }
    
    /**
     * 将特殊符号替换为空
     * @param str
     * @return
     */
    public static String replaceSpecialChars(String str){
    	if(CommonUtil.isEmpty(str))return null;
    	Pattern p = Pattern.compile("\\s*|\t|\r|\n");
    	Matcher m = p.matcher(str);
    	return m.replaceAll("");
    	
    }
    
    /**
     * <pre>
     * 取字符串的后几位
     * 
     * 用例：
     * System.out.println(endSubString(null, 4)); // null
     * System.out.println(endSubString("12345", 4)); // 2345
     * System.out.println(endSubString("1234", 4)); // 1234
     * System.out.println(endSubString("123", 4)); // 123
     * System.out.println(endSubString("12", 4)); // 12
     * System.out.println(endSubString("", 4)); // 
     * System.out.println(endSubString("", 4)); // 
     * </pre>
     * @param str
     * @param size
     * @return
     */
    public static String endSubString(String str, int size) {
    		if(StringUtils.isBlank(str) || size < 0) {
    			return str;
    		}
    		
    		int length = str.length();
    		if(length <= size) {
    			return str;
    		} else {
    			return str.substring(length - size, length);
    		}
    		
    }
    
    public static String startSubString(String str, int size) {
    	if(StringUtils.isBlank(str) || size < 0) {
			return str;
		}
    	int length = str.length();
    	if(length <= size) {
			return str;
		} else {
			return str.substring(0, size);
		}
    }
    
    public static String changeMap2EL(Map<String, Object> map){
    	if(CommonUtil.isEmpty(map))return null;
    	Iterator<Entry<String, Object>> it= map.entrySet().iterator();
		StringBuffer sb=new StringBuffer(";");
		while(it.hasNext()){
			Entry<String, Object> e= it.next();
			String key=e.getKey();
			Object val=e.getValue();
			sb.append(";");
			if(val instanceof Integer){
				sb.append("${"+key+"=="+val+"}");
			}
			if(val instanceof String){
				sb.append("${"+key+"==\""+val+"\"}");
			}
		}
		return sb.toString().replaceAll(";;", "");
    }
    
    /**
     * 校验手机号码
     * @param mobile
     * @return
     */
    public static boolean checkMobile(String mobile){
    	String rule="^1[0-9]{10}$";
    	return Pattern.matches(rule, mobile);
    	
    }
  
    public static void main(String[] args) {
		System.out.println(endSubString(null, 4)); // null
		System.out.println(endSubString("12345", 4)); // 2345
		System.out.println(endSubString("1234", 4)); // 1234
		System.out.println(endSubString("123", 4)); // 123
		System.out.println(endSubString("12", 4)); // 12
		System.out.println(endSubString("", 4)); //
		System.out.println(endSubString("", 4)); //
		
    	String ret=" I am a, \r\nI am Hello ok, \n new line \rffdsa! ";
    	System.out.println("before:"+ret);
    	System.out.println("after:"+replaceSpecialChars(ret));
    	
    	System.out.println("is blank=="+StringUtil.isBlank("null"));
    	
	}
    
}
