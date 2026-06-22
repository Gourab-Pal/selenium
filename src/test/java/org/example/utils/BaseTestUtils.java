package org.example.utils;

import org.testng.ITestResult;

public class BaseTestUtils {

    public static String status;

    public static String getTestCaseStatus(ITestResult result) {
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                status = "PASSED";
                break;
            case ITestResult.FAILURE:
                status = "FAILED";
                break;
            case ITestResult.SKIP:
                status = "SKIPPED";
                break;
            default:
                status = "PARTIAL";
        }
        return status;
    }

}
