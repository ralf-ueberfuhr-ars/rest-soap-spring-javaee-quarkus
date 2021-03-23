# Spring Boot Sample for REST

Dieses Sample enthält einen REST-Service mit Spring Boot. Desweiteren auch Microprofile-Funktionalitäten. Laufzeitumgebung ist ein mit der Anwendung verpackter Tomcat.

## Neues Projekt anlegen

https://start.spring.io/

## Starten in der Entwicklungsumgebung

Starten im Developer-Modus:
```bash
mvn spring-boot:run
```
oder aus der IDE die Main-Methode der Klasse `de.ars.schulung.samples.services.todo.TodosApplication`

oder bauen und starten mit 
```bash
mvn clean package
java -jar todos-service.jar
```

## Aufruf der Anwendung

## REST-Service

Auslesen aller Todos:
```
http://localhost:8080/api/v1/todos
```

Anzeige der REST-Schnittstelle (OpenAPI):
```
http://localhost:8080/v2/api-docs
```

Testen mit der Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

## Health Checks

Anzeige aller verfügbaren Links über den Actuator:

```
http://localhost:8080/actuator
```

```
http://localhost:8080/actuator/health
```