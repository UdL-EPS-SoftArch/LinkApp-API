Feature: Delete Post
  In order to delete a post
  As a user
  I want to delete a post which I created

  Scenario: Delete an existing post
    Given I login as "demo" with password "password"
    And There is a post with id "1" created by a user with username "demo"
    When I delete a post with id "1"
    Then The response code is 204
    And It has been deleted a post with id "1"

  Scenario: Delete an unexisting post
    Given I login as "demo" with password "password"
    And There is no post created with id "1"
    When I delete a post with id "1"
    Then The response code is 404

  Scenario: Delete a post created by another user
    Given I login as "demo" with password "password"
    And There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And There is a post with id "1" created by a user with username "user"
    When I delete a post with id "1"
    Then The response code is 403

