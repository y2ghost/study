from selenium import webdriver

driver = webdriver.Firefox()
driver.get("http://localhost:8080/frames.html")

driver.switch_to.frame("new_frame")
driver.switch_to.parent_frame()

driver.quit()
