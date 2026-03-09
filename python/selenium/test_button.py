from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.select import Select
import time

driver = webdriver.Firefox()
driver.get("http://localhost:8080/buttons.html")

defaultButton = driver.find_element(By.ID, "default_btn")
defaultButton.click()

if defaultButton.is_displayed():
    print("默认按钮已可见")
else:
    print("默认按钮不可见")

if defaultButton.is_enabled():
    print("默认按钮已可用")
else:
    print("默认按钮不可用")

defaultButton = driver.find_element(By.XPATH, "//button[text()='默认按钮']")
defaultButton.click()

defaultButton = driver.find_element(By.XPATH, "//button[contains(text(), '默认')]")
defaultButton.click()

defaultButton = driver.find_element(By.NAME, "dft_btn")
defaultButton.click()

submitButton = driver.find_element(By.XPATH, "//input[@value='提交表单']")
submitButton.click()

imageButton = driver.find_element(
    By.XPATH, "//input[contains(@src, 'img/yy-icon.png')]"
)
imageButton.click()

# 点击标签也可以选择关联的单选按钮
femaleRadio = driver.find_element(By.XPATH, "//label[@for='female']")
femaleRadio.click()

femaleRadio = driver.find_element(By.ID, "female")
femaleRadio.click()

if femaleRadio.get_attribute("type") == "radio":
    print("属于单选按钮")
else:
    print("不是单选按钮")

if femaleRadio.is_selected() == True:
    print("女性单选按钮被选中")
else:
    print("女性单选按钮没选中")

if femaleRadio.get_attribute("checked") == "true":
    print("女性单选按钮被选中")
else:
    print("女性单选按钮没选中")

checkButton = driver.find_element(By.NAME, "browser3")
checkButton.click()

checkButton = driver.find_element(By.ID, "edge")
checkButton.click()

checkButton = driver.find_element(By.ID, "chrome")
if checkButton.get_attribute("type") == "checkbox":
    print("属于复选框按钮")
else:
    print("不是复选框按钮")

checkButton = driver.find_element(By.ID, "firefox")
if checkButton.is_selected() == True:
    print("firefox复选框按钮被选中")
else:
    print("firefox复选框按钮没选中")

# 再次点击取消选择
checkButton.click()
if checkButton.get_attribute("checked") == "true":
    print("firefox复选框按钮被选中")
else:
    print("firefox复选框按钮没选中")

options = driver.find_elements(By.TAG_NAME, "option")
for option in options:
    print(f"选项值是: {option.get_attribute('value')}")

selectList = Select(driver.find_element(By.ID, "bld_grp"))
selectList.select_by_visible_text("O血型")
time.sleep(1)

selectList.select_by_value("B")
time.sleep(1)

selectList.select_by_index(3)
time.sleep(1)

# 多选框操作示例
fruits = Select(driver.find_element(By.ID, "fruits"))
fruits.select_by_index(0)
fruits.select_by_value("cranberry")
fruits.select_by_visible_text("接骨木莓")
fruits.select_by_index(6)

# 打印选择框第一个选择的选项
firstSelected = selectList.first_selected_option.get_attribute("value")
print(f"单选框第一个选中的选项: {firstSelected}")

# 打印多选框第一个选择的选项
firstSelected = fruits.first_selected_option.get_attribute("value")
print(f"多选框第一个选中的选项: {firstSelected}")

# 打印选择的选项并取消多选框的选择项
for option in selectList.all_selected_options:
    print(f"单选框选中的选项值: {option.get_attribute('value')}")

for option in fruits.all_selected_options:
    print(f"多选框选中的选项值: {option.get_attribute('value')}")

fruits.deselect_all()

time.sleep(3)
driver.quit()
