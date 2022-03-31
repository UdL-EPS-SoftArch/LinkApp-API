Feature: DeleteGroup
  Delete a Group
  As a User

  Background:
    Given I login as "demo" with password "password"

  Scenario: Delete group
    Given A already created group where with name "GEIADE", id 1 and description "Generacio de GEIADE 2017-2022"
    And The user "demo" is a User "ADMIN" of the group
    When The user "demo" deletes the group 1
    Then The response code is 204


  Scenario: User wants to delete a Group but can't
    Given A already created group where with name "GEIADE", id 1 and description "Generacio de GEIADE 2017-2022"
    And The user "demo" is a User "SUBSCRIBED" of the group
    When The user "demo" deletes the group 1
    Then The response code is 403

  Scenario: User wants to delete a Group where doesn't belong
    Given A already created group where with name "GEIADE", id 1 and description "Generacio de GEIADE 2017-2022"
    When The user "random" deletes the group 1
    Then The response code is 403

  Scenario: User wants to delete a Group where he is Admin
    Given The user "demo" creates a group with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022"
    When The user "demo" deletes the group 1
    Then The response code is 204

  Scenario: User deletes a Group and all the meets at the same time
    Given The user "demo" creates a group with name "GEIADE", id 1 and description "Generacio GEIADE 2017-2022"
    And A post is created in that group
    When The user "demo" deletes the group 1
    Then I check if the post has been deleted
    Then The response code is 404