Feature: Delete Post
  In order to delete a post
  As a user
  I want to delete a post which I created

  Scenario: Delete an existing post as an SUBSCRIBED user
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" belongs to that group as "SUBSCRIBED"
    And I create a post with text "create post 1"
    When I delete the post
    Then The response code is 204
    And It has been deleted the post

  Scenario: Delete an unexisting post as an SUBSCRIBED user
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" belongs to that group as "SUBSCRIBED"
    And There is no post created
    When I delete the post
    Then The response code is 404

  Scenario: Delete a post created by another user as an SUBSCRIBED user
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And A group exists
    And The user "user" belongs to that group as "SUBSCRIBED"
    And I login as "user" with password "existing"
    And I create a post with text "hola"
    And I'm not logged in
    And The user "demo" belongs to that group as "SUBSCRIBED"
    And I login as "demo" with password "password"
    When I delete the post
    Then The response code is 403

  Scenario: Delete a post created by another user as an ADMIN user
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And A group exists
    And The user "user" belongs to that group as "SUBSCRIBED"
    And I login as "user" with password "existing"
    And I create a post with text "hola"
    And I'm not logged in
    And I login as "demo" with password "password"
    And The user "demo" belongs to that group as "ADMIN"
    When I delete the post
    Then The response code is 204

  Scenario: Delete an existing comment from a post as an SUBSCRIBED user
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And A group exists
    And The user "user" belongs to that group as "SUBSCRIBED"
    And I login as "demo" with password "password"
    And The user "demo" belongs to that group as "SUBSCRIBED"
    And I create a post with text "create post 1"
    And I'm not logged in
    And I login as "user" with password "existing"
    And I create a comment to the previous post with text "create comment 1"
    When I delete the comment
    Then The response code is 204
    And It has been deleted the comment

  Scenario: Delete a comment created by another user as an SUBSCRIBED user
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And A group exists
    And The user "user" belongs to that group as "SUBSCRIBED"
    And I login as "user" with password "existing"
    And The user "demo" belongs to that group as "SUBSCRIBED"
    And I create a post with text "create post 1"
    And I create a comment to the previous post with text "create comment 1"
    And I'm not logged in
    And I login as "demo" with password "password"
    When I delete the comment
    Then The response code is 403

  Scenario: Delete a comment created by another user as an ADMIN user
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And A group exists
    And The user "user" belongs to that group as "SUBSCRIBED"
    And I login as "user" with password "existing"
    And The user "demo" belongs to that group as "ADMIN"
    And I create a post with text "create post 1"
    And I create a comment to the previous post with text "create comment 1"
    And I'm not logged in
    And I login as "demo" with password "password"
    When I delete the comment
    Then The response code is 204
    And It has been deleted the comment

  Scenario: Delete an existing comment from a post as an SUBSCRIBED user
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And A group exists
    And The user "user" belongs to that group as "SUBSCRIBED"
    And The user "demo" belongs to that group as "SUBSCRIBED"
    And I login as "demo" with password "password"
    And I create a post with text "create post 1"
    And I'm not logged in
    And I login as "user" with password "existing"
    And I create a comment to the previous post with text "create comment 1"
    And I'm not logged in
    And I login as "demo" with password "password"
    When I delete the post
    Then The response code is 204
    And It has been deleted the post
    And It has been deleted the comment

  Scenario: Prova
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And A group exists
    And The user "user" belongs to that group as "SUBSCRIBED"
    And The user "demo" belongs to that group as "SUBSCRIBED"
    And I login as "demo" with password "password"
    And I create a post with text "create post 1"
    And I'm not logged in
    And I login as "user" with password "existing"
    And I create a comment to the previous post with text "create comment 1"
    When I delete the comment
    Then The response code is 204
    And The post has not been deleted
    And It has been deleted the comment

