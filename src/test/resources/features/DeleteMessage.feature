Feature: Delete Message
  As an user
  I want to delete a message

  Background:
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And A group exists with a meet

  Scenario: Delete a Message in a meet I am assisting
    Given I login as "user" with password "password"
    And The user "user" belongs to that group as "AUTHORIZED"
    And The user "user" is assisting to the meet
    And I send a message to the meet with message "message"
    When I delete the message
    Then The response code is 403



