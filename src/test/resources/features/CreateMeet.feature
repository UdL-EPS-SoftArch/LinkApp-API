Feature: Create Meet
  As a user
  I want to create a meet

  Scenario: Create a new Meet in a group I belong to and have sufficient permissions
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" belongs to that group as "ADMIN"
    When I create a meet in that group with title "title", description "description", maxUsers 10, location "location"
    Then The response code is 201
    And It has been created a meet with title "title", description "description", maxUsers 10, location "location", status "true"
    And  The creation time of the meet is recent

  Scenario: Register a meet with empty title
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" belongs to that group as "ADMIN"
    When I create a meet in that group with title "", description "description", maxUsers 10, location "location"
    Then The response code is 400
    And The error message is "must not be blank"

  Scenario: Register a meet with empty description
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" belongs to that group as "ADMIN"
    When I create a meet in that group with title "title", description "", maxUsers 10, location "location"
    Then The response code is 400
    And The error message is "must not be blank"

  Scenario: Create a new Meet in a group where I don't have sufficient permissions
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" belongs to that group as "SUBSCRIBED"
    When I create a meet in that group with title "title", description "description", maxUsers 10, location "location"
    Then The response code is 403

  Scenario: Create a new Meet in a group I belong to and am a AUTHORIZED
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" belongs to that group as "AUTHORIZED"
    When I create a meet in that group with title "title", description "description", maxUsers 10, location "location"
    Then The response code is 201


  Scenario: Create a new Meet and edit it
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" belongs to that group as "AUTHORIZED"
    When I create a meet in that group with title "title", description "description", maxUsers 10, location "location"
    Then The response code is 201
    When I edit the meet with title "title2", description "description2", maxUsers 5, location "location2"
    Then The response code is 200
    And It has been created a meet with title "title2", description "description2", maxUsers 5, location "location2", status "true"
    And The edition time of the meet is recent

  Scenario: Create a new Meet and patch it
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" belongs to that group as "AUTHORIZED"
    When I create a meet in that group with title "title", description "description", maxUsers 10, location "location"
    Then The response code is 201
    When I patch the meet with title "title2", description "description2", maxUsers 5, location "location2"
    Then The response code is 200
    And It has been created a meet with title "title2", description "description2", maxUsers 5, location "location2", status "true"
    And The edition time of the meet is recent
