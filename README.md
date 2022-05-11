Hetzner Cloud API for Java
==========================

![Test Action Status](https://github.com/TomSDEVSN/hetznercloud-java/actions/workflows/test.yml/badge.svg)
![Build Action Status](https://github.com/TomSDEVSN/hetznercloud-java/actions/workflows/build.yml/badge.svg)

Simple Java client for the Hetzner Cloud API.

**Important message about this project [here](https://github.com/TomSDEVSN/hetznercloud-java/discussions/45)**

## Compile

You can simply compile it with Maven.

Just run ``mvn clean install`` to install it in your local Maven-repository.

## How to use

##### Maven

Dependency:

```xml
<dependencies>
    <dependency>
        <groupId>me.tomsdevsn</groupId>
        <artifactId>hetznercloud-api</artifactId>
    </dependency>
</dependencies>
```

##### Gradle 

You have to edit the ``build.gradle``

```
repositories({
    mavenCentral()
})

dependencies({
    implementation "me.tomsdevsn:hetznercloud-api"
})
```

## How to run tests
The tests need an API Token for the Hetzner Cloud. The API token has to be set as an environment variable called `HCLOUD_TOKEN`.

To obtain an API key, please see [the official API documentation](https://docs.hetzner.cloud/#getting-started).

```
HCLOUD_TOKEN="" mvn test
```

## JavaDocs

The JavaDocs are available [here](https://docs.hcloud.siewert.io)

## Dependencies

The following dependencies were used in this project:
 * [jackson-databind](https://github.com/FasterXML/jackson-databind) under Apache2.0 License
 * [spring-web](https://github.com/spring-projects/spring-framework/tree/master/spring-web) under Apache 2.0 License
 * [Lombok](https://projectlombok.org) under MIT License
