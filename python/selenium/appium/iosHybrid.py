from time import sleep
from appium import webdriver
from appium.options.ios import XCUITestOptions
from appium.webdriver.common.appiumby import AppiumBy
from selenium.common import ElementNotVisibleException, NoSuchElementException
from selenium.webdriver.support import expected_conditions as ec
from selenium.webdriver.support.wait import WebDriverWait

options = XCUITestOptions()
options.platformVersion = '13.4'
options.platform_version = "13"
options.app = "test_app"
options.device_name = "test_phone"
driver = webdriver.Remote("http://127.0.0.1:4723", options=options, direct_connection=True)
explicitWait = WebDriverWait(driver, 25, poll_frequency=1,
                             ignored_exceptions=[ElementNotVisibleException, NoSuchElementException])
driver.get("https://www.apple.com")
print(driver.contexts)

learnMoreLink = explicitWait.until(
    ec.presence_of_element_located((AppiumBy.XPATH, "//a[@data-analytics-title='Learn more about iPhone 14']")))
learnMoreLink.click()

sleep(10)
driver.quit()
