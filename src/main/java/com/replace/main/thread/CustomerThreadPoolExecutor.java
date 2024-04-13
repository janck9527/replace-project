package com.replace.main.thread;


import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.*;

/**
 * 设计模式: 单例模式
 * 自定义线程池: 实现业务需求:
 * 可以获取线程池内部细节,对排查程序故障有很大的好处
 */
@Slf4j
public final class CustomerThreadPoolExecutor extends ThreadPoolExecutor {

    private static final int corePoolSize = 20;
    private static final int maximumPoolSize = 150;
    // 单位秒
    // 空闲线程存活时间，当线程池中没有任务时，会销毁一些线程，销毁的线程数=maximumPoolSize（最大线程数）-corePoolSize（核心线程数）
    private static final long keepAliveTime = 300L;
    private static final TimeUnit unit = TimeUnit.SECONDS;  // 秒
    private static final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(20); // 队列容量
    private static final ThreadFactory threadFactory = new CustomerThreadFactory("threadFactory-");

    private static class CustomerThreadPoolExecutorHandler {
        //static int maximumPoolSize = Runtime.getRuntime().availableProcessors();
        private static CustomerThreadPoolExecutor newInstance = new CustomerThreadPoolExecutor(maximumPoolSize * 2, threadFactory);
    }

    public static CustomerThreadPoolExecutor newInstance() {
        return CustomerThreadPoolExecutorHandler.newInstance;
    }


    private CustomerThreadPoolExecutor(int maximumPoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory,new CustomerRejectedExecutionHandler());
    }

    @Override
    protected void beforeExecute(Thread task, Runnable r) {
        Runtime runtime = Runtime.getRuntime();
        int cupNum = runtime.availableProcessors();// 获取可以使用的CPU数量
        long freeMemory = runtime.freeMemory(); // 空闲内存 kb;
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        //log.info("current name: " + task.getName() + " | beforeExecute 获取可以使用的CPU数量: " + cupNum + " | 空闲内存: " + freeMemory + " | 最大内存: " + maxMemory + "  | 总内存: " + totalMemory);
    }

    @Override
    protected void afterExecute(Runnable task, Throwable t) {
        /*log.info("afterExecute Thread name : " + task.getClass().getSimpleName()+ " | current thread status :" + Thread.currentThread().getState()
                + " current thread id : " + Thread.currentThread().getId() + " | afterExecute poolSize : "
                + this.getPoolSize() + " | threadPool task num : "
                + this.getTaskCount() + " | finished task num: "
                + this.getCompletedTaskCount() + " | not finished task num: " + (this.getTaskCount() - this.getCompletedTaskCount()));*/
    }


}
