package com.replace.main.utils.watch;

/**
 * @ProjectName: Juc_project
 * @Package: com.example.main
 * @ClassName: TimeUtils
 * @Author: janck
 * @Description:
 * @Date: 2024/3/29 14:45
 * @Version: 1.0
 */
public class TimeUtils {
    /**
     * @return 当前毫秒数
     */
    public static long nowMs() {
        return System.currentTimeMillis();
    }

    /**
     * 当前毫秒与起始毫秒差
     * @param startMillis 开始纳秒数
     * @return 时间差
     */
    public static long diffMs(long startMillis) {
        return diffMs(startMillis, nowMs());
    }

    // 当前毫秒与起始毫秒差
    public static long diffMs(long startMillis, long endMillis) {
        if (startMillis < 0) {
            throw new IllegalArgumentException(String.format("Start time (%d) must not be less than zero!", startMillis));
        }
        if (endMillis < startMillis) {
            throw new IllegalArgumentException(String.format("End time (%d) must not be less than start time (%d)!", endMillis, startMillis));
        }
        return endMillis - startMillis;
    }
}
