from appium import webdriver
from appium.options.ios import XCUITestOptions
from appium.webdriver.common.appiumby import AppiumBy

options = XCUITestOptions()
options.platformVersion = '13.4'
options.platform_version = "13"
options.app = "test_app"
options.device_name = "test_phone"
driver = webdriver.Remote("http://127.0.0.1:4723", options=options, direct_connection=True)
driver.find_element(AppiumBy.ACCESSIBILITY_ID, "Alert Views").click()
driver.find_element(AppiumBy.ACCESSIBILITY_ID, "Simple").click()

elm1 = driver.find_element(AppiumBy.XPATH,
    "//XCUIElementTypeStaticText[@name='A Short Title Is Best']")
print(elm1.get_attribute("type"))
print(elm1.get_attribute("index"))
print(elm1.get_attribute("label"))
print(elm1.is_enabled())
print(elm1.location)
print(elm1.text)

