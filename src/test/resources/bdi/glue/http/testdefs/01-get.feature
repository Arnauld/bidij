@http
Feature: Check the HTTP steps

  Background:
    Given a sample server running on port 8080


  Scenario: Simple GET using host

    Given an host set to "http://localhost:8080"
    When a GET request is made to "/users"
    Then the response status code should be 200

  Scenario: Simple GET using complete uri

    When a GET request is made to "http://localhost:8080/users"
    Then the response status code should be 200

  Scenario Outline: Supported Verb: <verb>

    When a <verb> request is made to "http://localhost:8080/users"
    Then the response status code should be <status>

  Examples:
    | verb    | status |
    | GET     | 200    |
    # Method not supported by application
    | PUT     | 405    |
    # Bad request: no payload
    | POST    | 400    |
    #Method not supported by application
    | DELETE  | 405    |
    | HEAD    | 200    |
    | OPTIONS | 200    |
    | TRACE   | 200    |
    # Patch method is not defined in RFC 2068, and thus supported by servlet
    | PATCH   | 501    |

