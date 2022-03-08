Feature: Create Post
  In order to delete a post
  As a user
  I want to delete a post which I created

  Scenario: Create Post
    Given I login as "demo" with password "password"
    When I create a post with text "create post 1" and author username "demo"
    Then The response code is 201
    And It has been created a post with text "create post 1" and author username "demo"

  Scenario: Create Post without log in
    Given There is a registered user with username "marc" and password "password" and email "marc@sample.app"
    When I create a post with text "create post 1" and author username "marc"
    Then The response code is 401
    And Username "marc" has not created a post

  Scenario: Create Post logged with other user
    Given There is a registered user with username "marc" and password "password" and email "marc@sample.app"
    And I login as "demo" with password "password"
    When I create a post with text "create post 1" and author username "marc"
    Then The response code is 403
    And Username "marc" has not created a post

  Scenario: Create many posts by a user
    Given I login as "demo" with password "password"
    When I create a post with text "create post 1" and author username "demo"
    And I create a post with text "create post 2" and author username "demo"
    And I create a post with text "create post 3" and author username "demo"
    Then The response code is 201
    And Username "demo" has created "3" posts
    And It has been created a post with text "create post 3" and author username "demo"

  Scenario: Create a comment in a post with log in
    Given I login as "demo" with password "password"
    And I create a post with text "create post 1" and author username "demo"
    When I create a post with text "create comment 1" and author username "demo"
    And The post with text "create comment 1" is a comment from post with text "create comment 1" with author "demo"
    Then The response code is 201
    And It has been created a comment "create comment 1" with author username "demo"