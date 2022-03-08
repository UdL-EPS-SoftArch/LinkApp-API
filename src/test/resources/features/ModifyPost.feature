Feature: Modify Post
  In order to modify a post
  As a user
  I want to modify a post which I created

  Scenario: Modify an existing post
    Given I login as "demo" with password "password"
    And There is a post with id "1" created by a user with username "demo"
    When I modify a post with id "1" with new text "new text"
    Then The response code is 200
    And It has been modified a post with id "1" with the text "new text"

