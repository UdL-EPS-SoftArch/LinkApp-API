Feature: Modify Meet
  As a user
  I want to modify a meet

  Scenario: Create a new Meet and edit it
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" joins the group as "AUTHORIZED"
    When I create a meet in that group with title "title", description "description", maxUsers 10, location "location"
    Then The response code is 201
    When I edit the meet with title "title2", description "description2", maxUsers 5, location "location2"
    Then The response code is 200
    And It has been created a meet with title "title2", description "description2", maxUsers 5, location "location2", status "true"
    And The edition time of the meet is recent

  Scenario: Create a new Meet and patch it
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" joins the group as "AUTHORIZED"
    When I create a meet in that group with title "title", description "description", maxUsers 10, location "location"
    Then The response code is 201
    When I patch the meet with title "title2", description "description2", maxUsers 5, location "location2"
    Then The response code is 200
    And It has been created a meet with title "title2", description "description2", maxUsers 5, location "location2", status "true"
    And The edition time of the meet is recent

  Scenario: Update meet status with cron job
    Given A group exists
    And I create a meet that ends in the past in that group
    When The cron status job is executed
    Then Then the meet status is false
