Feature: Create Meet
  As a user
  I want to create a meet

  Scenario: Create a new Meet in a group I belong to and having sufficient permissions
    Given I am authenticated as "user"
    And The group with id 1 exists
    And The user "user" belongs to the group 1 as "ADMIN"
    When I create a meet in the group with id 1 with title "title", description "description", maxUsers 10, location "location"
    Then The response code is 201
#    And It has been created a meet with meet with title "title", status "true", description "description", maxUsers "10", location "location", meetDate ""
