Feature: Delete Post
  In order to delete a post
  As a user
  I want to delete a post which I created

  Scenario: Delete an existing post
    Given I login as "demo" with password "password"
    And There is a post created by a user with username "demo" with text "hola"
    When I delete the post
    Then The response code is 204
    And It has been deleted the post

  Scenario: Delete an unexisting post
    Given I login as "demo" with password "password"
    And There is no post created with id "1"
    When I delete the post with id "1"
    Then The response code is 404

  Scenario: Delete a post created by another user
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And I login as "user" with password "existing"
    And There is a post created by a user with username "user" with text "hola"
    And I'm not logged in
    And I login as "demo" with password "password"
    When I delete the post
    Then The response code is 403

  Scenario: Delete an existing comment from a post
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And I login as "user" with password "existing"
    And There is a post created by a user with username "user" with text "hola"
    And I'm not logged in
    And I login as "demo" with password "password"
    And There is a comment created by a user with username "demo" with text "hola" from the post just created by user with username "user"
    When I delete the post
    Then The response code is 204
    And It has been deleted the post

  Scenario: Delete a comment created by another user
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And I login as "user" with password "existing"
    And There is a post created by a user with username "user" with text "ei"
    And There is a comment created by a user with username "user" with text "hola" from the post just created by user with username "user"
    And I'm not logged in
    And I login as "demo" with password "password"
    When I delete the post
    Then The response code is 403
