import requests
from selenium import webdriver
from selenium.webdriver.common.by import By

driver = webdriver.Firefox()
driver.get("https://www.cnblogs.com")

images = driver.find_elements(By.CSS_SELECTOR, "img")
for img in images:
    img_src = img.get_attribute("src")
    if img_src is None:
        continue

    response = requests.head(img_src)
    if 200 == response.status_code:
        print(f"发现图片:{img_src}")

driver.quit()
