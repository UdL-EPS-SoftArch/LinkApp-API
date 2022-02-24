Feature: CreateGroup
  Create a Group
  As a User

  Scenario: Create new Group
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    When I Create a public Group called "GEIADE" with description "Generacio de GEIADE 2017-2022"
    Then The response code is 201
    #And It has been created a Group with title "GEIADE" and description "Generacio de GEIADE 2017-2022"

