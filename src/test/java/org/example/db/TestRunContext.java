package org.example.db;

public class TestRunContext {

    private static String testRunId;

    public static void setTestRunId(String id) {
        testRunId = id;
    }

    public static String getTestRunId() {
        return testRunId;
    }
}