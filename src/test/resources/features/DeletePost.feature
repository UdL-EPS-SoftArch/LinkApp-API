Feature: Delete Post
  In order to delete a post
  As a user
  I want to delete a post which I created

  Scenario Outline: Delete an existing post
    Given I login as "<username>" with password "<password>"
    And There is a post created by a user with username "<user>"
    When I delete a post with id "<id>"
    Then The response code is <code>
    And It has been deleted a post with id "<id>"
    Examples:
      | username | password | id | user | code |
      | user     | password | id | user | 204  |
