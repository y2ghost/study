from selenium import webdriver
from selenium.webdriver.common.by import By
import time

driver = webdriver.Firefox()
driver.get("https://www.example.com/login")
driver.maximize_window()
driver.refresh()
driver.set_page_load_timeout(30)
print(driver.get_window_size())
print(driver.get_window_position())
print(driver.title)
# 等待页面加载完毕
time.sleep(5)
driver.find_element(By.NAME, "user-name").send_keys("username")
driver.find_element(By.ID, "password").send_keys("password")
driver.find_element(By.ID, "login-button").click()
print(driver.title)
driver.close()
driver.quit()
