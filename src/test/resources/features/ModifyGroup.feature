Feature: CreateGroup
  Create a Group
  As a User

Scenario: Modify group
  Given I login as "demo" with password "password"
  And A already created group where with name "GEIADE", id 1 and description "Generacio de GEIADE 2017-2022"
  And The user "demo" is an Admin "ADMIN" of the group
  When The user "demo" modifies the group 1 description to "2022 Generation"
  Then The response code is 204
  And The description of group 1 now is "2022 Generation"