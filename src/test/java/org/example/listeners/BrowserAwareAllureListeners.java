package org.example.listeners;

import io.qameta.allure.Allure;
import io.qameta.allure.testng.AllureTestNg;
import org.example.config.TestConfig;
import org.example.utils.BrowserVersionUtils;
import org.testng.ITestResult;


public class BrowserAwareAllureListeners extends AllureTestNg {
    @Override
    public void onTestStart(ITestResult testResult) {
        super.onTestStart(testResult);
        // At this point the TEST uuid is current in the thread context (no fixture pushed yet).
        // updateTestCase() correctly targets the test result here.
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
