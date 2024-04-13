package com.replace.main.thread;

import com.alibaba.fastjson2.JSON;
import com.replace.main.thread.task.WorkerTask;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义线程池饱和时处理策略
 */
public class CustomerRejectedExecutionHandler implements RejectedExecutionHandler {
    final Map<String, Runnable> tempMap = new ConcurrentHashMap<>();

    /**
     * 具体实现方法，一般上线后，会把多余的线程任务，保存到日志或写库，这里模拟尝试几种处理
     *
     * @param r        进入该处的任务一定是被丢弃的
     * @param executor
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        WorkerTask worker = (WorkerTask) r;

        // 模拟写日志
        System.err.println(worker.getId() + " 被丢弃! 此时消息为: " + JSON.toJSON(worker));
        tempMap.put(worker.getId(), r); // 被丢弃的任务加到tempMap中去
        // 重新放入队列里执行
        try {
            System.out.println("重新放入队列前的队列大小为: " + executor.getQueue().size());
            executor.getQueue().put(r);
            System.out.println("重新放入队列后的队列大小为: " + executor.getQueue().size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
