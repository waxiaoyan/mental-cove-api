package com.mental.cove.utils;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.*;

public class VerificationCodeManager {
    private static ConcurrentHashMap<String, Pair<String, Long>> codeStorage = new ConcurrentHashMap<>();
    private static final long EXPIRE_AFTER = TimeUnit.MINUTES.toMillis(5); // 5分钟后过期

    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    static {
        scheduler.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            codeStorage.entrySet().removeIf(entry -> now - entry.getValue().getRight() > EXPIRE_AFTER);
        }, 5, 5, TimeUnit.MINUTES); // 每5分钟执行一次清理
    }

    public static void storeCode(String phoneNumber, String code) {
        codeStorage.put(phoneNumber, new ImmutablePair<>(code, System.currentTimeMillis()));
    }

    public static boolean verifyCode(String phoneNumber, String code) {
        Pair<String, Long> stored = codeStorage.get(phoneNumber);
        if (stored != null && stored.getLeft().equals(code) &&
                System.currentTimeMillis() - stored.getRight() < EXPIRE_AFTER) {
            return true;
        }
        return false;
    }

    public static void removeCode(String phoneNumber) {
        codeStorage.remove(phoneNumber);
    }
}