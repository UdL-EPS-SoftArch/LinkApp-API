Feature: Modify User Role
  In order to manage permissions
  As a user
  I want to modify permissions of some users

  Scenario: Modify admin role of a user to authorized as admin
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "ADMIN"
    And I login as "user2" with password "password2"
    And The user "user2" joins the group as "ADMIN"
    And I login as "user" with password "password"
    When I update the user "user2" role of the group to "AUTHORIZED"
    Then The response code is 200
    Then The role of the user "user2" has been changed to "AUTHORIZED"

  Scenario: Modify admin role of a user to subscribed as admin
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "ADMIN"
    And I login as "user2" with password "password2"
    And The user "user2" joins the group as "ADMIN"
    And I login as "user" with password "password"
    When I update the user "user2" role of the group to "SUBSCRIBED"
    Then The response code is 200
    Then The role of the user "user2" has been changed to "SUBSCRIBED"

  Scenario: Modify authorized role of a user to subscribed as admin
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "ADMIN"
    And I login as "user2" with password "password2"
    And The user "user2" joins the group as "AUTHORIZED"
    And I login as "user" with password "password"
    When I update the user "user2" role of the group to "SUBSCRIBED"
    Then The response code is 200
    Then The role of the user "user2" has been changed to "SUBSCRIBED"

  Scenario: Modify subscribed role of a user to authorized as admin
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "ADMIN"
    And I login as "user2" with password "password2"
    And The user "user2" joins the group as "SUBSCRIBED"
    And I login as "user" with password "password"
    When I update the user "user2" role of the group to "AUTHORIZED"
    Then The response code is 200
    Then The role of the user "user2" has been changed to "AUTHORIZED"

  Scenario: Modify subscribed role of a user to admin as admin
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "ADMIN"
    And I login as "user2" with password "password2"
    And The user "user2" joins the group as "SUBSCRIBED"
    And I login as "user" with password "password"
    When I update the user "user2" role of the group to "ADMIN"
    Then The response code is 200
    Then The role of the user "user2" has been changed to "ADMIN"

  Scenario: Modify authorized role of a user to admin as admin
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "ADMIN"
    And I login as "user2" with password "password2"
    And The user "user2" joins the group as "AUTHORIZED"
    And I login as "user" with password "password"
    When I update the user "user2" role of the group to "ADMIN"
    Then The response code is 200
    Then The role of the user "user2" has been changed to "ADMIN"

  Scenario: Modify admin role of a user to authorized as subscribed
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "SUBSCRIBED"
    And I login as "user2" with password "password2"
    And The user "user2" joins the group as "ADMIN"
    And I login as "user" with password "password"
    When I update the user "user2" role of the group to "AUTHORIZED"
    Then The response code is 403

  Scenario: Modify admin role of a user to subscribed as subscribed
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "SUBSCRIBED"
    And I login as "user2" with password "password2"
    And The user "user2" joins the group as "ADMIN"
    And I login as "user" with password "password"
    When I update the user "user2" role of the group to "SUBSCRIBED"
    Then The response code is 403

  Scenario: Modify authorized role of a user to subscribed as subscribed
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "SUBSCRIBED"
    And I login as "user2" with password "password2"
    And The user "user2" joins the group as "AUTHORIZED"
    And I login as "user" with password "password"
    When I update the user "user2" role of the group to "SUBSCRIBED"
    Then The response code is 403

  Scenario: Modify subscribed role of a user to authorized as subscribed
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "SUBSCRIBED"
    And I login as "user2" with password "password2"
    And The user "user2" joins the group as "SUBSCRIBED"
    And I login as "user" with password "password"
    When I update the user "user2" role of the group to "AUTHORIZED"
    Then The response code is 403

  Scenario: Modify subscribed role of a user to admin as subscribed
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "SUBSCRIBED"
    And I login as "user2" with password "password2"
    And The user "user2" joins the group as "SUBSCRIBED"
    And I login as "user" with password "password"
    When I update the user "user2" role of the group to "ADMIN"
    Then The response code is 403

  Scenario: Modify authorized role of a user to admin as subscribed
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "SUBSCRIBED"
    And I login as "user2" with password "password2"
    And The user "user2" joins the group as "AUTHORIZED"
    And I login as "user" with password "password"
    When I update the user "user2" role of the group to "ADMIN"
    Then The response code is 403

  Scenario: Modify admin role of a user to authorized as authorized
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "AUTHORIZED"
    And I login as "user2" with password "password2"
    And The user "user2" joins the group as "ADMIN"
    And I login as "user" with password "password"
    When I update the user "user2" role of the group to "AUTHORIZED"
    Then The response code is 403

  Scenario: Modify admin role of a user to subscribed as authorized
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "AUTHORIZED"
    And I login as "user2" with password "password2"
    And The user "user2" joins the group as "ADMIN"
    And I login as "user" with password "password"
    When I update the user "user2" role of the group to "SUBSCRIBED"
    Then The response code is 403

  Scenario: Modify authorized role of a user to subscribed as authorized
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "AUTHORIZED"
    And I login as "user2" with password "password2"
    And The user "user2" joins the group as "AUTHORIZED"
    And I login as "user" with password "password"
    When I update the user "user2" role of the group to "SUBSCRIBED"
    Then The response code is 403

  Scenario: Modify subscribed role of a user to authorized as authorized
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "AUTHORIZED"
    And I login as "user2" with password "password2"
    And The user "user2" joins the group as "SUBSCRIBED"
    And I login as "user" with password "password"
    When I update the user "user2" role of the group to "AUTHORIZED"
    Then The response code is 403

  Scenario: Modify subscribed role of a user to admin as authorized
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "AUTHORIZED"
    And I login as "user2" with password "password2"
    And The user "user2" joins the group as "SUBSCRIBED"
    And I login as "user" with password "password"
    When I update the user "user2" role of the group to "ADMIN"
    Then The response code is 403

  Scenario: Modify authorized role of a user to admin as authorized
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And There is a registered user with username "user2" and password "password2" and email "user2@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "AUTHORIZED"
    And I login as "user2" with password "password2"
    And The user "user2" joins the group as "AUTHORIZED"
    And I login as "user" with password "password"
    When I update the user "user2" role of the group to "ADMIN"
    Then The response code is 403

  Scenario: Modify own admin role to authorized
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "ADMIN"
    When I update the user "user" role of the group to "AUTHORIZED"
    Then The response code is 403

  Scenario: Modify own admin role to subscribed
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "ADMIN"
    When I update the user "user" role of the group to "SUBSCRIBED"
    Then The response code is 403

  Scenario: Modify own authorized role to subscribed
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "AUTHORIZED"
    When I update the user "user" role of the group to "SUBSCRIBED"
    Then The response code is 403

  Scenario: Modify own subscribed role to authorized
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "SUBSCRIBED"
    When I update the user "user" role of the group to "AUTHORIZED"
    Then The response code is 403

  Scenario: Modify own subscribed role to admin
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "SUBSCRIBED"
    When I update the user "user" role of the group to "ADMIN"
    Then The response code is 403

  Scenario: Modify own authorized role to admin
    Given There is a registered user with username "user" and password "password" and email "user@sample.app"
    And I login as "user" with password "password"
    And A group exists
    And The user "user" joins the group as "AUTHORIZED"
    When I update the user "user" role of the group to "ADMIN"
    Then The response code is 403
