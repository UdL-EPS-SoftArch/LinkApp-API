Feature: Create Post
  As a user

  Scenario: Create Post
    Given I login as "demo" with password "password"
    When I create a post with text "create post 1"
    Then The response code is 201
    And It has been created a post with text "create post 1"

  Scenario: Create Post without log in
    Given There is a registered user with username "marc" and password "password" and email "marc@sample.app"
    When I create a post with text "create post 1"
    Then The response code is 401
    And Username "marc" has not created a post

  Scenario: Create many posts by a user
    Given I login as "demo" with password "password"
    When I create a post with text "create post 1"
    And I create a post with text "create post 2"
    And I create a post with text "create post 3"
    Then The response code is 201
    And Username "demo" has created "3" posts
    And It has been created a post with text "create post 3"

  Scenario: Create a comment in a post with log in
    Given I login as "demo" with password "password"
    And I create a post with text "create post 1"
    When I create a comment to the previous post with text "create comment 1"
    Then The response code is 201
    And The post with text "create comment 1" is a comment from post with text "create post 1"