from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.action_chains import ActionChains

driver=webdriver.Chrome()
driver.get('https://www.baidu.com')

ActionChains(driver)\
        .key_down(Keys.CONTROL)\
        .send_keys("a")\
        .key_up(Keys.CONTROL)\
        .key_down(Keys.CONTROL)\
        .send_keys("c")\
        .key_up(Keys.CONTROL)\
        .perform()

driver.quit()
