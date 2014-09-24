Feature: google.com
  It should be up
  And I should be able to search for things

  @browser
  Scenario: Searching for things
    When I go to "http://www.google.com.au/"
    And I fill in "q" with "wikipedia"
    And I press "Google Search"
    Then I should see "www.wikipedia.org"