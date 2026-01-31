import requests
from selenium import webdriver
from selenium.webdriver.common.by import By


def get_link_url(link):
    return link.get_attribute("href")


driver = webdriver.Firefox()
driver.get("http://localhost:8080/links.html")

link1 = driver.find_element(By.ID, "baidu")
print(f"发现链接1:{get_link_url(link1)}")

link2 = driver.find_element(By.ID, "bilibili")
print(f"发现链接2:{get_link_url(link2)}")

link3 = driver.find_element(By.PARTIAL_LINK_TEXT, "博客")
print(f"发现链接3:{get_link_url(link3)}")

link4 = driver.find_element(By.XPATH, "//a[@id='baidu']")
print(f"发现链接4:{get_link_url(link4)}")

link5 = driver.find_element(By.XPATH, "//div/a[3]")
print(f"发现链接5:{get_link_url(link5)}")

links = driver.find_elements(By.CSS_SELECTOR, "a")
for link in links:
    link_href = get_link_url(link)
    if link_href is None:
        continue

    response = requests.head(link_href)
    if 200 == response.status_code:
        print(f"发现链接: {link.text}({link_href})")

driver.quit()
