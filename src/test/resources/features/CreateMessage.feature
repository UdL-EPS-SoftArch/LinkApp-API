Feature: Create Message
  As an assistant of a Meet
  I want to send a message

  Background:
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And A group exists with a meet

  Scenario: Send a Message in a meet I am assisting
    Given I login as "user" with password "password"
    And The user "user" belongs to that group as "AUTHORIZED"
    # Meet class have similar tests --> TO-DO differences in user permissions when belonging to a group
    And The user "user" is assisting to the meet
    When I send a message to the meet with message "message"
    Then The response code is 201
    # And It has been created a message with meet with (...)

  Scenario: Send a blank message in a meet I am assisting
    Given I login as "user" with password "password"
    And The user "user" belongs to that group as "AUTHORIZED"
    # Meet class have similar tests --> TO-DO differences in user permissions when belonging to a group
    And The user "user" is assisting to the meet
    When I send a message to the meet with message ""
    Then The response code is 400
    And The error message is "must not be blank"
    # And It has been created a message with meet with (...)

  Scenario: Send a Message in a meet I am not assisting
    Given I login as "user" with password "password"
    And The user "user" belongs to that group as "AUTHORIZED"
    # Meet class have similar tests --> TO-DO differences in user permissions when belonging to a group
    When I send a message to the meet with message "message"
    Then The response code is 403
    And The error message is "you cannot send a message to a meet you are not assisting"
    # And It has been created a message with meet with (...)

  Scenario: Send a Message in a meet of a group I do not belong to
    Given I login as "user" with password "password"
    # Meet class have similar tests --> TO-DO differences in user permissions when belonging to a group
    When I send a message to the meet with message "message"
    Then The response code is 403
    And The error message is "you cannot send a message to a meet of a group you do not belong to"
    # And It has been created a message with meet with (...)

