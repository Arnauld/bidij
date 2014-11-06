@http
Feature: HTTPS steps

  Scenario: GET on app with https configuration set

    Given a sample server running on port 8080 and on secure port 8083
    Given an host set to "http://localhost:8080"
    When a GET request is made to "/users"
    Then the response status code should be 200

  @http_secure
  @http_secure__allow_all_hostname
  Scenario: GET on https

    Given a sample server running on port 8080 and on secure port 8083
    And an host set to "https://localhost:8083"
    When a GET request is made to "/users"
    Then the response status code should be 200

  @http_secure
  @http_secure__allow_all_hostname
  Scenario: GET on https with basic auth no credential provided

    Given a sample server running on port 8080 and on secure port 8083
    And an host set to "https://localhost:8083"
    When a GET request is made to "/auth/users"
    Then the response status code should be 401 (Unauthorized)

  @http_secure
  @http_secure__allow_all_hostname
  Scenario: GET on https with basic auth using default credentials

    Given a sample server running on port 8080 and on secure port 8083
    Given an host set to "https://localhost:8083"
    And default basic auth credentials set
    When a GET request is made to "/auth/users"
    Then the response status code should be 200 (OK)

  @http_secure
  @http_secure__allow_all_hostname
  Scenario: GET on https with basic auth specifying credentials

    Given a sample server running on port 8080 and on secure port 8083
    Given an host set to "https://localhost:8083"
    And basic auth credentials set to "carmen" and "mccallum"
    When a GET request is made to "/auth/users"
    Then the response status code should be 200 (OK)

  @http_secure
  @http_secure__allow_all_hostname
  Scenario: GET on https with basic auth specifying credentials and parameters

    Given a sample server running on port 8080 and on secure port 8083
    Given an host set to "https://localhost:8083"
    And basic auth credentials set to "carmen" and "mccallum"
    When a GET request is made to "/auth/users" with the following parameters:
      | parameter name | parameter value        |
      | offset         | 50                     |
      | limit          | 10                     |
      | filter         | { :name "/john (.*)/"} |
    Then the response status code should be 200 (OK)
