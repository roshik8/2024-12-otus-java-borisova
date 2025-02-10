package model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TestResult {
    private int totalTests;
    private int passedTests;
    private int failedTests;
    private final List<String> passedTestNames = new ArrayList<>();
    private final List<String> failedTestNames = new ArrayList<>();

    public void addPassedTest(String testName) {
        passedTests++;
        passedTestNames.add(testName);
        totalTests++;
    }

    public void addFailedTest(String testName) {
        failedTests++;
        failedTestNames.add(testName);
        totalTests++;
    }

    @Override
    public String toString() {
        return String.format("Всего тестов: %d\nПройдено тестов: %d\nПровалено тестов: %d\n\nПройденные тесты: %s\nПроваленные тесты: %s",
                totalTests, passedTests, failedTests, passedTestNames, failedTestNames);
    }
}
