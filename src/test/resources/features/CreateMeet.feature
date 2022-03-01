Feature: Create Meet
  As a user
  I want to create a meet

  Scenario: Create a new Meet in a group I belong to and having sufficient permissions
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" belongs to that group as "ADMIN"
    When I create a meet in that group with title "title", description "description", maxUsers 10, location "location"
    Then The response code is 201
    And It has been created a meet with title "title", description "description", maxUsers 10, location "location", status "true", meetDate "{string}"
