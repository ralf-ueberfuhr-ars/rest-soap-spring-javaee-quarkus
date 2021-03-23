# Java EE Sample for REST and SOAP

Dieses Sample enthält einen REST- und einen SOAP-Service mit Java EE. Desweiteren auch Microprofile-Funktionalitäten. Laufzeitumgebung ist OpenLiberty.

## Starten in der Entwicklungsumgebung

Einmalig bauen, um z.B. die Liberty-Runtime zu konfigurieren:
```bash
mvn clean package
```

Starten im Developer-Modus:
```bash
mvn io.openliberty.tools:liberty-maven-plugin:devc
```

## Aufruf der Anwendung

## REST-Service

Auslesen aller Todos:
```
http://host.docker.internal:9080/todos-app/api/v1/todos
```

Anzeige der REST-Schnittstelle (OpenAPI):
```
http://host.docker.internal:9080/openapi/
```

Testen mit der Swagger UI:
```
http://host.docker.internal:9080/openapi/ui/
```

## SOAP Service

Direktaufruf: (nur per POST mit SOAP-Nachricht)
```
http://host.docker.internal:9080/todos-app/TodosService
```

WSDL:
```
http://host.docker.internal:9080/todos-app/TodosService?wsdl
```

## Health Checks

```
http://host.docker.internal:9080/health/
```