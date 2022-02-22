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

    #modify account settings of non-existing user or not logged?