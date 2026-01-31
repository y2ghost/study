Feature: Login in practice site

  Scenario: Successful login
    Given I use "Chrome"
    When I navigate to "http://localhost:8080/login-form.html"
    And I log in with the username "user" and password "user"
    And I click Submit
    Then I should see the message "登陆成功"

  Scenario: Failure login
    Given I use "Chrome"
    When I navigate to "http://localhost:8080/login-form.html"
    And I log in with the username "bad-user" and password "bad-password"
    And I click Submit
    Then I should see the message "无效凭证"