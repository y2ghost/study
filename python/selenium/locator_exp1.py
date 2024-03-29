from time import sleep
from selenium import webdriver
from selenium.webdriver.firefox.service import Service
from selenium.webdriver.common.by import By

serviceObject = Service(port=4444)
driver = webdriver.Firefox()
driver.get("https://www.test.com/customer/account/login")
driver.find_element(By.CSS_SELECTOR, "#email").send_keys("admin12@gmail.com")
driver.find_element(By.ID, "pass").send_keys("admin12!@")
driver.find_element(By.NAME, "send").click()
# driver.find_element(By.CSS_SELECTOR, "span:contains('Men')").click()
# driver.find_element(By.CSS_SELECTOR, "input[type='email']").send_keys("adminadmin@gmail.com")
# driver.find_element(By.CSS_SELECTOR, "button[title='Subscribe'][type='submit']").click()
driver.find_element(By.XPATH, "//span[text()='Men']").click()
driver.find_element(By.XPATH, "//input[@type='email']").send_keys("adminadmin@gmail.com")
driver.find_element(By.XPATH, "//button[@title='Subscribe' and @type='submit']").click()
sleep(5)
driver.close()
driver.quit()
