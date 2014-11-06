Feature: ssh access

  @Ignore
  Scenario: SSH authentication with private key from vm

    Given a default vm popped
    # e.g. "~/.ssh/id_dsa"
    Given a ssh private key at "~/.vagrant.d/insecure_private_key" with no passphrase
    And an interactive ssh session opened on "127.0.0.1:2222" with the following credentials:
      | username |
      | vagrant  |
    When through ssh, I run `ls -al`
    Then within 5 seconds, the ssh session output should contain ".vbox_version"

  Scenario: SSH authentication with private key

    Given a sample ssh server running on port 2222
    Given an interactive ssh session opened on "127.0.0.1:2222" with the following credentials:
      | username  | password |
      | whatever  | P4ss0rd  |
    When through ssh, I run `cd ${project.basedir} && ls -al`
    Then within 5 seconds, the ssh session output should contain "pom.xml"