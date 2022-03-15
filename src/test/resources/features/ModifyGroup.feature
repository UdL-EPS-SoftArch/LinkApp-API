Feature: ModifyGroup
  Modify a Group
  As a User

Scenario: Modify group
  Given I login as "demo" with password "password"
  And A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022"
  And The user "demo" is a User "ADMIN" of the group
  When A user "demo" modifies the group description to "2022 Generation"
  Then The response code is 204
  And The description of the group is now "2022 Generation"

  Scenario: Modify group but has not permission
    Given I login as "demo" with password "password"
    And A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022"
    And The user "demo" is a User "SUBSCRIBED" of the group
    When A user "demo" modifies the group description to "2022 Generation"
    Then The response code is 403

  Scenario: Modify group but not belongs to group
    Given I login as "demo" with password "password"
    And A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022"
    When A user "random" modifies the group description to "2022 Generation"
    Then The response code is 403

  Scenario: Modify group as owner of the group
    Given I login as "demo" with password "password"
    And The user "demo" creates a group with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022"
    When A user "demo" modifies the group description to "2022 Generation"
    Then The response code is 204
    And The description of the group is now "2022 Generation"
