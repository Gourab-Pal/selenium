package org.example.listeners;

import io.qameta.allure.Allure;
import io.qameta.allure.testng.AllureTestNg;
import org.example.config.TestConfig;
import org.example.utils.BrowserVersionUtils;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class BrowserAwareAllureListeners implements ITestListener {
    @Override
    public void onTestStart(ITestResult testResult) {
        String browser = TestConfig.getBrowser();
        String version = BrowserVersionUtils.getBrowserVersion();
        Allure.getLifecycle().updateTestCase(tc -> {
            if (tc.getHistoryId() != null) {
                tc.setHistoryId(tc.getHistoryId() + "_" + browser);
            }
            tc.setName(tc.getName() + " [" + browser + " " + version + "]");
        });
    }
}
