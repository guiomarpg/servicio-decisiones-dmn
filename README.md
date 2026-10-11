# Servicio de decisiones DMN

Entorno para publicar vuestros modelos DMN como **servicios de decisión** y llamarlos vía API (REST/JSON), sin instalar nada en vuestro ordenador. Usa el motor abierto **Apache KIE**, el mismo que hay debajo de Aletyx Playground: el modelo que diseñáis allí funciona aquí tal cual.

*Sistemas de Ayuda a la Decisión en la Automatización de Procesos · UNIR*

---

## Qué necesitáis

- Un navegador (Chrome, Edge o Firefox).
- Una cuenta gratuita de GitHub: <https://github.com/signup>.

GitHub da a cada cuenta gratuita una cuota mensual de horas de Codespaces; con parar el entorno al terminar (paso 6) os sobra para toda la asignatura.

---

## Paso a paso

### 1. Cread vuestra copia del entorno

En la página de este repositorio pulsad **Use this template → Open in a codespace**.

Se crea vuestra propia copia y se abre en el navegador un editor parecido a Visual Studio Code.

### 2. Esperad a que se prepare

La **primera vez** tarda unos 2–3 minutos: instala Java, descarga el motor y compila el ejemplo. Veréis el progreso en el terminal de la parte inferior. Las siguientes veces se abre en segundos.

Cuando el servicio está listo, el terminal muestra una línea como esta:

```
Listening on: http://0.0.0.0:8080
```

### 3. Abrid Swagger UI

Al arrancar aparece un aviso abajo a la derecha: **«Your application running on port 8080 is available»**. Pulsad **Open in Browser**. Se abre una pestaña nueva con **Swagger UI**, la página que documenta y permite probar vuestros servicios.

Si el aviso no aparece o lo cerrasteis: pestaña **PORTS** (Puertos) del panel inferior → puerto **8080** → icono del globo (*Open in Browser*).

![Swagger UI con el modelo de ejemplo](docs/img/01_swagger_inicio.png)

Por cada modelo veréis tres operaciones:

| Operación | Para qué sirve |
|---|---|
| `POST /<modelo>` | Evalúa el modelo y devuelve las entradas y todas las decisiones |
| `POST /<modelo>/dmnresult` | Igual, en detalle: estado de cada decisión y mensajes del motor |
| `GET /<modelo>` | Devuelve el propio fichero DMN |

Si el modelo tiene **servicios de decisión**, aparece además una operación por servicio: `POST /<modelo>/<servicio>`.

### 4. Llamad al servicio

1. Desplegad la operación `POST /T3_FRIOCAMPO_Entrega`.
2. Pulsad **Try it out**.
3. En *Request body* pegad el contenido de un fichero de la carpeta `peticiones` (por ejemplo, `friocampo_granada.json`).
4. Pulsad **Execute**.

![Petición en Swagger UI](docs/img/02_try_it_out.png)

Debajo aparece la respuesta: el **código** (200 si todo ha ido bien) y el **cuerpo** JSON con el resultado de cada decisión. Swagger también muestra la orden `curl` equivalente, por si queréis repetir la llamada desde un terminal.

![Respuesta del servicio](docs/img/03_respuesta.png)

### 5. Publicad vuestro propio modelo

1. En Aletyx Playground, descargad vuestro modelo (`.dmn`).
2. En el codespace, arrastrad el fichero a la carpeta **`modelos`** del explorador de la izquierda.
3. Recargad la pestaña de Swagger UI: en unos segundos aparece vuestro modelo con sus operaciones.

Si cambiáis el modelo, basta con sustituir el fichero y volver a llamar al servicio: el motor lo recarga solo.

**Si el modelo tiene un error**, Swagger UI no carga y el navegador muestra una página de error con el mensaje del motor (por ejemplo, *«Decision node 'D' is missing the variable declaration»*). Corregid el modelo en Aletyx, sustituid el fichero y recargad.

> La ruta de cada servicio es el **nombre del modelo** (el que aparece en las propiedades del DMN en Aletyx), no el nombre del fichero.

### 6. Parad el entorno al terminar

Id a <https://github.com/codespaces>, pulsad los tres puntos junto a vuestro codespace y elegid **Stop codespace**. Si se os olvida, se para solo tras 30 minutos sin uso. Vuestros ficheros se conservan; para volver, abrid el codespace desde esa misma página.

Para guardar los modelos en vuestro repositorio de GitHub: icono de **Source Control** (barra izquierda) → escribid un mensaje → **Commit** → **Sync**.

---

## Preguntas frecuentes

**La respuesta da código 500.** El servicio ha recibido la petición, pero alguna decisión ha fallado. Repetid la llamada con la operación `/dmnresult`: el campo `messages` dice qué decisión falló y por qué, y `decisionResults` muestra el estado de cada una (`SUCCEEDED`, `FAILED` o `SKIPPED`).

**La respuesta da código 400.** El JSON está mal formado (falta una llave, una coma o unas comillas). Revisadlo o pegad de nuevo un ejemplo de `peticiones`.

**¿Puede llamar a mi servicio otra persona o un proceso externo?** Por defecto el servicio solo es accesible para vosotros. Si una actividad lo pide: pestaña **PORTS** → clic derecho en el puerto 8080 → **Port Visibility → Public**. Volved a ponerlo en privado al terminar.

**Prefiero trabajar en mi ordenador.** Seguid la guía [GUIA_WINDOWS.md](GUIA_WINDOWS.md).

---

## Contenido del repositorio

| Carpeta o fichero | Qué contiene |
|---|---|
| `modelos/` | Los modelos DMN que se publican (atajo a `src/main/resources/modelos`) |
| `peticiones/` | Peticiones JSON de ejemplo para el modelo de FRIOCAMPO |
| `arrancar.cmd` / `arrancar.sh` | Arranque en un ordenador propio (Windows / Mac o Linux) |
| `pom.xml`, `mvnw`, `.devcontainer/` | Configuración del entorno; no hace falta tocarlos |
