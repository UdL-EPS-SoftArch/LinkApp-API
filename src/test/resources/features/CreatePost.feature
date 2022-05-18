Feature: Create Post
  In order to create a post
  As a user
  I want to create a new post

  Scenario: Create Post
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" joins the group as "ADMIN"
    When I create a post with text "create post 1"
    Then The response code is 201
    And There is a post created with text "create post 1"

  Scenario: Create Post without log in
    Given There is a registered user with username "marc" and password "password" and email "marc@sample.app"
    And I'm not logged in
    And A group exists
    And The user "demo" joins the group as "ADMIN"
    When I create a post with text "create post 1"
    Then The response code is 401

  Scenario: Create many posts by a user
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" joins the group as "ADMIN"
    When I create a post with text "create post 1"
    And I create a post with text "create post 2"
    And I create a post with text "create post 3"
    Then The response code is 201
    And There are "3" posts created by "demo"

  Scenario: Create a comment in a post with log in
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" joins the group as "ADMIN"
    And I create a post with text "create post 1"
    When I create a comment to the previous post with text "create comment 1"
    Then The response code is 201
    And The post with text "create comment 1" is a comment from post with text "create post 1"
