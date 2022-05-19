Feature: Register UserRole
  In order to interact with a group
  As a user
  I want to register new roles

  Scenario: Subscribe to a new group
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And A group exists
    When The user "user" joins the group as "SUBSCRIBED"
    Then The response code is 201
    And The user "user" belongs to that group as "SUBSCRIBED"

  Scenario: Subscribe to a group where user is already subscribed
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "ADMIN"
    And The user "user" belongs to that group as "ADMIN"
    When The user "user" joins the group as "ADMIN"
    Then The response code is 403
