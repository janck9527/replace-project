package com.replace.main.utils;

import com.alibaba.fastjson2.JSON;
import com.sun.istack.internal.NotNull;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @ProjectName: Juc_project
 * @Package: com.example.utils
 * @ClassName: CompletableFutureTaskUtils
 * @Author: janck
 * @Description:
 * @Date: 2024/4/7 02:00
 * @Version: 1.0
 */
public class CompletableFutureTaskUtils extends CompletableFutureUtils {

    public ExecutorService executorService;

    public Map<String, CompletableFuture<?>> futureMap;

    public CompletableFutureTaskUtils() {
        //初始化futureMap
        generateFutureMap();
    }

    public CompletableFutureTaskUtils(ExecutorService executorService) {
        //赋值线程池
        if (ObjectUtils.isNotNull(executorService)) {
            this.executorService = executorService;
        }
        //初始化futureMap
        generateFutureMap();
    }

    private void generateFutureMap() {
        this.futureMap = new ConcurrentHashMap<>();
    }

    @Override
    public <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        CompletableFuture<U> future = this.getCompletableFutureSupplyAsync(supplier);
        futureMap.put(UUID.randomUUID().toString(), future);
        return future;
    }

    @Override
    public <U> CompletableFuture<U> supplyAsync(String key, Supplier<U> supplier) {
        CompletableFuture<U> future = this.getCompletableFutureSupplyAsync(supplier);
        futureMap.put(Optional.ofNullable(key).filter(StringUtils::isNotEmpty).orElse(UUID.randomUUID().toString()), future);
        return future;
    }

    @Override
    public <F, U> CompletableFuture<F> thenApplyAsync(String futureKey, Function<? super U, ? extends F> function) {
        CompletableFuture<F> future = this.getCompletableFutureThenApplyAsync((CompletableFuture<U>) this.futureMap.get(futureKey), function);
        futureMap.put(UUID.randomUUID().toString(), future);
        return future;
    }

    @Override
    public void runAsync(Runnable runnable) {
        if (ObjectUtils.isNotNull(this.executorService)) {
            CompletableFuture.runAsync(runnable, this.executorService);
        } else {
            CompletableFuture.runAsync(runnable);
        }
    }

    @Override
    public <F, U> CompletableFuture<F> thenApplyAsync(String key, String futureKey, Function<? super U, ? extends F> function) {
        CompletableFuture<F> future = this.getCompletableFutureThenApplyAsync((CompletableFuture<U>) this.futureMap.get(futureKey), function);
        futureMap.put(Optional.ofNullable(key).filter(StringUtils::isNotEmpty).orElse(UUID.randomUUID().toString()), future);
        return future;
    }

    @Override
    public <F, U> CompletableFuture<F> thenCombineAsync(String futureKey1,
                                                        String futureKey2,
                                                        BiFunction<? super F, ? super U, ?> biFunction) {
        CompletableFuture<F> future = this.getCompletableFutureThenCombineAsync((CompletableFuture<F>) this.futureMap.get(futureKey1), (CompletableFuture<U>) this.futureMap.get(futureKey2), biFunction);
        futureMap.put(UUID.randomUUID().toString(), future);
        return future;
    }

    @Override
    public <F, U> CompletableFuture<F> thenCombineAsync(String key,
                                                        String futureKey1,
                                                        String futureKey2,
                                                        BiFunction<? super F, ? super U, ?> biFunction) {
        CompletableFuture<F> future = this.getCompletableFutureThenCombineAsync((CompletableFuture<F>) this.futureMap.get(futureKey1), (CompletableFuture<U>) this.futureMap.get(futureKey2), biFunction);
        futureMap.put(Optional.ofNullable(key).filter(StringUtils::isNotEmpty).orElse(UUID.randomUUID().toString()), future);
        return future;
    }

    @NotNull
    private <U> CompletableFuture<U> getCompletableFutureSupplyAsync(Supplier<U> supplier) {
        CompletableFuture<U> future;
        if (ObjectUtils.isNotNull(this.executorService)) {
            future = CompletableFuture.supplyAsync(supplier, this.executorService);
        } else {
            future = CompletableFuture.supplyAsync(supplier);
        }
        return future;
    }

    @NotNull
    private <F, U> CompletableFuture<F> getCompletableFutureThenApplyAsync(CompletableFuture<U> from, Function<? super U, ? extends F> function) {
        CompletableFuture<F> future;
        if (ObjectUtils.isNotNull(this.executorService)) {
            future = from.thenApplyAsync(function, this.executorService);
        } else {
            future = from.thenApplyAsync(function);
        }
        return future;
    }

    @NotNull
    private <F, U> CompletableFuture<F> getCompletableFutureThenCombineAsync(CompletableFuture<F> future1,
                                                                             CompletableFuture<U> future2,
                                                                             BiFunction<? super F, ? super U, ?> biFunction) {
        CompletableFuture<F> future;
        if (ObjectUtils.isNotNull(this.executorService)) {
            future = future1.thenCombineAsync(future2, (t, u) -> (F) biFunction.apply(t, u), this.executorService);
        } else {
            future = future1.thenCombineAsync(future2, (t, u) -> (F) biFunction.apply(t, u));
        }
        return future;
    }

    @SneakyThrows
    @Override
    public void allOf() {
        CompletableFuture.allOf(this.futureMap.values().toArray(new CompletableFuture[0])).join();
    }

    @SneakyThrows
    @Override
    public void anyOf() {
        CompletableFuture.anyOf(this.futureMap.values().toArray(new CompletableFuture[0])).join();
    }

    @SneakyThrows
    @Override
    public Object getMapValueByKey(String key) {
        return this.futureMap.get(key).get();
    }

    @Override
    public String getMapValues() {
        if (ObjectUtils.isNull(this.futureMap)) {
            return "";
        }
        Map<String, String> resultMap = this.futureMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, v -> {
            try {
                return v.getValue().get() + "";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }));

        return JSON.toJSONString(resultMap);
    }


    @Override
    public void close() {
        this.executorService = null;
        this.futureMap = null;
    }
}
//    CompletableFutureUtil completableFutureUtil = CompletableFutureUtil.getInstance(AsyncConfiguration.myExecutorService);
//    CompletableFuture<List<Long>> idListCompletableFuture = completableFutureUtil.supplyAsync(() -> page.getRecords().stream().map(Entity::getId).collect(Collectors.toList()));
//    CompletableFuture<List<SaleOutboundItem>> itemCompletableFuture = completableFutureUtil.thenApplyAsync(idListCompletableFuture, saleOutboundItemService::getDataByOutboundIds);
//    CompletableFuture<List<Long>> itemIdsCompletableFuture = completableFutureUtil.thenApplyAsync(itemCompletableFuture, list -> Optional.ofNullable(list).filter(CollUtil::isNotEmpty).map(m -> m.stream().map(SaleOutboundItem::getId).collect(Collectors.toList())).orElse(null));
//    completableFutureUtil.thenApplyAsync(itemCompletableFuture, items -> Optional.ofNullable(items).filter(CollUtil::isNotEmpty).map(m -> m.stream().collect(Collectors.groupingBy(SaleOutboundItem::getOutboundId))).orElse(MapUtil.newHashMap()));
//        completableFutureUtil.thenApplyAsync("itemBoltMap", itemIdsCompletableFuture, itemIds -> Optional.ofNullable(itemIds).filter(CollUtil::isNotEmpty).map(ids -> Optional.ofNullable(saleOutboundItemBoltService.getDataByOutboundItemIds(ids)).filter(CollUtil::isNotEmpty).map(bolts -> bolts.stream().collect(Collectors.groupingBy(SaleOutboundItemBolt::getOutboundItemId))).orElse(MapUtil.newHashMap())).orElse(MapUtil.newHashMap()));
//        completableFutureUtil.thenApplyAsync("itemWarehouseLocationMap", itemIdsCompletableFuture, itemIds -> Optional.ofNullable(itemIds).filter(CollUtil::isNotEmpty).map(ids -> Optional.ofNullable(saleOutboundItemWarehouseLocationService.getDataByOutboundItemIds(ids)).filter(CollUtil::isNotEmpty).map(bolts -> bolts.stream().collect(Collectors.groupingBy(SaleOutboundItemWarehouseLocation::getOutboundItemId))).orElse(MapUtil.newHashMap())).orElse(MapUtil.newHashMap()));
//        completableFutureUtil.allOf();
//        Map<Long, List<SaleOutboundItem>> itemMap = (Map<Long, List<SaleOutboundItem>>) completableFutureUtil.getMapValueByKey("itemMap");
//        Map<Long, List<SaleOutboundItemBolt>> itemBoltMap = (Map<Long, List<SaleOutboundItemBolt>>) completableFutureUtil.getMapValueByKey("itemBoltMap");
//        Map<Long, List<SaleOutboundItemWarehouseLocation>> itemWarehouseLocationMap = (Map<Long, List<SaleOutboundItemWarehouseLocation>>) completableFutureUtil.getMapValueByKey("itemWarehouseLocationMap");
//        completableFutureUtil.shutdown();

