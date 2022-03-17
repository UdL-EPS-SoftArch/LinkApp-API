Feature: Delete UserRole
  In order to interact with a group
  As a user
  I want to leave a group

  Scenario: Leave a group being subscribed
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" belongs to that group as "SUBSCRIBED"
    When The user "user" leaves the group
    Then The response code is 204