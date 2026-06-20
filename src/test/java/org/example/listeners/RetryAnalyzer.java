package org.example.listeners;

import org.example.config.TestConfig;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    @Override
    public boolean retry(ITestResult result) {
        int maxRetry = TestConfig.getMaxRetryCount();
        if (retryCount < maxRetry) {
            retryCount++;
            return true;
        }
        return false;
    }
}