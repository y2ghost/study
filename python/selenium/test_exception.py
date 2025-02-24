from selenium import webdriver
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
from selenium.common.exceptions import NoSuchElementException
from selenium.common.exceptions import StaleElementReferenceException
from selenium.webdriver.common.by import By
import time

driver = webdriver.Firefox()

try:
    driver.get("http://localhost:8080/not_exists.html")
    WebDriverWait(driver, 5).until(EC.presence_of_element_located((By.ID, "query")))
except TimeoutException:
    print("花费太多时间加载页面，模拟超时！")

try:
    driver.get("http://localhost:8080/textbox.html")
    book1Box = driver.find_element(By.NAME, "book-not-exist")
    book1Box.send_keys("书籍找到了？")
except NoSuchElementException as exception:
    print("没有找到ID为book-not-exist的书籍")

time.sleep(3)
driver.quit()
