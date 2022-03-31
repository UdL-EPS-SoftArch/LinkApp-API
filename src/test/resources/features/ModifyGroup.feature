Feature: ModifyGroup
  Modify a Group
  As a User

  Background:
    Given I login as "demo" with password "password"

  Scenario: Modify group
    Given A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022"
    And The user "demo" is a User "ADMIN" of the group
    When A user "demo" modifies the group description to "2022 Generation"
    Then The response code is 204
    And The description of the group is now "2022 Generation"

  Scenario: Add a theme to a group that has no related themes
    Given A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022"
    And The number of related themes is 0
    And The user "demo" is a User "ADMIN" of the group
    When A user "demo" adds the theme "CULTURE"
    Then The response code is 204
    And The number of related themes is 1

  Scenario: Add a theme to a group that has 1 related theme
    Given A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022" and theme "ARTS"
    And The number of related themes is 1
    And The user "demo" is a User "ADMIN" of the group
    When A user "demo" adds the theme "CULTURE"
    Then The response code is 204
    And The number of related themes is 2

  Scenario: Add a theme to a group that has more than 1 related themes
    Given A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022" and theme "ARTS" and "CULTURE"
    And The number of related themes is 2
    And The user "demo" is a User "ADMIN" of the group
    When A user "demo" adds the theme "CARS"
    Then The response code is 204
    And The number of related themes is 3

  Scenario: Add a theme to a group that is already related to that theme
    Given A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022" and theme "ARTS" and "CULTURE"
    And The number of related themes is 2
    And The user "demo" is a User "ADMIN" of the group
    When A user "demo" adds the theme "ARTS"
    Then The response code is 422
    And The number of related themes is 2

  Scenario: Add an nonexistent theme to a group
    Given A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022" and theme "ARTS" and "CULTURE"
    And The number of related themes is 2
    And The user "demo" is a User "ADMIN" of the group
    When A user "demo" adds the theme "ARRRRTS"
    Then The response code is 400
    And The number of related themes is 2

  Scenario: Add a group of themes to a group
    Given A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022"
    And The number of related themes is 0
    And The user "demo" is a User "ADMIN" of the group
    When A user "demo" adds the themes "ARTS", "GASTRONOMY", "FITNESS"
    Then The response code is 204
    And The number of related themes is 3

  Scenario: Delete a theme related to a group
    Given A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022" and theme "ARTS" and "CULTURE"
    And The number of related themes is 2
    And The user "demo" is a User "ADMIN" of the group
    When A user "demo" deletes the theme "ARTS"
    Then The response code is 204
    And The number of related themes is 1

  Scenario: Delete a nonexistent theme
    Given A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022" and theme "ARTS" and "CULTURE"
    And The number of related themes is 2
    And The user "demo" is a User "ADMIN" of the group
    When A user "demo" deletes the theme "ARRRRTS"
    Then The number of related themes is 2

  Scenario: Delete a non-related theme from a group
    Given A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022" and theme "ARTS" and "CULTURE"
    And The number of related themes is 2
    And The user "demo" is a User "ADMIN" of the group
    When A user "demo" deletes the theme "CARS"
    Then The number of related themes is 2

  Scenario: Deletes a group of themes of a group
    Given A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022" and theme "ARTS" and "CULTURE"
    And The number of related themes is 2
    And The user "demo" is a User "ADMIN" of the group
    When A user "demo" deletes the themes "ARTS", "CULTURE"
    Then The response code is 204
    And The number of related themes is 0

  Scenario: Modify group but has not permission
    Given A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022"
    And The user "demo" is a User "SUBSCRIBED" of the group
    When A user "demo" modifies the group description to "2022 Generation"
    Then The response code is 403

  Scenario: Modify group but not belongs to group
    Given A already created group where with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022"
    When A user "random" modifies the group description to "2022 Generation"
    Then The response code is 403

  Scenario: Modify group as owner of the group
    Given The user "demo" creates a group with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022"
    When A user "demo" modifies the group description to "2022 Generation"
    Then The response code is 204
    And The description of the group is now "2022 Generation"
