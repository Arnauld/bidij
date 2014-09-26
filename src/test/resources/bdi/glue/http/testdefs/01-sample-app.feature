Feature: Check the HTTP steps

  In order to ease the testing of HTTP server
  As an automation tester
  I want some predefined steps to write and to check HTTP request and HTTP response.

  Scenario: Simple GET using host

    Given a sample server running on port 8080
    Given an host set to "http://localhost:8080"
    When a GET request is made to "/users"
    Then the response status code should be 200

  Scenario: Simple GET using complete uri

    Given a sample server running on port 8080
    When a GET request is made to "http://localhost:8080/users"
    Then the response status code should be 200

  Scenario: GET on app with https configuration set

    Given a sample server running on port 8080 and on secure port 8083
    Given an host set to "http://localhost:8080"
    When a GET request is made to "/users"
    Then the response status code should be 200

  @secure
  @secure__allow_all_hostname
  Scenario: GET on https

    Given a sample server running on port 8080 and on secure port 8083
    And an host set to "https://localhost:8083"
    When a GET request is made to "/users"
    Then the response status code should be 200

  @secure
  @secure__allow_all_hostname
  Scenario: GET on https with basic auth no credential provided

    Given a sample server running on port 8080 and on secure port 8083
    And an host set to "https://localhost:8083"
    When a GET request is made to "/auth/users"
    Then the response status code should be 401 (Unauthorized)

  @secure
  @secure__allow_all_hostname
  Scenario: GET on https with basic auth using default credentials

    Given a sample server running on port 8080 and on secure port 8083
    Given an host set to "https://localhost:8083"
    And default basic auth credentials set
    When a GET request is made to "/auth/users"
    Then the response status code should be 200 (OK)

  @secure
  @secure__allow_all_hostname
  Scenario: GET on https with basic auth specifying credentials

    Given a sample server running on port 8080 and on secure port 8083
    Given an host set to "https://localhost:8083"
    And basic auth credentials set to "carmen" and "mccallum"
    When a GET request is made to "/auth/users"
    Then the response status code should be 200 (OK)

  @secure
  @secure__allow_all_hostname
  Scenario: GET on https with basic auth specifying credentials

    Given a sample server running on port 8080 and on secure port 8083
    Given an host set to "https://localhost:8083"
    And basic auth credentials set to "carmen" and "mccallum"
    When a GET request is made to "/auth/users" with the following parameters:
      | parameter name | parameter value        |
      | offset         | 50                     |
      | limit          | 10                     |
      | filter         | { :name "/john (.*)/"} |
    Then the response status code should be 200 (OK)

  Scenario: GET with json content negotiation

    Given a sample server running on port 8080
    Given an host set to "http://localhost:8080"
    And a content format negotiation set to "application/json"
    When a GET request is made to "/about"
    Then the response status code should be 200 (OK)
    And the json response's body should contain:
    """
       { "version" : "1.2.0" }
    """

  Scenario: GET with html content negocation

    Given a sample server running on port 8080
    Given an host set to "http://localhost:8080"
    And a content format negotiation set to "text/html"
    When a GET request is made to "/about"
    Then the response status code should be 200 (OK)
    And the response's body should start with "<html"

  Scenario: A POST with json response

    Given a sample server running on port 8080
    And an host set to "http://localhost:8080"
    When a POST request is made to "/users" with content type "application/json" with:
    """
    {
      "firstname":"Carmen",
      "lastname":"Mccallum"
    }
    """
    Then the response status code should be 201
    And the json response's body should contain:
    """
    {
      "status":"ok"
    }
    """
