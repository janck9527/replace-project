package com.replace.main.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @ProjectName: Juc_project
 * @Package: com.example.utils
 * @ClassName: CompletableFutureUtils
 * @Author: janck
 * @Description:
 * @Date: 2024/4/7 01:59
 * @Version: 1.0
 */
public abstract class CompletableFutureUtils {

    private static CompletableFutureUtils createInstance() {
        return new CompletableFutureTaskUtils();
    }

    /**
     * 获取实例
     * @return
     */
    public static CompletableFutureUtils getInstance() {
        return createInstance();
    }

    private static CompletableFutureUtils createInstance(ExecutorService executorService) {
        return new CompletableFutureTaskUtils(executorService);
    }

    public static CompletableFutureUtils getInstance(ExecutorService executorService) {
        return createInstance(executorService);
    }

    /**
     * 异步执行某个任务
     * @param supplier 执行任务的函数
     * @param <U>
     * @return 默认为CompletableFuture 会情况进行回调处理
     */
    public abstract <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier);

    /**
     * 异步执行某个任务
     * @param key 自定义key
     * @param supplier 执行任务的函数
     * @param <U>
     * @return 默认为CompletableFuture 会情况进行回调处理
     */
    public abstract <U> CompletableFuture<U> supplyAsync(String key, Supplier<U> supplier);

    /**
     * 表示任务执行完后再执行某个操作（任务的后置处理）
     * @param futureKey
     * @param function
     * @param <F>
     * @param <U>
     * @return 默认为CompletableFuture 会情况进行回调处理
     */
    public abstract <F, U> CompletableFuture<F> thenApplyAsync(String futureKey, Function<? super U, ? extends F> function);

    /**
     * 异步执行无返回值的方法
     * 方法不需要返回结果
     */
    public abstract void runAsync(Runnable runnable);

    /**
     * 表示任务执行完后再执行某个操作（任务的后置处理）
     * @param key  自定义
     * @param futureKey
     * @param function
     * @param <F>
     * @param <U>
     * @return 默认为CompletableFuture 会情况进行回调处理
     */
    public abstract <F, U> CompletableFuture<F> thenApplyAsync(String key, String futureKey, Function<? super U, ? extends F> function);

    /**
     * 在前两个异步任务完成之后在执行的任务
     * 可以获取前面两线程的返回结果，本身也有返回结果
     * @param futureKey1 前一个异步任务
     * @param futureKey2 需要等待的后一个异步任务
     * @param biFunction 执行逻辑
     * @param <F>
     * @param <U>
     * @return
     */
    public abstract  <F, U> CompletableFuture<F> thenCombineAsync(String futureKey1,
                                                                  String futureKey2,
                                                                  BiFunction<? super F, ? super U, ?> biFunction);

    /**
     * 在前两个异步任务完成之后在执行的任务
     * 可以获取前面两线程的返回结果，本身也有返回结果
     * @param key 自定义key
     * @param futureKey1 前一个异步任务
     * @param futureKey2 需要等待的后一个异步任务
     * @param biFunction 执行逻辑
     * @param <F>
     * @param <U>
     * @return
     */
    public abstract  <F, U> CompletableFuture<F> thenCombineAsync(String key,
                                                                  String futureKey1,
                                                                  String futureKey2,
                                                                  BiFunction<? super F, ? super U, ?> biFunction);

    /**
     * 等待一组CompletableFuture任务全部完成
     */
    public abstract void allOf();

    /**
     * 多个CompletableFuture中等待任意一个完成
     */
    public abstract void anyOf();

    /**
     * 根据定义的key返回对应的CompletableFuture
     * @param key 自定义key
     * @return CompletableFuture
     */
    public abstract Object getMapValueByKey(String key);

    /**
     * 查询所有的value并返回对应的值
     * @return 所有的返回值
     */
    public abstract String getMapValues();

    /**
     * 工具类使用完后，请直接调用清理
     */
    public abstract void close();
}
