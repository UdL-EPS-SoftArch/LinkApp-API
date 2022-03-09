Feature: Modify Post
  In order to modify a post
  As a user
  I want to modify a post which I created

  Scenario: Modify an existing post
    Given I login as "demo" with password "password"
    And There is a post created by a user with username "demo" with text "hola"
    When I modify the post just created with new text "new text"
    Then The response code is 200
    And It has been modified the post just created with the text "new text"

  Scenario: Modify a post created by another user
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And I login as "user" with password "existing"
    And There is a post created by a user with username "user" with text "hola"
    And I'm not logged in
    And I login as "demo" with password "password"
    When I modify the post just created with new text "new text"
    Then The response code is 403
    And The post has not been modified

  Scenario: Modify an unexisting post
    Given I login as "demo" with password "password"
    And There is no post created with id "1"
    When I modify the post with id "1" with new text "new text"
    Then The response code is 404

  Scenario: Modify an existing comment from a post
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And I login as "user" with password "existing"
    And There is a post created by a user with username "user" with text "ei"
    And I'm not logged in
    And I login as "demo" with password "password"
    And There is a comment created by a user with username "demo" with text "hola" from the post just created by user with username "user"
    When I modify the post just created with new text "new text"
    Then The response code is 200
    And It has been modified the post just created with the text "new text"

  Scenario: Modify a comment created by another user
    Given There is a registered user with username "user" and password "existing" and email "user@sample.app"
    And I login as "user" with password "existing"
    And There is a post created by a user with username "user" with text "hola"
    And There is a comment created by a user with username "user" with text "hola" from the post just created by user with username "user"
    And I'm not logged in
    And I login as "demo" with password "password"
    When I modify the post just created with new text "new text"
    Then The response code is 403
    And The post has not been modified

