Feature: CreateGroup
  Create a Group
  As a User

Scenario: Modify group
  Given I login as "demo" with password "password"
  And A already created group where with name "GEIADE", id 1 and description "Generacio de GEIADE 2017-2022"
  And The user "demo" belongs to that group as "ADMIN"
  When The user "demo" modifies the group 1 description to "2022 Generation"
  Then The response code is 201
