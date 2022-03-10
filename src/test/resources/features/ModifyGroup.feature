Feature: ModifyGroup
  Modify a Group
  As a User

Scenario: Modify group
  Given I login as "demo" with password "password"
  And I Create a public Group called "GEIADE" with description "Generacio de GEIADE 2017-2022"
  When The user "demo" modifies the group description to "2022 Generation"
  Then The response code is 204
  And The description of the group is now "2022 Generation"