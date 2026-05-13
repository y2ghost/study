from appium import webdriver
from appium.options.ios import XCUITestOptions
from appium.webdriver.common.appiumby import AppiumBy

options = XCUITestOptions()
options.platformVersion = '13.4'
options.platform_version = "13"
options.app = "test_app"
options.device_name = "test_phone"
driver = webdriver.Remote("http://127.0.0.1:4723", options=options, direct_connection=True)
original_ele = driver.find_element(AppiumBy.ACCESSIBILITY_ID, "Text View")
destination_ele = driver.find_element(AppiumBy.ACCESSIBILITY_ID, "Alert Views")
driver.scroll(original_ele, destination_ele)
destination_ele.click()
