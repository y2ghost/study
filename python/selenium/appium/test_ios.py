from appium import webdriver
from appium.options.ios import XCUITestOptions

options = XCUITestOptions()
options.platformVersion = '13.4'
options.platform_version = "13"
options.app = "test_app"
options.device_name = "test_phone"
driver = webdriver.Remote("http://127.0.0.1:4723", options=options, direct_connection=True)
print(driver.title)
driver.close()
driver.quit()
