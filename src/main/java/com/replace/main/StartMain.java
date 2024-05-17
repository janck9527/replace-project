package com.replace.main;

import com.alibaba.fastjson2.JSON;
import com.replace.main.utils.StringUtils;
import com.replace.main.utils.file.FileUtils;
import com.replace.main.utils.watch.TraceWatch;

import java.util.Arrays;
import java.util.Map;

/**
 * @ProjectName: replace-project
 * @Package: com.replace.main
 * @ClassName: StartMain
 * @Author: janck
 * @Description:
 * @Date: 2024/4/12 10:44
 * @Version: 1.0
 */
public class StartMain {
    public static void main(String[] args) {
        TraceWatch traceWatch = new TraceWatch();
        traceWatch.start("host文件时间");
//        Map<String, String> stringStringMap = ScanWebUtils.analysisData(Arrays.asList("monica.im"));
        Map<String, String> stringStringMap = ScanWebUtils.analysisData(Arrays.asList(Config.HOST_LIST));
        traceWatch.stop();
        System.out.println(Config.HOST_PREFIX);
        stringStringMap.forEach((key, value) -> System.out.println(value+" "+key ));
        System.out.println(Config.HOST_SUFFIX);
        System.out.println("耗时："+JSON.toJSONString(traceWatch.getTaskMap().get("host文件时间").get(0).getData()));
    }
}
