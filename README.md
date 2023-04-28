Hetzner Cloud API for Java
==========================

![Test Action Status](https://github.com/TomSDEVSN/hetznercloud-java/actions/workflows/test.yml/badge.svg)
![Build Action Status](https://github.com/TomSDEVSN/hetznercloud-java/actions/workflows/build.yml/badge.svg)

Simple Java client for the Hetzner Cloud API.

**Important message about this project [here](https://github.com/TomSDEVSN/hetznercloud-java/discussions/45)**

## Compile

This project uses Maven as build automation.

Just run ``mvn clean install`` to install it in the local Maven repository cache.

## How to use

##### Maven

Dependency:

```xml
<dependencies>
    <dependency>
        <groupId>me.tomsdevsn</groupId>
        <artifactId>hetznercloud-api</artifactId>
        <version>3.1.0</version>
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
    implementation "me.tomsdevsn:hetznercloud-api:3.1.0"
}
```

<details>
  <summary>kotlin-dsl</summary>

```kotlin
dependencies {
    implementation("me.tomsdevsn:hetznercloud-api:3.1.0")
}
```
</details>

## How to run tests
The tests need an API Token for the Hetzner Cloud. The API token has to be set as an environment variable called `HCLOUD_TOKEN`.

To obtain an API key, please see [the official API documentation](https://docs.hetzner.cloud/#getting-started).

```
HCLOUD_TOKEN="${api_key}" mvn test
```

## JavaDocs

The JavaDocs are available [here](https://docs.hcloud.siewert.io)

## Dependencies

The following dependencies were used in this project:
* [jackson-databind](https://github.com/FasterXML/jackson-databind) under Apache2.0 License
* [Lombok](https://projectlombok.org) under MIT License
