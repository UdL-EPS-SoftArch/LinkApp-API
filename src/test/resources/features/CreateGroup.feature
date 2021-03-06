Feature: CreateGroup
  Create a Group
  As a User

  Scenario: Create new Group
    Given I login as "demo" with password "password"
    When I Create a public Group called "GEIADE" with description "Generacio de GEIADE 2017-2022"
    Then The response code is 201
    And It has been created a Group with title "GEIADE" and description "Generacio de GEIADE 2017-2022"

  Scenario: Create new Group with a related theme
    Given I login as "demo" with password "password"
    When I Create a public Group called "GEIADE" with description "Generacio de GEIADE 2017-2022" and theme "ARTS"
    Then The response code is 201
    And It has been created a Group with title "GEIADE" and description "Generacio de GEIADE 2017-2022" and theme "ARTS"

  Scenario: Create new Group with blank title
    Given I login as "demo" with password "password"
    When I Create a public Group called "" with description "Generacio de GEIADE 2017-2022"
    Then The response code is 400
