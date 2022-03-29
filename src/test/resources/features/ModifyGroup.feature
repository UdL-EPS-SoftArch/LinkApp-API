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

  Scenario: Add a theme to a group that has no related themes
    Given I login as "demo" with password "password"
    And A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022"
    And The number of related themes is 0
    And The user "demo" is a User "ADMIN" of the group
    When A user "demo" adds the group theme "CULTURE"
    Then The response code is 204
    And The number of related themes is 1

  Scenario: Add a theme to a group that has 1 related theme
    Given I login as "demo" with password "password"
    And A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022" and theme "ARTS"
    And The number of related themes is 1
    And The user "demo" is a User "ADMIN" of the group
    When A user "demo" adds the group theme "CULTURE"
    Then The response code is 204
    And The number of related themes is 2

  Scenario: Add a theme to a group that has more than 1 related themes
    Given I login as "demo" with password "password"
    And A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022" and theme "ARTS" and "CULTURE"
    And The number of related themes is 2
    And The user "demo" is a User "ADMIN" of the group
    When A user "demo" adds the group theme "CARS"
    Then The response code is 204
    And The number of related themes is 3

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
