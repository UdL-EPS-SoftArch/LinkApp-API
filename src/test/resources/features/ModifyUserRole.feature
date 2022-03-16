Feature: Modify User Role
  In order to manage permissions
  As a user
  I want to modify permissions of some users

  Scenario: Modify admin to authorized as admin
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" belongs to that group as "ADMIN"
    And The user "user2" belongs to that group as "ADMIN"
    When I update the user "user2" role of the group to "AUTHORIZED"
    Then The response code is 200
    Then The role of the user "user2" has been changed to "AUTHORIZED"

  Scenario: Modify admin to subscribed as admin
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" belongs to that group as "ADMIN"
    And The user "user2" belongs to that group as "ADMIN"
    When I update the user "user2" role of the group to "SUBSCRIBED"
    Then The response code is 200
    Then The role of the user "user2" has been changed to "SUBSCRIBED"

  Scenario: Modify authorized to subscribed as admin
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" belongs to that group as "ADMIN"
    And The user "user2" belongs to that group as "AUTHORIZED"
    When I update the user "user2" role of the group to "SUBSCRIBED"
    Then The response code is 200
    Then The role of the user "user2" has been changed to "SUBSCRIBED"

  Scenario: Modify subscribed to authorized as admin
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" belongs to that group as "ADMIN"
    And The user "user2" belongs to that group as "SUBSCRIBED"
    When I update the user "user2" role of the group to "AUTHORIZED"
    Then The response code is 200
    Then The role of the user "user2" has been changed to "AUTHORIZED"

  Scenario: Modify subscribed to admin as admin
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" belongs to that group as "ADMIN"
    And The user "user2" belongs to that group as "SUBSCRIBED"
    When I update the user "user2" role of the group to "ADMIN"
    Then The response code is 200
    Then The role of the user "user2" has been changed to "ADMIN"

  Scenario: Modify authorized to admin as admin
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" belongs to that group as "ADMIN"
    And The user "user2" belongs to that group as "AUTHORIZED"
    When I update the user "user2" role of the group to "ADMIN"
    Then The response code is 200
    Then The role of the user "user2" has been changed to "ADMIN"