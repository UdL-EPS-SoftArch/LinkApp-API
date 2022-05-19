Feature: Create Message
  As an assistant of a Meet
  I want to send a message

  Background:
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And A group exists with a meet
    And I login as "user" with password "password"

  Scenario: Send a Message in a meet I am assisting
    Given The user "user" joins the group as "AUTHORIZED"
    And The user "user" is assisting to the meet
    When I send a message to the meet with message "message"
    Then The response code is 201
    And It has been created a message with message "message"
    And The creation time of the message is recent
    And The author of the message is correct

  Scenario: Send a blank message in a meet I am assisting
    Given The user "user" joins the group as "AUTHORIZED"
    And The user "user" is assisting to the meet
    When I send a message to the meet with message ""
    Then The response code is 400
    And The error message is "must not be blank"

  Scenario: Send a Message in a meet I am not assisting
    Given The user "user" joins the group as "AUTHORIZED"
    And The user "user" is not assisting to the meet
    When I send a message to the meet with message "message"
    Then The response code is 403

  Scenario: Send a Message in a meet of a group I do not belong to
    Given I login as "user" with password "password"
    When I send a message to the meet with message "message"
    Then The response code is 403

  Scenario: Send a Message in a closed meet
    Given The user "user" is assisting to the meet
    And The meet has closed
    When I send a message to the meet with message "message"
    Then The response code is 403

  Scenario: Send a Message in a meet of a group I do not belong to
    When I send a message to the meet with message "message"
    Then The response code is 403


