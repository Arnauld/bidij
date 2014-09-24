Feature: Illustrate Http feature

  Scenario: GET method

    Given my application has no user yet
    When I send POST to localhost:8080/user with:
      {
        "firstname":"mccallum",
        "lastname":'carmen"
      }
    Then
