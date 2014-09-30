@proc
Feature: Check the Process steps

  @linux
  Scenario: Simple Process

    Given a new temporary directory kept in variable "workingDir"
    And the current working directory has been set to "${workingDir}"
    When I run `touch filemode.file`
    And I run `ls -al`
    Then once finished, the last command output should satisfy:
    """
    \Q-rw-r--r--\E (.+) filemode.file
    """
