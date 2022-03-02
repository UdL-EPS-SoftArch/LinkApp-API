Feature: Delete Meet
  As a user
  I want to delete a meet

  Scenario: Delete a new Meet in a group I belong to and am a AUTHORIZED
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" belongs to that group as "AUTHORIZED"
    And I create a meet in that group with title "title", description "description", maxUsers 10, location "location"
    When I delete the meet
    Then The response code is 204

  Scenario: Delete a new Meet in a group I belong to and am an ADMIN
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" belongs to that group as "ADMIN"
    And I create a meet in that group with title "title", description "description", maxUsers 10, location "location"
    When I delete the meet
    Then The response code is 204

  Scenario: Delete a new Meet in a group I belong to and am an SUBSCRIBED
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" belongs to that group as "ADMIN"
    And I create a meet in that group with title "title", description "description", maxUsers 10, location "location"
    And I update the user "demo" role of the group to "SUBSCRIBED"
    When I delete the meet
    Then The response code is 403