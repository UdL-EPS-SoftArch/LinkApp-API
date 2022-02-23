Feature: Modify User
  In order to keep updated the information about a user
  As a user
  I want to modify my account settings

  Scenario: Modify email
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    When I modify the email of the user "user" by "userModified@sample.app"
    Then The response code is 200
    And The email of the user "user" has been modified by "userModified@sample.app"

  Scenario: Modify password
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    When I modify the password of the user "user" by "modifiedPassword"
    Then The response code is 200
    And I can login with username "user" and password "modifiedPassword"

  Scenario: Modify email when not logged in
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    When I modify the email of the user "user" by "userModified@sample.app"
    Then The response code is 401

  Scenario: Modify password when not logged in
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    When I modify the password of the user "user" by "modifiedPassword"
    Then The response code is 401

  Scenario: Modify email of non-existing user
    Given There is no registered user with username "nonExistingUser"
    When I modify the email of the user "nonExistingUser" by "nonExistingUserModified@sample.app"
    Then The response code is 401

    #modify account settings of non-existing user or not logged?
    #modify email with invalid syntax (without @)?