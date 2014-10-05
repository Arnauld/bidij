# Behavior Driven Infrastructure for java

## What?

Predefined steps for Java/Cucumber

* HTTP
* JDBC
* SSH
* Process

## Maven

```xml
<dependency>
  <groupId>org.technbolts</groupId>
  <artifactId>bidij</artifactId>
  <version>0.2.1</version>
</dependency>
```


### Http example

```cucumber
Scenario: Server deployed and configured with https

    Given an host set to "https://12.4.0.19:8083"
     When a GET request is made to "/auth/users"
     Then the response status code should be 401 (Unauthorized)
```

```cucumber
Scenario: GET on https with basic auth specifying credentials

    Given a sample server running on port 8080 and on secure port 8083
    Given an host set to "https://localhost:8083"
    And basic auth credentials set to "carmen" and "mccallum"
    When a GET request is made to "/auth/users" with the following parameters:
      | parameter name | parameter value        |
      | offset         | 50                     |
      | limit          | 10                     |
      | filter         | { :name "/john (.*)/"} |
    Then the response status code should be 200 (OK)
```

Runner

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

### Jdbc Examples

```cucumber
Scenario: Jdbc Configuration

    Given the following jdbc configurations:
      | configuration name | driver        | url                               | username | password |
      | server_mode        | org.h2.Driver | jdbc:h2:tcp://localhost/~/test    | pif      | pifp     |
      | in_memory          | org.h2.Driver | jdbc:h2:mem:test                  | sa       | sa       |
      | file               | org.h2.Driver | jdbc:h2:${workingDir}/db_${idgen} | sa       | sa       |
    And a sample database running using configuration "file"

    Given the "file" jdbc configuration has been applied
    When a query is made on table "user"
    Then the number of rows returned should be greater than 0
```

```java
@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"myapplication.steps",
                "bdi.glue.jdbc.common"},
        format = "tzatziki.analysis.exec.gson.JsonEmitterReport:target/jdbc")
public class JdbcFeatures {
}
```

### More examples

Look at the [generated pdf](doc/features.pdf)

### Worlds and IOC

**bidij** make an heavy use of the cucumber's `world` concept.
Each universe (http, jdbc, proc, ssh, ...) has its own `World` class: `bdi.glue.jdbc.common.JdbcWorld`, 
`bdi.glue.http.common.HttpWorld`, ...

**This allow to share and to customize settings by user defined steps.**

This is easily and automatically accomplished through pico-container:

```xml
 <dependency>
     <groupId>info.cukes</groupId>
     <artifactId>cucumber-picocontainer</artifactId>
     <version>${cucumber.version}</version>
 </dependency>
```


e.g. to customize default HTTP basic auth from a custom properties file.

```java

public class MyApplicationStepdefs {

    private final HttpWorld httpWorld;
    private TestSettings testSettings;
    
    public MyApplicationStepdefs(HttpWorld httpWorld) {
        this.httpWorld = httpWorld;
    }
    
    @Before
    public void loadSettings () {
        testSettings = TestSettings.load();
    }

    @Given("^default basic auth credentials set$")
    public void default_basic_auth_credentials_set() throws Throwable {
        httpWorld.currentRequestBuilder()
                .basicAuthCredentials(
                        testSettings.getProperty("username"),
                        testSettings.getProperty("password"));
    }
}
```

# Inspirations

* [Yes Mum, I'll Behave: Beginning Behaviour Driven Infrastructure](http://kartar.net/2009/12/yes-mum-ill-behave-beginning-behaviour-driven-infrastructure/)
* [BEHAVIOUR DRIVEN INFRASTRUCTURE THROUGH CUCUMBER](http://fractio.nl/2009/11/09/behaviour-driven-infrastructure-through-cucumber/)
* [cucumber-nagios](http://auxesis.github.io/cucumber-nagios/)


# Release

* [Maven Release Plugin: The Final Nail in the Coffin](http://axelfontaine.com/blog/final-nail.html)