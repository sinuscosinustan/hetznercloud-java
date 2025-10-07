Hetzner Cloud API for Java
==========================

![Test Action Status](https://github.com/sinuscosinustan/hetznercloud-java/actions/workflows/test.yml/badge.svg)
![Build Action Status](https://github.com/sinuscosinustan/hetznercloud-java/actions/workflows/build.yml/badge.svg)

Simple Java client for the Hetzner Cloud API.

**Important message about this project [here](https://github.com/sinuscosinustan/hetznercloud-java/discussions/45)**

## Compile

This project uses Maven as build automation.

Just run ``mvn clean install`` to install it in the local Maven repository cache.

## How to use

##### Maven

Dependency:

```xml
<dependencies>
    <dependency>
        <groupId>io.github.sinuscosinustan</groupId>
        <artifactId>hetznercloud-api</artifactId>
        <version>4.0.1</version>
    </dependency>
</dependencies>
```

##### Gradle

Put this in the ``build.gradle`` file of the project:

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation "io.github.sinuscosinustan:hetznercloud-api:4.0.1"
}
```

<details>
  <summary>kotlin-dsl</summary>

```kotlin
dependencies {
    implementation("io.github.sinuscosinustan:hetznercloud-api:4.0.1")
}
```
</details>

## Requirements

- Java 17 or higher
- Maven 3.6.0 or higher

## How to run tests

This project has unit tests, as well as integration tests.

### Unit Tests (Default)
```bash
mvn test
```

### Integration Tests
Integration tests require an API Token for the Hetzner Cloud. Set the API token as an environment variable called `HCLOUD_TOKEN`.

To obtain an API key, please see [the official API documentation](https://docs.hetzner.cloud/#getting-started).

```bash
HCLOUD_TOKEN="${api_key}" mvn test -Pintegration-tests
```

### Code Quality

#### Checkstyle
Checkstyle runs automatically during compilation:
```bash
mvn compile
```

#### Code Coverage (JaCoCo)
Generate code coverage reports:
```bash
mvn test
```
View the HTML report at `target/site/jacoco/index.html`

## JavaDocs

The JavaDocs are available [here](https://sinuscosinustan.github.io/hetznercloud-java/)

## Dependencies

The following dependencies were used in this project:
* [jackson-databind](https://github.com/FasterXML/jackson-databind) under Apache2.0 License
* [Lombok](https://projectlombok.org) under MIT License
