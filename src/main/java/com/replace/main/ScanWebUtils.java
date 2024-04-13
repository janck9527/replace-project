package com.replace.main;
import java.util.ArrayList;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.replace.main.thread.CustomerThreadPoolExecutor;
import com.replace.main.utils.CompletableFutureUtils;
import com.replace.main.utils.ObjectUtils;
import com.replace.main.utils.StringUtils;
import com.replace.main.utils.http.HttpUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ProjectName: replace-project
 * @Package: com.replace.main
 * @ClassName: ScanWeb
 * @Author: janck
 * @Description:
 * @Date: 2024/4/12 10:46
 * @Version: 1.0
 */
public class ScanWebUtils {

    // 自定义线程池
    private static CustomerThreadPoolExecutor threadPoolExecutor = CustomerThreadPoolExecutor.newInstance();

    /**
     * 对
     *
     * @return
     */
    public static Map<String, String> analysisData(List<String> hostList) {
        if (ObjectUtils.isNull(hostList)) {
            return Collections.emptyMap();
        }
        CompletableFutureUtils completableFutureUtil = CompletableFutureUtils.getInstance(threadPoolExecutor);

        hostList.forEach(host -> {
            completableFutureUtil.supplyAsync(host, ()->{
                String httpUrl = StringUtils.format(Config.SEARCH_URL, host);
                String dataStr = HttpUtils.sendGet(httpUrl);
                if (StringUtils.isEmpty(dataStr)) {
                    return "";
                }
                JSONObject jsonObject = JSON.parseObject(dataStr);
                Object object = optimalData(jsonObject);
                Answer answer = JSON.parseObject(JSON.toJSONString(object), Answer.class);
                if(ObjectUtils.isNull(answer.getRecords())){
                    return "";
                }
                return answer.getRecords().get(0).getValue();
            });
        });
        completableFutureUtil.allOf();
        String mapStr = completableFutureUtil.getMapValues();
        completableFutureUtil.close();
        if (StringUtils.isEmpty(mapStr)){
            return Collections.emptyMap();
        }
        return (Map<String, String>)JSON.parseObject(mapStr,Map.class);
    }

    /**
     * 获取最佳数据
     */
    public static Object optimalData(JSONObject jsonObject) {
        if (ObjectUtils.isNull(jsonObject) || jsonObject.getIntValue("code") != 0) {
            return null;
        }
        JSONArray dataObjArray = jsonObject.getJSONArray("data");
        if (ObjectUtils.isNull(dataObjArray)) {
            return null;
        }
        // 过滤掉null元素
        // 如果不是期望的类型，返回一个空流
        List<Object> listOfAnswersLists = dataObjArray.stream()
                .filter(ObjectUtils::isNotNull) // 过滤掉null元素
                .flatMap(a1 -> {
                    if (a1 instanceof Map) {
                        Map<Integer, List<Map<Integer, List<Object>>>> aa = (Map<Integer, List<Map<Integer, List<Object>>>>) a1;
                        return aa.values().stream();
                    } else {
                        return Stream.empty(); // 如果不是期望的类型，返回一个空流
                    }
                })
                .flatMap(Collection::stream)
                .flatMap(a3 -> {
                    if (ObjectUtils.isNotNull(a3)) {
                        return a3.values().stream();
                    }
                    return Stream.empty();
                })
                .collect(Collectors.toList());
        // 找到time_consume最小的Answer
        Optional<Object> answerWithMinTimeConsume = listOfAnswersLists.stream()
                .min((a1, a2) -> {
                    Double num1 = Double.parseDouble(((Map<String, Object>) a1).get("time_consume") + "");
                    Double num2 = Double.parseDouble(((Map<String, Object>) a2).get("time_consume") + "");
                    return num1.compareTo(num2);
                });
        return answerWithMinTimeConsume.get();
    }

    public static void main(String[] args) {
        System.out.println(analysisData(Arrays.asList(Config.HOST_LIST)));
    }

}
