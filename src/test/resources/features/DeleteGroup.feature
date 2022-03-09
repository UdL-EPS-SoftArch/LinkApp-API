Feature: DeleteGroup
  Delete a Group
  As a User

  Scenario: Delete group
    Given I login as "demo" with password "password"
    And A already created group where with name "GEIADE", id 1 and description "Generacio de GEIADE 2017-2022"
    And The user "demo" is an Admin "ADMIN" of the group
    When The user "demo" deletes the group 1
    Then The response code is 204


  Scenario: User wants to delete a Group but can't
    Given I login as "demo" with password "password"
    And A already created group where with name "GEIADE", id 1 and description "Generacio de GEIADE 2017-2022"
    And The user "demo" is an Admin "SUBSCRIBED" of the group
    When The user "demo" deletes the group 1
    Then The response code is 403