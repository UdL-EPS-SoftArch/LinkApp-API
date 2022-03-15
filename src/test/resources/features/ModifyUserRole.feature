Feature: Modify User Role
  In order to manage permissions
  As a user
  I want to modify permissions of some users

  Scenario: Modify authorized to subscribed
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" belongs to that group as "ADMIN"
    And The user "user2" belongs to that group as "AUTHORIZED"
    When I update the user "user2" role of the group to "SUBSCRIBED"
    Then The response code is 200

  Scenario: Modify subscribed to admin
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" belongs to that group as "ADMIN"
    And The user "user2" belongs to that group as "SUBSCRIBED"
    When I update the user "user2" role of the group to "ADMIN"
    Then The response code is 200
