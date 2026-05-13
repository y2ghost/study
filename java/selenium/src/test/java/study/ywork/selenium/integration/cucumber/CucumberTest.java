package study.ywork.selenium.integration.cucumber;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "classpath:study/ywork/selenium", glue = { "study.ywork.selenium.integration.cucumber" })
public class CucumberTest extends AbstractTestNGCucumberTests {

}
