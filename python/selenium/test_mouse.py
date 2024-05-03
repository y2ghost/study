from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait
import time

driver = webdriver.Firefox()
driver.get("https://www.baidu.com")
firstWindow = driver.current_window_handle
newsElement = driver.find_element(By.LINK_TEXT, "新闻")

print("鼠标左键点击")
newsElement.click()

print("鼠标左键保持")
webdriver.ActionChains(driver).click_and_hold(newsElement).perform()

print("鼠标左键双击")
webdriver.ActionChains(driver).double_click(newsElement).perform()

print("鼠标右键点击")
driver.switch_to.new_window()
driver.get("https://www.cnblogs.com/")
loginMenu = driver.find_element(By.LINK_TEXT, "注册")
webdriver.ActionChains(driver).context_click(loginMenu).perform()
time.sleep(3)

print(f"只保留第一个窗口")
allWindows = driver.window_handles
allWindows.remove(firstWindow)

for win in allWindows:
    driver.switch_to.window(win)
    driver.close()

driver.switch_to.window(firstWindow)
print("鼠标移动到<更多>菜单项")
loginMenu = driver.find_element(By.LINK_TEXT, "更多")
ActionChains(driver).move_to_element(loginMenu).perform()
WebDriverWait(driver, 3).until(EC.visibility_of_element_located((By.LINK_TEXT, "百科")))
baikeMenu = driver.find_element(By.LINK_TEXT, "百科")
baikeMenu.click()

print(f"可以移动鼠标到特点位置")
ActionChains(driver).move_by_offset(255, 255).click().perform()

time.sleep(3)
driver.quit()
