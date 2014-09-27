@http
Feature: Headers and Content Negociation

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
