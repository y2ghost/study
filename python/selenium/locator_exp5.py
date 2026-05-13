from time import sleep
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By

serviceObject = Service(port=4444)
driver = webdriver.Firefox()
driver.get("https://www.test.com/")
driver.find_element(By.XPATH, "//*[text()='Elements']").click()
driver.find_element(By.XPATH, "//span[text()='Check Box']").click()
driver.find_element(By.XPATH, "//button[@title='Toggle']//*[name()='svg']").click()
# 方法一
driver.find_element(By.XPATH, "//*[text()='Desktop']/preceding-sibling::span[2]").click()
driver.find_element(By.XPATH, "//*[text()='Downloads']/preceding-sibling::span[2]").click()
# 方法二
checkboxes = driver.find_elements(By.XPATH, "//span[@class='rct-checkbox']")
for checkbox in checkboxes:
    if checkbox.get_attribute('id') == 'Desktop' and checkbox.get_attribute('id') == 'Downloads':
        if not checkbox.is_selected():
            checkbox.click()

driver.find_element(By.XPATH, "//span[text()='Radio Button']").click()

sleep(10)
driver.quit()
