Feature: Delete Post
  In order to delete a post
  As a user
  I want to delete a post which I created

  Scenario: Delete an existing post
    Given I login as "demo" with password "password"
    And I create a post with text "create post 1"
    When I delete the post
    Then The response code is 204
    And It has been deleted the post

  Scenario: Delete an unexisting post
    Given I login as "demo" with password "password"
    And There is no post created
    When I delete the post
    Then The response code is 404

  Scenario: Delete a post created by another user
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And I login as "user" with password "existing"
    And I create a post with text "hola"
    And I'm not logged in
    And I login as "demo" with password "password"
    When I delete the post
    Then The response code is 403

  Scenario: Delete an existing comment from a post
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And I login as "demo" with password "password"
    And I create a post with text "create post 1"
    And I'm not logged in
    And I login as "user" with password "existing"
    And I create a comment to the previous post with text "create comment 1"
    When I delete the comment
    Then The response code is 204
    And It has been deleted the post

  Scenario: Delete a comment created by another user
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And I login as "user" with password "existing"
    And I create a post with text "create post 1"
    And I create a comment to the previous post with text "create comment 1"
    And I'm not logged in
    And I login as "demo" with password "password"
    When I delete the post
    Then The response code is 403
