from time import sleep
from appium import webdriver
from appium.options.android import UiAutomator2Options
from appium.webdriver.common.touch_action import TouchAction
from appium.webdriver.common.appiumby import AppiumBy

options = UiAutomator2Options()
options.platform_version = "13"
options.app = "haha.apk"
options.device_name = "test_phone"
driver = webdriver.Remote("http://127.0.0.1:4723", options=options, direct_connection=True)
sleep(10)
actions = TouchAction(driver)
element = driver.find_element(AppiumBy.ID, 'io.selendroid.testapp:id/input_adds_check_box')
# # actions.tap(x=800, y=900, count=1).perform()
# actions.tap(element=element, count=1).perform()
longPressElement = driver.find_element(AppiumBy.XPATH, "//android.widget.Button[@content-desc='buttonTestCD']")
# actions.long_press(longPressElement, duration=50000000)
# actions.long_press(longPressElement, duration=5).perform()
scrollToElementView = driver.find_element(AppiumBy.ANDROID_UIAUTOMATOR, "")
scrollToElementView.click()
