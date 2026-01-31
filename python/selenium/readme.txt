开启HTTP服务
- python -m http.server 8080

异常类列表
- ConnectionClosedException
- ElementClickInterceptedException
- ElementNotInteractableException
- ElementNotSelectableException
- ElementNotVisibleException
- ErrorInResponseException
- ErrorHandler.UnknownServerException
- ImeActivationFailedException
- ImeNotAvailableException
- InsecureCertificateException
- InvalidArgumentException
- InvalidCookieDomainException
- InvalidCoordinateException
- InvalidElementStateException
- InvalidSelectorException
- InvalidSessionIdException
- InvalidSwitchToTargetException
- JavascriptException
- JsonException
- MoveTargetOutOfBoundsException
- NoAlertPresentException
- NoSuchAttributeException
- NoSuchCookieException
- NoSuchElementException
- NoSuchFrameException
- NoSuchWindowException
- NoSuchContextException
- ScreenshotException
- StaleElementReferenceException
- TimeoutException
- UnableToSetCookieException
- UnexpectedAlertPresentException
- UnexpectedTagNameException
- UnknownMethodException
- WebDriverException

ExpectedConditions模块说明
- 用于一系列的条件判断
- 导入模块方法
  > from selenium.webdriver.support import expected_conditions as EC
- 常见判断方法
  > alert_is_present
  > element_located_selection_state_to_be(ui_locator, is_selected)
  > element_located_to_be_selected(ui_locator)
  > element_selection_state_to_be(ui_element, is_selected)
  > element_to_be_clickable(ui_locator)
  > element_to_be_selected(ui_element)
  > frame_to_be_available_and_switch_to_it(ui_locator)
  > invisibility_of_element_located(ui_locator)
  > title_is(title)
  > staleness_of(ui_element)
  > text_to_be_present_in_element(ui_locator, inner_text)
  > text_to_be_present_in_element_value(ui_locator, value)
  > title_contains(title_text)
  > visibility_of(ui_element)
  > visibility_of_all_elements_located(ui_locator)
  > visibility_of_any_elements_located(ui_locator)
  > visibility_of_element_located(ui_locator)
  > invisibility_of_element_located(ui_locator)
  > new_window_is_opened(current_handles)
  > number_of_windows_to_be(num_windows)
  > url_changes(url)
  > url_contains(url)
  > url_matches(url)
  > url_to_be(url)
  > presence_of_all_elements_located(locator)
  > presence_of_element_located(locator)
