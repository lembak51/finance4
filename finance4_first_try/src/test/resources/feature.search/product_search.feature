Feature: Search for the book


  Scenario: Search some book
    Given open home page
    And select english language
    When user searches book by "Control Your Mind and Master Your Feelings"
    When open book by "2018"
    And get author from API
    Then Compare author from api and ui




