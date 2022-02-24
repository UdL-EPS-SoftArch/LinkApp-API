Feature: Delete Post
  In order to delete a post
  As a user
  I want to delete a post which I created

  Scenario: Delete an existing post
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I can login with username "user" and password "password"
    And It exists a post with id "id" created by username "user"
    When I delete a post with id "id"
    Then The response code is 204
    And It has been deleted a post with id "id"
