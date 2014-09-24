Feature: Check the HTTP steps

  In order to ease the testing of HTTP server
  As an automation tester
  I want some predefined steps to write and to check HTTP request and HTTP response.

  Scenario: Simple GET

    Given a sample server running on port 8080
    And a "Host" header set to "localhost:8080"
    When a GET request is made to "/users"
    Then the response status code should be 200


  Scenario: A POST with json response

    Given a sample server running on port 8080
    And a "Host" header set to "localhost:8080"
    When a POST request is made to "/users" with content type "application/json" with:
    """
    {
      "firstname":"Carmen",
      "lastname":"Mccallum"
    }
    """
    Then the response status code should be 200
    And the json response's body should contain:
    """
    {
      "status":"ok"
    }
    """
