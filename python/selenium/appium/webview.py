from appium import webdriver
from appium.options.android import UiAutomator2Options
from appium.webdriver.common.appiumby import AppiumBy
from selenium.common import ElementNotVisibleException, NoSuchElementException
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions as ec

options = UiAutomator2Options()
options.platform_version = "13"
options.app = "haha.apk"
options.device_name = "test_phone"
driver = webdriver.Remote("http://127.0.0.1:4723", options=options, direct_connection=True)

explicitWait = WebDriverWait(driver, 25, poll_frequency=1,
    ignored_exceptions=[ElementNotVisibleException, NoSuchElementException])

acceptButton1 = explicitWait.until(ec.presence_of_element_located((AppiumBy.ID,
    "android:id/button1")))
acceptButton1.click()

acceptButton2 = explicitWait.until(ec.presence_of_element_located((AppiumBy.ID,
    "android:id/button1")))
acceptButton2.click()
print(driver.contexts)

driver.switch_to.context("WEBVIEW_info.android1.webview")
