from selenium import webdriver

driver = webdriver.Firefox()
driver.get("https://www.baidu.com")

print("设置浏览器的窗口位置")
driver.set_window_position(x=500, y=400)
driver.set_window_rect(x=30, y=30, width=450, height=500)

window_pos = driver.get_window_position()
print(f"窗口位置: {window_pos}")

window_size = driver.get_window_size()
print(f"窗口大小: {window_size}")

driver.quit()
