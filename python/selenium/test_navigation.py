from selenium import webdriver

driver = webdriver.Firefox()
driver.get("https://www.baidu.com")
driver.get("https://www.cnblogs.com")

driver.back()
print("回到第一个页面")

driver.forward()
print("回到第二个页面")

driver.refresh()
print("刷新当前页面")

driver.quit()
