Feature: fooforge.com ssh logins
  As a user of fooforge.com
  I need to login remotely

  @Ignore
  Scenario: Basic ssh

    Given an interactive ssh session opened on "127.0.0.1:2222" with the following credentials:
      | username | password |
      | vagrant  | foobar   |


  Scenario: SSH authentication with private key

    Given a default vm popped
    # e.g. "~/.ssh/id_dsa"
    Given a ssh private key at "~/.vagrant.d/insecure_private_key" with no passphrase
    And an interactive ssh session opened on "127.0.0.1:2222" with the following credentials:
      | username |
      | vagrant  |
    When through ssh, I run `ls -al`
    Then within 5 seconds, the ssh session output should contain ".vbox_version"