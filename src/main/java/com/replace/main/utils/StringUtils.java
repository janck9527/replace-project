package com.replace.main.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.replace.main.utils.text.StrFormatter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * 字符串工具类
 *
 * @author xuweijie
 */
public class StringUtils {
    /**
     * 空字符串
     */
    private static final String NULLSTR = "";

    /**
     * 下划线
     */
    private static final char SEPARATOR = '_';

    /**
     * 获取参数不为空值
     *
     * @param value defaultValue 要判断的value
     * @return value 返回值
     */
    public static <T> T nvl(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * * 判断一个Collection是否为空， 包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll) {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * 判断一个Collection是否非空，包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * * 判断一个对象数组是否为空
     *
     * @param objects 要判断的对象数组
     *                * @return true：为空 false：非空
     */
    public static boolean isEmpty(Object[] objects) {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * * 判断一个对象数组是否非空
     *
     * @param objects 要判断的对象数组
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Object[] objects) {
        return !isEmpty(objects);
    }

    /**
     * * 判断一个Map是否为空
     *
     * @param map 要判断的Map
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || map.isEmpty();
    }

    /**
     * * 判断一个Map是否为空
     *
     * @param map 要判断的Map
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * * 判断一个字符串是否为空串
     *
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str) {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * * 判断一个字符串是否为非空串
     *
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * * 判断一个对象是否非空
     *
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }

    /**
     * * 判断一个对象是否是数组类型（Java基本型别的数组）
     *
     * @param object 对象
     * @return true：是数组 false：不是数组
     */
    public static boolean isArray(Object object) {
        return isNotNull(object) && object.getClass().isArray();
    }

    /**
     * 去空格
     */
    public static String trim(String str) {
        return (str == null ? "" : str.trim());
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始
     * @return 结果
     */
    public static String substring(final String str, int start) {
        if (str == null) {
            return NULLSTR;
        }

        if (start < 0) {
            start = str.length() + start;
        }

        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return NULLSTR;
        }

        return str.substring(start);
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始
     * @param end   结束
     * @return 结果
     */
    public static String substring(final String str, int start, int end) {
        if (str == null) {
            return NULLSTR;
        }

        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }

        if (end > str.length()) {
            end = str.length();
        }

        if (start > end) {
            return NULLSTR;
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * 判断是否为空，并且不是空白字符
     *
     * @param str 要判断的value
     * @return 结果
     */
    public static boolean hasText(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 驼峰转下划线命名
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase = true;
        // 当前字符是否大写
        boolean curreCharIsUpperCase = true;
        // 下一字符是否大写
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i > 0) {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            } else {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1)) {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase) {
                sb.append(SEPARATOR);
            } else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase) {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs) {
        if (str != null && strs != null) {
            for (String s : strs) {
                if (str.equalsIgnoreCase(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String convertToCamelCase(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母大写
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 首字母大写
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * 驼峰式命名法 例如：user_name->userName
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }

    /**
     * 数字左边补齐0，使之达到指定长度。注意，如果数字转换为字符串后，长度大于size，则只保留 最后size个字符。
     *
     * @param num  数字对象
     * @param size 字符串指定长度
     * @return 返回数字的字符串格式，该字符串为指定长度。
     */
    public static String padl(final Number num, final int size) {
        return padl(num.toString(), size, '0');
    }

    /**
     * 字符串左补齐。如果原始字符串s长度大于size，则只保留最后size个字符。
     *
     * @param s    原始字符串
     * @param size 字符串指定长度
     * @param c    用于补齐的字符
     * @return 返回指定长度的字符串，由原字符串左补齐或截取得到。
     */
    public static final String padl(final String s, final int size, final char c) {
        final StringBuilder sb = new StringBuilder(size);
        if (s != null) {
            final int len = s.length();
            if (s.length() <= size) {
                for (int i = size - len; i > 0; i--) {
                    sb.append(c);
                }
                sb.append(s);
            } else {
                return s.substring(len - size, len);
            }
        } else {
            for (int i = size; i > 0; i--) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static Map<String, Object> splitQuery(String query) throws UnsupportedEncodingException {
        Map<String, Object> query_pairs = new LinkedHashMap();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }


    /**
     * 判断给定的set列表中是否包含数组array 判断给定的数组array中是否包含给定的元素value
     *
     * @param collection 给定的集合
     * @param array      给定的数组
     * @return boolean 结果
     */
    public static boolean containsAny(Collection<String> collection, String... array) {
        if (isEmpty(collection) || isEmpty(array)) {
            return false;
        } else {
            for (String str : array) {
                if (collection.contains(str)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static void main(String[] args) {
        //String result = JSON.toJSONString(StringUtils.changeJsonObj(JSONObject.parseObject("{\"c2rkod\":[{\"soUClO\":0,\"trbtfList\":[{\"kicnrr\":\"18.190.153.11\",\"mwMuWc\":0,\"hyyOwO\":\"Hilliard\",\"uxmZSS\":27049,\"nmisMV\":\"US\",\"pbKsWh\":1},{\"kicnrr\":\"18.190.153.11\",\"mwMuWc\":1,\"hyyOwO\":\"Hilliard\",\"uxmZSS\":33001,\"nmisMV\":\"US\",\"pbKsWh\":1}],\"yjxzUj\":\"1.0.0\"}],\"dtutck\":[[{\"name\":\"cs-YS-002\",\"keyId\":\"ca-app-pub-3940256099942544/2247696110\",\"type\":2},{\"name\":\"cs-YS-001\",\"keyId\":\"ca-app-pub-3940256099942544/2247696110\",\"type\":2},{\"name\":\"cs-CY-002\",\"keyId\":\"ca-app-pub-3940256099942544/1033173712\",\"type\":1},{\"name\":\"cs-CY-001\",\"keyId\":\"ca-app-pub-3940256099942544/1033173712\",\"type\":1},{\"name\":\"cs-KP\",\"keyId\":\"ca-app-pub-3940256099942544/3419835294\",\"type\":0}]]}"), JSON.parseObject(APP11_REPLACE, Map.class)));

        //System.out.println(result);
//        System.out.println(JSON.parse("{\"www\": {\"de\":\"" + JSON.toJSONString(StringUtils.splitNum("reihbyvfrehibi", 4)) + "\"}}"));
//        System.out.println(JSONObject.parseObject(str));
    }

    /**
     * 根据传过来的数字，将字符串切割成相等的字符串
     *
     * @param query
     * @param num
     * @return
     */
    public static List<String> splitNum(String query, Integer num) {
        if (isEmpty(query)) {
            return null;
        }
        if (ObjectUtils.isNull(num)) {
            num = 1;
        }

        List<String> list = new ArrayList<>();
        int packetLength = query.length() / num;
        for (int i = 0; i < num - 1; i++) {
            list.add(query.substring(i * packetLength, i * packetLength + packetLength));
        }
        list.add(query.substring((num - 1) * packetLength, query.length()));
        return list;
    }

    public static String changeJson(String result, String jsonReplace) {
        try {
            result = JSON.toJSONString(StringUtils.changeJsonObj(JSONObject.parseObject(result), JSON.parseObject(jsonReplace, Map.class)));
        } catch (Exception e) {
            result = JSON.toJSONString(StringUtils.changeJsonArray(JSON.parseArray(result), JSON.parseObject(jsonReplace, Map.class)));
        }
        return result;
    }


    /**
     * 实现json数据按照替换规则进行替换
     * <p>
     *     * @param jsonObj  原始json数据
     * <p>
     *     * @param keyMap  key替换规则
     * <p>
     *     * @param valueMap value替换规则
     * <p>
     *     * @return 替换后的josn数据
     */

    public static List<Object> changeJsonArray(JSONArray jsonArray, Map<String, String> keyMap) {
        List<Object> list = new ArrayList<>();
        for (Object obj : jsonArray) {
            if (obj instanceof String) {
                list.add(obj);
            } else if (obj instanceof JSONObject) {
                list.add(changeJsonObj((JSONObject) obj, keyMap));
            } else if (obj instanceof JSONArray) {
                JSONArray jsonArr = (JSONArray) obj;
                for (int i = 0; i < jsonArr.size(); i++) {
                    Object object = jsonArr.get(i);
                    if (ObjectUtils.isNotNull(object)) {
                        if (object instanceof JSONObject) {
                            list.add(changeJsonObj((JSONObject) object, keyMap));
                        } else if (object instanceof JSONArray) {
                            list.add(changeJsonArray((JSONArray) object, keyMap));
                        } else if (object instanceof String) {
                            list.add(object);
                        }
                    }
                }
            }
        }
        return list;
    }

    public static JSONObject changeJsonObj(JSONObject jsonObj, Map<String, String> keyMap) {
        JSONObject resJson = new JSONObject();
        Set<String> keySet = jsonObj.keySet();
        for (String key : keySet) {
            //改变json数据的key值
            String resKey = keyMap.get(key) == null ? key : keyMap.get(key);
            Object obj = jsonObj.get(key);
            if (obj instanceof JSONObject) {
                resJson.put(resKey, changeJsonObj((JSONObject) obj, keyMap));
            } else if (obj instanceof JSONArray) {
                resJson.put(resKey, changeJsonArr((JSONArray) obj, keyMap));
            } else {
                //改变json的values值
                resJson.put(resKey, jsonObj.get(key));
            }
        }
        return resJson;
    }


    public static JSONArray changeJsonArr(JSONArray jsonArr, Map<String, String> keyMap) {
        JSONArray resJson = new JSONArray();
        for (int i = 0; i < jsonArr.size(); i++) {
            Object obj = jsonArr.get(i);
            if (ObjectUtils.isNotNull(obj)) {
                if (obj instanceof JSONObject) {
                    resJson.add(changeJsonObj((JSONObject) obj, keyMap));
                } else if (obj instanceof JSONArray) {
                    resJson.add(changeJsonArr((JSONArray) obj, keyMap));
                } else if (obj instanceof String) {
                    resJson.add(obj);
                }
            }
        }
        return resJson;
    }

    public static String format(String template, Object... params) {
        if (isEmpty(params) || isEmpty(template)) {
            return template;
        }
        return StrFormatter.format(template, params);
    }

    //产生8位随机数
    public static String makeRandomPassword(int len) {
        len -= 2;
        char charr[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_".toCharArray();
        char cha[] = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        sb.append(cha[r.nextInt(cha.length - 1)]);
        sb.append(cha[r.nextInt(cha.length - 1)]);
        for (int x = 0; x < len; ++x) {
            sb.append(charr[r.nextInt(charr.length - 1)]);
        }
        return sb.toString();
    }

}
