Feature: Register UserRole
  In order to interact with a group
  As a user
  I want to register and delete new roles

  Scenario: Subscribe to a new group
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And A group exists
    When The user "user" belongs to that group as "SUBSCRIBED"
    Then The response code is 200


