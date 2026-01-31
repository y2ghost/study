from selenium import webdriver
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.common.exceptions import TimeoutException
from selenium.common.exceptions import ElementNotVisibleException
from selenium.common.exceptions import ElementNotSelectableException
import time

timeout = 10

driver = webdriver.Firefox()
driver.implicitly_wait(timeout)

driver.get("https://www.baidu.com")
print("这是一种隐性等待")

try:
    print("这是一种显式等待")
    wait = WebDriverWait(
        driver,
        timeout,
        poll_frequency=1,
        ignored_exceptions=[ElementNotVisibleException, ElementNotSelectableException],
    )
    searchElement = wait.until(EC.presence_of_element_located((By.NAME, "wd")))
except TimeoutException:
    print("演示显式等待，没找到搜索文本框！")

# 输入搜索关键字
searchElement = driver.find_element(By.NAME, "wd")
searchElement.send_keys("selenium测试")
searchElement.submit()

time.sleep(3)
driver.quit()
