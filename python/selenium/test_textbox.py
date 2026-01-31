from selenium import webdriver
from selenium.webdriver.common.by import By
import time

driver = webdriver.Firefox()
driver.get("http://localhost:8080/textbox.html")

book1Box = driver.find_element(By.NAME, "book1")
book1Box.send_keys("数学之美")

book2Box = driver.find_element(By.NAME, "book2")
book2Box.clear()
book2Box.send_keys("重来1、重来2、重来3")

print(f"单行文本框内容: {book1Box.get_property('value')}")
print(f"多行文本框内容: {book2Box.get_property('value')}")

time.sleep(3)
driver.quit()
