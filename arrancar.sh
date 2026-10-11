#!/bin/sh
# Arranca el servicio de decisiones en http://localhost:8080 (Swagger UI)
cd "$(dirname "$0")"
./mvnw -B quarkus:dev -Dquarkus.console.enabled=false
