# LinkApp

Template for a Spring Boot project including Spring REST, HATEOAS, JPA, etc. Additional details: [HELP.md](HELP.md)

[![Open Issues](https://img.shields.io/github/issues-raw/UdL-EPS-SoftArch/LinkApp-API?logo=github)](https://github.com/orgs/UdL-EPS-SoftArch/projects/14)
[![CI/CD](https://github.com/UdL-EPS-SoftArch/LinkApp-API/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/UdL-EPS-SoftArch/LinkApp-API/actions)
[![CucumberReports: UdL-EPS-SoftArch](https://messages.cucumber.io/api/report-collections/faed8ca5-e474-4a1a-a72a-b8e2a2cd69f0/badge)](https://reports.cucumber.io/report-collections/faed8ca5-e474-4a1a-a72a-b8e2a2cd69f0)
[![Heroku App Status](https://heroku-shields.herokuapp.com/linkapp-api)](https://linkapp-api.herokuapp.com)

## Vision

**For** outgoing people **who** want to meet new people and want to spend time hanging out with others
**the project** LinkApp **is an** iniciative from an University Degree
**that** allows people to put in contact with each other and arrenge meetings
**Unlike** other meeting applications, LinkApp is a new vision of the concept of hanging out without paying either advertising.

## Features per Stakeholder

| USER                     | SUBSCRIBED (+USER)          | AUTHORIZED (+SUBSCRIBED)      | ADMIN GROUP (+AUTHORIZED)         |
|--------------------------|-----------------------------|-------------------------------|-----------------------------------|
| Register                 | Attend a meeting            | Create a meeting              | Modify roles of a user in a Group |
| Login                    | Unsubscribe (leave) a group | Modify the themes of a Group  | Delete anyone's post              |
| Logout                   | Reply to a post             | Create a new post             |                                   |
| Create a group           | Delete a post               | Create a new Meet             |                                   |
| Send a message to a Meet |                             |                               |                                   |

## Entities Model

![EntityModelsDiagram](http://www.plantuml.com/plantuml/svg/5SqnhW8X40RW_ftYUG2OtcefjZ1Paqqs7W1X528CPFY9rrUhh_oM0Q-OjVoTieGo8qyj_mdeuqoa_csV6MdUvs0DJS026rgbMzpCkX_cQ0yu3OcsB2_Nkt7xXQeVOALLa95vN5laOlilMLZYrmy0?v3)

