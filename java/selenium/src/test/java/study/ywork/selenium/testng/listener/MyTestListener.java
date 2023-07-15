package study.ywork.selenium.testng.listener;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;
import org.slf4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class MyTestListener implements ITestListener {
    static final Logger log = getLogger(lookup().lookupClass());

    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
        log.debug("onStart {}", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        ITestListener.super.onTestStart(result);
        log.debug("onTestStart {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ITestListener.super.onTestSuccess(result);
        log.debug("onTestSuccess {}", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ITestListener.super.onTestFailure(result);
        log.debug("onTestFailure {}", result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ITestListener.super.onTestSkipped(result);
        log.debug("onTestSkipped {}", result.getSkipCausedBy());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
        log.debug("onTestFailedButWithinSuccessPercentage {}", result.getThrowable());
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
        log.debug("onTestFailedWithTimeout {}", result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        ITestListener.super.onFinish(context);
        log.debug("onFinish {}", context.getName());
    }
}
