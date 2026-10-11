# Servicio de decisiones DMN en vuestro ordenador (Windows)

Alternativa a Codespaces para quien prefiera trabajar en local. El resultado es el mismo: Swagger UI en `http://localhost:8080` con vuestros modelos publicados como servicios.

## Una sola vez: instalar Java

1. Entrad en <https://adoptium.net/es/temurin/releases/> y descargad **Temurin 21 (LTS)** para Windows x64, en formato **.msi**.
2. Ejecutad el instalador. En la pantalla *Custom Setup*, desplegad **Set JAVA_HOME variable** y elegid **Will be installed on local hard drive**. El resto, por defecto.
3. Comprobadlo: abrid *Símbolo del sistema* y escribid `java -version`. Debe responder con una versión 21.

No hace falta instalar nada más: el entorno descarga por sí solo la herramienta de compilación (Maven) y el motor la primera vez.

## Descargar el entorno

En la página del repositorio: **Code → Download ZIP**. Descomprimid el ZIP en una carpeta **sin espacios ni tildes en la ruta**, por ejemplo `C:\UNIR\servicio-decisiones-dmn`.

## Arrancar el servicio

1. Haced doble clic en **`arrancar.cmd`**.
2. Se abre una ventana negra. La **primera vez** tarda unos minutos, porque descarga unos 150 MB; las siguientes, segundos.
3. Si Windows pregunta por el cortafuegos, pulsad **Permitir acceso**.
4. Cuando aparezca `Listening on: http://0.0.0.0:8080`, abrid en el navegador **<http://localhost:8080>**: se abre la página de inicio del servicio, con los accesos a Swagger UI y a Pruebas.

A partir de aquí, el uso es el mismo que en Codespaces (pasos 4 a 6 del [README](README.md)).

## Dónde van los modelos

En local, la carpeta de los modelos es:

```
src\main\resources\modelos
```

Copiad allí vuestro `.dmn` descargado de Aletyx y recargad Swagger UI.

## Parar el servicio

Cerrad la ventana negra (o pulsad `Ctrl + C` dentro de ella).

## Si algo falla

| Síntoma | Causa probable | Qué hacer |
|---|---|---|
| `JAVA_HOME not found` o `'java' no se reconoce` | Java no instalado o sin la opción JAVA_HOME | Repetid la instalación marcando *Set JAVA_HOME variable* y abrid una ventana nueva |
| Se queda parado descargando o da error de conexión | Red corporativa con proxy | Probad desde otra red, o usad Codespaces |
| `Port 8080 already in use` | Otro programa usa el puerto | Cerrad la otra ventana del servicio, o reiniciad el ordenador |
| La página del navegador muestra un error del motor | El modelo DMN tiene un error | Leed el mensaje, corregid en Aletyx y sustituid el fichero |
