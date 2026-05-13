from time import sleep
from appium import webdriver
from appium.options.android import UiAutomator2Options
from appium.webdriver.common.appiumby import AppiumBy

options = UiAutomator2Options()
options.platform_version = "13"
options.app = "haha.apk"
options.device_name = "test_phone"
driver = webdriver.Remote("http://127.0.0.1:4723", options=options, direct_connection=True)
sleep(10)

# elm1 = driver.find_element(AppiumBy.ANDROID_UIAUTOMATOR, "UiSelector().index(9)")
# elm1.click()
# btn = driver.find_element(AppiumBy.ACCESSIBILITY_ID, "btnId")
# btn.click()
# btn = driver.find_element(AppiumBy.XPATH, "//imageButton[@content-desc='btnDesc']")
# button.click()
# buttonList = driver.find_elements(AppiumBy.CLASS_NAME, "android.widget.Button")
# print(len(buttonList))

# print(driver.current_activity)
# print(driver.current_package)
# print(driver.current_context)
# print(driver.orientation)
# print(driver.location)
# print(driver.is_locked())

driver.find_element(AppiumBy.ACCESSIBILITY_ID, "startUserRegistrationCD").click()
usernameElement = driver.find_element(AppiumBy.ID, "io.selendroid.testapp:id/inputUsername")
print(usernameElement.get_attribute("focusable"))
print(usernameElement.get_attribute("class"))
print(usernameElement.is_enabled())
print(usernameElement.is_selected())
print(usernameElement.is_displayed())

datePicker = driver.find_element(AppiumBy.ACCESSIBILITY_ID, "Date Picker")
datePicker.click()

for _ in range(10):
    try:
        value = driver.find_element(AppiumBy.ID, "elementID").is_displayed()
        if value is True:
            break
    except:
        driver.swipe(340, 1500, 3400, 1000, 5000)
        continue
driver.find_element(AppiumBy.ID, "elementID").click()
driver.close()
driver.quit()

