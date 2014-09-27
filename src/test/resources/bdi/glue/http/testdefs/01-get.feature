@http
Feature: Check the HTTP steps

  Scenario: Simple GET using host

    Given a sample server running on port 8080
    Given an host set to "http://localhost:8080"
    When a GET request is made to "/users"
    Then the response status code should be 200

  Scenario: Simple GET using complete uri

    Given a sample server running on port 8080
    When a GET request is made to "http://localhost:8080/users"
    Then the response status code should be 200

