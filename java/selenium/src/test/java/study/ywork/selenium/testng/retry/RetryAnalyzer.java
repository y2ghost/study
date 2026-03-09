package study.ywork.selenium.testng.retry;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    static final int MAX_RETRIES = 5;
    int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount <= MAX_RETRIES) {
            retryCount++;
            return true;
        }

        return false;
    }
}
