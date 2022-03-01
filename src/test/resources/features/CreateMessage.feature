Feature: Create Message
  As an assistant of a Meet
  I want to send a message

  Background:
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And The group with id 1 exists
    And The meet with id 1 belongs to the group with id 1

  Scenario: Send a Message in a meet I am assisting
  # TO-DO differences in user permissions when highlighting messages
    Given I am authenticated as "user"
    And The user "user" belongs to the group with id 1
    # Meet class have similar tests --> TO-DO differences in user permissions when belonging to a group
    And The user "user" is assisting to the meet with id 1
    When I send a message in the meet with id 1 with message "message"
    Then The response code is 201
    # And It has been created a message with meet with (...)
