Feature: Check the JDBC steps

  In order to ease the testing of database
  As an automation tester
  I want some predefined steps to write and to check database.

  Scenario: Simple Access

    Given a sample database running and defining "default" jdbc configuration
    Given the "default" jdbc configuration has been applied
    When a query is made on table "user"
    Then the number of rows returned should be greater than 0

  @wip
  Scenario: Jdbc Configuration

    Given the following jdbc configurations:
      | configuration name | driver        | url                                 | username | password |
      | server_mode        | org.h2.Driver | jdbc:h2:tcp://localhost/~/test      | pif      | pifp     |
      | in_memory          | org.h2.Driver | jdbc:h2:mem:test                    | sa       | sa       |
      | file               | org.h2.Driver | jdbc:h2:${workingDir}/db_${idgen} | sa       | sa       |
    And a sample database running using configuration "file"

    Given the "file" jdbc configuration has been applied
    When a query is made on table "user"
    Then the number of rows returned should be greater than 0
