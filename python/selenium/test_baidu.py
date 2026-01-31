from selenium import webdriver
from selenium.webdriver.common.by import By
import time

driver = webdriver.Firefox()
# driver = webdriver.Chrome()
# driver = webdriver.Edge()
driver.get("https://www.baidu.com")

# 操作窗口大小
driver.maximize_window()
driver.fullscreen_window()

# 输入搜索关键字
elem = driver.find_element(By.NAME, "wd")
elem.send_keys("selenium测试")
elem.submit()

print(driver.title)
time.sleep(3)

# 退出或是关闭浏览器
# driver.close()
driver.quit()
