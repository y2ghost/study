from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By

serviceObject = Service(port=4444)
driver = webdriver.Firefox()
driver.get("http://www.test.com/index.php")
driver.find_element(By.XPATH, "//*[@id='contact-link']//preceding-sibling::div/a").click()
driver.find_element(By.XPATH, "//*[@for='email']//following-sibling::input").send_keys("admin12@gmail.com")
driver.find_element(By.XPATH, "//label[text()='Password']//parent::div/span/input").send_keys("admin12!@")
driver.find_element(By.XPATH, "//p[@class='submit']//child::button").click()
