@http
Feature: POST request

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
