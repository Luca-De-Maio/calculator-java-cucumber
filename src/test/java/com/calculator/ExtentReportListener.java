package com.calculator;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

public class ExtentReportListener implements ConcurrentEventListener {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> featureTest = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> scenarioTest = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> stepTest = new ThreadLocal<>();

    private static ExtentReports getExtent() {
        if (extent == null) {
            extent = new ExtentReports();
            ExtentSparkReporter spark = new ExtentSparkReporter("target/cucumber-reports/extent-report.html");
            spark.config().setDocumentTitle("Automation Test Report");
            spark.config().setReportName("Calculator Test Report");
            spark.config().setTheme(Theme.STANDARD);
            extent.attachReporter(spark);
        }
        return extent;
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, this::handleTestRunStarted);
        publisher.registerHandlerFor(TestSourceRead.class, this::handleTestSourceRead);
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        publisher.registerHandlerFor(TestStepStarted.class, this::handleTestStepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
        publisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinished);
    }

    private void handleTestRunStarted(TestRunStarted event) {
        getExtent();
    }

    private void handleTestSourceRead(TestSourceRead event) {
        // No implementation needed
    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        ExtentTest feature = extent.createTest(event.getTestCase().getUri().toString());
        featureTest.set(feature);
        ExtentTest scenario = feature.createNode(event.getTestCase().getName());
        scenarioTest.set(scenario);
    }

    private void handleTestStepStarted(TestStepStarted event) {
        ExtentTest step = scenarioTest.get().createNode(event.getTestStep().toString());
        stepTest.set(step);
    }

    private void handleTestStepFinished(TestStepFinished event) {
        Status status = event.getResult().getStatus();
        switch (status) {
            case PASSED:
                stepTest.get().pass("Step passed");
                break;
            case FAILED:
                stepTest.get().fail("Step failed: " + event.getResult().getError());
                break;
            case SKIPPED:
                stepTest.get().skip("Step skipped");
                break;
            default:
                stepTest.get().info("Step: " + event.getTestStep().toString());
                break;
        }
        stepTest.remove();
    }

    private void handleTestCaseFinished(TestCaseFinished event) {
        scenarioTest.remove();
        featureTest.remove();
        extent.flush();
    }

    private void handleTestRunFinished(TestRunFinished event) {
        extent.flush();
    }
}
