Feature: Modify Post
  In order to modify a post
  As a user
  I want to modify a post which I created

  Scenario: Modify an existing post
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And I login as "user" with password "existing"
    And A group exists
    And The user "user" belongs to that group as "SUBSCRIBED"
    And I create a post with text "hola"
    When I modify the post just created with new text "new text"
    Then The response code is 200
    And It has been modified the post just created with the text "new text"

  Scenario: Modify a post created by another user
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And A group exists
    And The user "user" belongs to that group as "SUBSCRIBED"
    And I login as "user" with password "existing"
    And I create a post with text "hola"
    And I'm not logged in
    And The user "demo" belongs to that group as "SUBSCRIBED"
    And I login as "demo" with password "password"
    When I modify the post just created with new text "new text"
    Then The response code is 403
    And The post has not been modified

  Scenario: Modify a post created by another user as an ADMIN
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And A group exists
    And The user "user" belongs to that group as "ADMIN"
    And I login as "user" with password "existing"
    And I create a post with text "hola"
    And I'm not logged in
    And The user "demo" belongs to that group as "SUBSCRIBED"
    And I login as "demo" with password "password"
    When I modify the post just created with new text "new text"
    Then The response code is 403
    And The post has not been modified

  Scenario: Modify an unexisting post
    Given I login as "demo" with password "password"
    And A group exists
    And The user "demo" belongs to that group as "SUBSCRIBED"
    And There is no post created
    When I modify the post with id "10000" with new text "something"
    Then The response code is 404

  Scenario: Modify an existing comment from a post
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And A group exists
    And The user "user" belongs to that group as "SUBSCRIBED"
    And I login as "user" with password "password"
    And I create a post with text "create post 1"
    And I'm not logged in
    And The user "demo" belongs to that group as "SUBSCRIBED"
    And I login as "demo" with password "password"
    And I create a comment to the previous post with text "create comment 1"
    When I modify the comment just created with new text "new text"
    Then The response code is 200
    And It has been modified the comment just created with the text "new text"

  Scenario: Modify a comment created by another user
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And A group exists
    And The user "user" belongs to that group as "SUBSCRIBED"
    And I login as "user" with password "existing"
    And I create a post with text "create post 1"
    And I create a comment to the previous post with text "create comment 1"
    And I'm not logged in
    And The user "demo" belongs to that group as "SUBSCRIBED"
    And I login as "demo" with password "password"
    When I modify the post just created with new text "new text"
    Then The response code is 403
    And The comment has not been modified

  Scenario: Modify a comment created by another user as an ADMIN
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And A group exists
    And The user "user" belongs to that group as "ADMIN"
    And I login as "user" with password "existing"
    And I create a post with text "create post 1"
    And I create a comment to the previous post with text "create comment 1"
    And I'm not logged in
    And The user "demo" belongs to that group as "SUBSCRIBED"
    And I login as "demo" with password "password"
    When I modify the post just created with new text "new text"
    Then The response code is 403
    And The comment has not been modified
