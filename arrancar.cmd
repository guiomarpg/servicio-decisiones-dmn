@echo off
REM Arranca el servicio de decisiones en http://localhost:8080 (Swagger UI)
REM Para pararlo: cerrad esta ventana o pulsad Ctrl+C.
cd /d "%~dp0"
call mvnw.cmd -B quarkus:dev -Dquarkus.console.enabled=false
pause
