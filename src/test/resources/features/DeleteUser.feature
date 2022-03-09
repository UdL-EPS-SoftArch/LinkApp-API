Feature: Delete User
  In order to stop someone from using the app
  As a user
  I want to delete an account

  #Delete own account
  Scenario: Delete an own account
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I can login with username "user" and password "password"
    When I delete the user with username "user"
    Then The response code is 204
    And It has been deleted a user with username "user" and email "user@sample.app"

  #Delete own account and try to sign in again with the same credentials
  Scenario: Delete an own account and try to sign in with the same credentials
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I can login with username "user" and password "password"
    When I delete the user with username "user"
    Then The response code is 204
    And It has been deleted a user with username "user" and email "user@sample.app"
    And I cannot login with username "user" and password "password"

  #Delete own and try to sign up again with the same credentials
  Scenario: Delete an own account and try to register with the same credentials
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I can login with username "user" and password "password"
    When I delete the user with username "user"
    Then The response code is 204
    And I'm not logged in
    And I register a new user with username "user", email "user@sample.app" and password "password"
    And The response code is 201

  #Delete an existing user being logged as another user
  Scenario: Delete an existing user being logged as another user
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password" and email "user2@sample.app"
    And I login as "user" with password "password"
    When I delete the user with username "user2"
    Then The response code is 403

  #Delete an existing user beig admin of the application
  #Scenario: Delete an existing user
   # Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    #And the user is admin
    #And There is a registered user with username "user2" and password "password" and email "user2@sample.app"
    #And I login as "user" with password "password"
    #When I delete the user with username "user2"
    #Then The response code is 403

  #Delete an unexisting user being admin of the application
  Scenario: Delete a unexisting user
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And There is no registered user with username "usertodelete"
