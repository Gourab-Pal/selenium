package org.example.listeners;

import org.example.config.TestConfig;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final Map<String, Integer> retryMap = new ConcurrentHashMap<>();
    @Override
    public boolean retry(ITestResult result) {
        int maxRetry = TestConfig.getMaxRetryCount();
        if (retryCount < maxRetry) {
            retryCount++;
            String key = getKey(result);
            retryMap.put(key, retryCount);
            return true;
        }
        return false;
    }

    public static int getRetryCount(ITestResult result) {
        return retryMap.getOrDefault(getKey(result), 0);
    }

    private static String getKey(ITestResult result) {
        return result.getTestClass().getName() + "#" + result.getMethod().getMethodName();
    }
}