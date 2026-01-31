from selenium import webdriver
from selenium.webdriver.common.by import By
import time

driver = webdriver.Firefox()
driver.get("http://localhost:8080/drag_and_drop.html")

dragElement = driver.find_element(By.ID, "draggable")
dropElement = driver.find_element(By.ID, "target")
webdriver.ActionChains(driver).drag_and_drop(dragElement, dropElement).perform()

time.sleep(3)
driver.quit()
