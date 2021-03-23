# Quarkus Sample for REST

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```bash
mvn compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```bash
mvn clean package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

If you want to build an _uber-jar_, execute the following command:
```shell script
mvn clean package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can create a native executable using: 
```bash
mvn package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```bash
mvn package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/getting-started-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

## OpenApi

`http://localhost:8080/openapi`
`http://localhost:8080/swagger-ui`

## Health Checks

`http://localhost:8080/q/health`
`http://localhost:8080/q/health/live`
`http://localhost:8080/q/health/ready`

## Provided examples

### RESTEasy JAX-RS example

REST is easy peasy with this Hello World RESTEasy resource.

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
