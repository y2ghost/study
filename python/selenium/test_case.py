import unittest
from selenium import webdriver


class testBase(unittest.TestCase):
    # 演示基本测试方法
    def test1(self):
        lang = ["python", "ruby"]
        s1 = "python"
        s2 = "ruby"
        color1 = None
        color2 = "blue"
        self.assertTrue(s1 != s2)
        self.assertFalse(s1 == s2)
        self.assertIs(s1, "python")
        self.assertIsNot(s2, "python")
        self.assertIsNone(color1)
        self.assertIsNotNone(color2)
        self.assertIsInstance(s1, str)
        self.assertNotIsInstance(s1, int)
        self.assertEqual(s1, "python")
        self.assertNotEqual(s1, s2)
        self.assertIn(s1, lang)
        self.assertNotIn(color2, lang)

    def test2(self):
        driver = webdriver.Firefox()
        driver.get("https://www.baidu.com")
        title = driver.title
        driver.quit()
        self.assertEqual(title, "百度一下，你就知道")


if __name__ == "__main__":
    unittest.main()
