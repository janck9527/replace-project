package com.replace.main;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName: replace-project
 * @Package: com.replace.main
 * @ClassName: Answer
 * @Author: janck
 * @Description:
 * @Date: 2024/4/12 11:50
 * @Version: 1.0
 */
@Data
public class Answer {
    // 耗时时间
    private String timeConsume;
    // 详细参数
    private List<Record> records;

    // Getters and setters

    @lombok.Data
    public static class Record {
        private int ttl;
        private String value;
        private String ipLocation;
    }

    @lombok.Data
    public static class Data {
        private Map<String, List<Answer>> answerMap;
    }

    @lombok.Data
    public static class Root {
        private int code;
        private Data data;
    }
}
