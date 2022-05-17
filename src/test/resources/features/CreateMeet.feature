Feature: Create Meet
  As a user
  I want to create a meet

  Background:
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" joins the group as "ADMIN"

  Scenario: Create a new Meet in a group I belong to and have sufficient permissions
    When I create a meet in that group with title "title", description "description", maxUsers 10, location "location"
    Then The response code is 201
    And It has been created a meet with title "title", description "description", maxUsers 10, location "location", status "true"
    And  The creation time of the meet is recent

  Scenario: Register a meet with empty title
    When I create a meet in that group with title "", description "description", maxUsers 10, location "location"
    Then The response code is 400
    And The error message is "must not be blank"

  Scenario: Register a meet with empty description
    When I create a meet in that group with title "title", description "", maxUsers 10, location "location"
    Then The response code is 400
    And The error message is "must not be blank"

  Scenario: Create a new Meet in a group where I don't have sufficient permissions
    And A group exists
    And The user "demo" joins the group as "SUBSCRIBED"
    When I create a meet in that group with title "title", description "description", maxUsers 10, location "location"
    Then The response code is 403

  Scenario: Create a new Meet without a group
    And The user "demo" does not belong to the group
    When I create a meet in that group with title "title", description "description", maxUsers 10, location "location"
    Then The response code is 403

  Scenario: Create a new Meet in a group I belong to and am a AUTHORIZED
    When I create a meet in that group with title "title", description "description", maxUsers 10, location "location"
    Then The response code is 201

  Scenario: Create a new Meet with initial date in the past
    When I create a meet in that group with initial date in the past
    Then The response code is 422

  Scenario: Create a new Meet with final date before initial date
    When I create a meet in that group with final date before initial date
    Then The response code is 422

  Scenario: Create a Meet force max users
    When I create a meet in that group with title "title", description "description", maxUsers 1, location "location"
    Then The response code is 201
    Given There is no registered user with username "demo2"
    And I'm not logged in
    When I register a new user with username "demo2", email "demo2@sample.app" and password "password2"
    And I login as "demo2" with password "password2"
    And The user "demo2" joins the group as "SUBSCRIBED"
    When The user "demo2" tries to attend the meeting
    Then The response code is 422

  Scenario: Create a Meet and do not force max users
    When I create a meet in that group with title "title", description "description", maxUsers 2, location "location"
    Then The response code is 201
    Given There is no registered user with username "demo2"
    And I'm not logged in
    When I register a new user with username "demo2", email "demo2@sample.app" and password "password2"
    And I login as "demo2" with password "password2"
    And The user "demo2" joins the group as "SUBSCRIBED"
    When The user "demo2" tries to attend the meeting
    Then The response code is 201
