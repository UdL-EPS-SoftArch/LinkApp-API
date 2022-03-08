Feature: Edit Message
  As an assistant of a Meet
  I want to edit a message

  Background:
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And A group exists with a meet

  Scenario: Edit a Message
    Given I login as "user" with password "password"
    And The user "user" belongs to that group as "AUTHORIZED"
    And The user "user" is assisting to the meet
    When I send a message to the meet with message "message"
    Then The response code is 201
    When I edit the message with message "message-edited"
    Then The response code is 403

