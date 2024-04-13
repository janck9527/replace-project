package com.replace.main.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ProjectName: Juc_project
 * @Package: com.example.thread
 * @ClassName: CustomerThreadFactory
 * @Author: janck
 * @Description:
 * @Date: 2024/3/29 16:20
 * @Version: 1.0
 */
@Slf4j
public class CustomerThreadFactory implements ThreadFactory {
    private ThreadGroup group;
    private AtomicInteger threadNumber = new AtomicInteger(1);
    private String namePrefix;

    public CustomerThreadFactory() {

    }

    public CustomerThreadFactory(String name) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        if (null == name || name.isEmpty()) {
            name = "pool";
        }
        namePrefix = name + "-thread-";
    }

    @Override
    public Thread newThread(Runnable task) {
        // 获取当前任务的名称
        Thread thread = new Thread(group, task, namePrefix + threadNumber.getAndIncrement(), 0);
        thread.setUncaughtExceptionHandler(new CustomerUncaughtExceptionHandler());
        thread.setDaemon(true);
        return thread;
    }

    /**
     * 自定义统一处理线程池异常:日志处理
     */
    private static class CustomerUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.err.println("异常处理器捕获到了异常: " + "current thread id : " + t.getId() + "  | name : " + t.getName() + " | status : " + t.getState() + " |  error : " + e.getMessage() + " stack info : " + t.getStackTrace());
        }
    }
}
