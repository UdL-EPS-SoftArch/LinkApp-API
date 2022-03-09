Feature: DeleteGroup
  Delete a Group
  As a User

  Scenario: Delete group
    Given I login as "demo" with password "password"
    And I Create a public Group called "GEIADE" with description "Generacio de GEIADE 2017-2022"
    When The user deletes the group
    Then The response code is 204