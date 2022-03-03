Feature: Create Post
  In order to delete a post
  As a user
  I want to delete a post which I created

  Scenario: Create Post
    Given There is a registered user with username "xavier" and password "password" and email "user@sample.app"
    And I login as "xavier" with password "password"
    When I create a post with text "create post 1" and author username "xavier"
    Then The response code is 201
    And It has been created a post with text "create post 1" and author username "xavier"


  #And I can login with username "user" and password "password"
  #And It exists a post with id "id" created by username "user"
  #When I delete a post with id "id"
  #Then The response code is 204
  #And It has been deleted a post with id "id"