Feature: fooforge.com ssh logins
  As a user of fooforge.com
  I need to login remotely

  @Ignore
  Scenario: Basic ssh

    Given an interactive ssh session opened on "127.0.0.1:2222" with the following credentials:
      | username | password |
      | vagrant  | foobar   |


  Scenario: SSH auth. with private key

    Given a default vm popped
    # e.g. "~/.ssh/id_dsa"
    Given a ssh private key at "~/.vagrant.d/insecure_private_key" with no passphrase
    And an interactive ssh session opened on "127.0.0.1:2222" with the following credentials:
      | username |
      | vagrant  |