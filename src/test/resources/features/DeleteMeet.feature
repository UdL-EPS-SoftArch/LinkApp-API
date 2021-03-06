Feature: Delete Meet
  As a user
  I want to delete a meet

  Background:
    Given I login as "demo" with password "password"
    And A group exists

  Scenario: Delete a new Meet in a group I belong to and am a AUTHORIZED
    Given The user "demo" joins the group as "AUTHORIZED"
    And I create a meet in that group with title "title", description "description", maxUsers 10, location "location"
    When I delete the meet
    Then The response code is 204

  Scenario: Delete a new Meet in a group I belong to and am an ADMIN
    Given The user "demo" joins the group as "ADMIN"
    And I create a meet in that group with title "title", description "description", maxUsers 10, location "location"
    When I delete the meet
    Then The response code is 204

  Scenario: Delete a new Meet in a group I belong to and am an SUBSCRIBED
    Given The user "demo" joins the group as "ADMIN"
    And There is a registered user with username "demo2" and password "password" and email "user@sample.app"
    And The user "demo" belongs to that group as "ADMIN"
    And I login as "demo2" with password "password"
    And The user "demo2" joins the group as "ADMIN"
    And I create a meet in that group with title "title", description "description", maxUsers 10, location "location"
    And I login as "demo" with password "password"
    And I update the user "demo2" role of the group to "SUBSCRIBED"
    When I login as "demo2" with password "password"
    And I delete the meet
    Then The response code is 403
