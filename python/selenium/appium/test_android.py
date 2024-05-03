import time
from appium import webdriver
from appium.options.android import UiAutomator2Options

options = UiAutomator2Options()
options.platform_version = "13"
options.app = "haha.apk"
options.device_name = "test_phone"
driver = webdriver.Remote("http://127.0.0.1:4723", options=options, direct_connection=True)
print(driver.title)
driver.close()
driver.quit()
