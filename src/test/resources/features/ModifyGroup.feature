Feature: ModifyGroup
  Modify a Group
  As a User

Scenario: Modify group
  Given I login as "demo" with password "password"
  And A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022"
  When The allowed user "demo" modifies the group description to "2022 Generation"
  Then The response code is 204
  And The description of the group is now "2022 Generation"

  Scenario: Modify group but not achieved
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" belongs to that group as "ADMIN"
    When A NOT allowed user modifies the group description to "Whatever"
    Then Nothing happens
