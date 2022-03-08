Feature: CreateGroup
  Create a Group
  As a User

  Scenario: Create new Group
    Given I login as "demo" with password "password"
    When I Create a public Group called "GEIADE" with description "Generacio de GEIADE 2017-2022"
    Then The response code is 201
    And It has been created a Group with title "GEIADE" and description "Generacio de GEIADE 2017-2022"
