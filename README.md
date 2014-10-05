# Behavior Driven Infrastructure for java

## What?

Predefined steps for Java/Cucumber

* HTTP
* JDBC
* Process
* SSH


### Http example

```cucumber
Scenario: Server deployed and configured with https

    Given an host set to "https://12.4.0.19:8083"
     When a GET request is made to "/auth/users"
     Then the response status code should be 401 (Unauthorized)
```

```java
@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"myapplication.steps",   
                "bdi.glue.http.common",       // predefined http steps
                "bdi.glue.http.httpclient"},  // httpclient implementation
        format = "tzatziki.analysis.exec.gson.JsonEmitterReport:target/http")
public class RunFeatures {
}
```

### Ssh Example

```cucumber
Scenario: SSH authentication with private key

    # e.g. "~/.ssh/id_dsa"
    Given a ssh private key at "~/.vagrant.d/insecure_private_key" with no passphrase
    And an interactive ssh session opened on "127.0.0.1:2222" with the following credentials:
      | username |
      | vagrant  |
    When through ssh, I run `ls -al`
    Then within 5 seconds, the ssh session output should contain ".vbox_version"
```

```java
@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"myapplication.steps",
                "bdi.glue.ssh.common",   // predefined ssh steps
                "bdi.glue.env"},
        format = "tzatziki.analysis.exec.gson.JsonEmitterReport:target/ssh")
public class RunFeatures {
}
```

### More examples

Look at the [generated pdf](doc/features.pdf)

# Inspirations

* [Yes Mum, I'll Behave: Beginning Behaviour Driven Infrastructure](http://kartar.net/2009/12/yes-mum-ill-behave-beginning-behaviour-driven-infrastructure/)
* [BEHAVIOUR DRIVEN INFRASTRUCTURE THROUGH CUCUMBER](http://fractio.nl/2009/11/09/behaviour-driven-infrastructure-through-cucumber/)
* [cucumber-nagios](http://auxesis.github.io/cucumber-nagios/)


# Release

* [Maven Release Plugin: The Final Nail in the Coffin](http://axelfontaine.com/blog/final-nail.html)