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
    And I'm not logged in
    When I modify the email of the user "user" by "userModified@sample.app"
    Then The response code is 401

  Scenario: Modify password when not logged in
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I'm not logged in
    When I modify the password of the user "user" by "modifiedPassword"
    Then The response code is 401

  Scenario: Modify email of a user different from yours
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I can login with username "user" and password "password"
    When I modify the email of the user "user2" by "user2Modified@sample.app"
    Then The response code is 403

  Scenario: Modify password of a user different from yours
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I can login with username "user" and password "password"
    When I modify the password of the user "user2" by "user2ModifiedPassword"
    Then The response code is 403

  #Scenario: Modify email of non-existing user
   # Given There is no registered user with username "nonExistingUser"
   # And There is a registered user with username "user" and password "password" and email "user@sample.app"
   # And I can login with username "user" and password "password"
   # When I modify the email of the user "nonExistingUser" by "nonExistingUserModified@sample.app"
   # Then The response code is 401

  #Scenario: Modify password of non-existing user
   # Given There is no registered user with username "nonExistingUser"
   # And There is a registered user with username "user" and password "password" and email "user@sample.app"
   # And I can login with username "user" and password "password"
   # When I modify the password of the user "nonExistingUser" by "nonExistingUserPassword"
   # Then The response code is 401

    #modify account settings of non-existing user or not logged?
    #modify email with invalid syntax (without @)?