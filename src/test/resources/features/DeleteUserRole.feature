Feature: Delete UserRole
  In order to interact with a group
  As a user
  I want to leave a group

  Scenario: Leave a group being subscribed
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "SUBSCRIBED"
    When The user "user" leaves the group
    Then The response code is 204

  Scenario: Leave a group being the unique admin in the group
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "ADMIN"
    When The user "user" leaves the group
    Then The response code is 403

  Scenario: Leave a group being admin and there are other admins in the group
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "ADMIN"
    And The user "user2" joins the group as "ADMIN"
    When The user "user" leaves the group
    Then The response code is 204
