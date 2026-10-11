package unir.decisiones;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Devuelve las baterías de escenarios de la carpeta "escenarios" para que la página
 * pruebas.html las ejecute contra el servicio. Los ficheros se leen en cada llamada:
 * se pueden añadir o cambiar sin reiniciar el servicio.
 */
@jakarta.ws.rs.Path("/pruebas/baterias")
public class BateriasResource {

    @Inject
    ObjectMapper mapper;

    @ConfigProperty(name = "pruebas.carpeta", defaultValue = "escenarios")
    String carpeta;

    @GET
    @org.eclipse.microprofile.openapi.annotations.Operation(hidden = true)
    @Produces(MediaType.APPLICATION_JSON)
    public List<JsonNode> listar() throws IOException {
        List<JsonNode> baterias = new ArrayList<>();
        java.nio.file.Path dir = Paths.get(carpeta).toAbsolutePath();
        if (!Files.isDirectory(dir)) {
            return baterias;
        }
        List<java.nio.file.Path> ficheros = new ArrayList<>();
        try (DirectoryStream<java.nio.file.Path> ds = Files.newDirectoryStream(dir, "*.json")) {
            ds.forEach(ficheros::add);
        }
        ficheros.sort(null);
        for (java.nio.file.Path f : ficheros) {
            ObjectNode n;
            try {
                n = (ObjectNode) mapper.readTree(f.toFile());
            } catch (Exception ex) {
                n = mapper.createObjectNode();
                n.put("error", "No se puede leer el fichero: " + ex.getMessage());
            }
            n.put("fichero", f.getFileName().toString());
            baterias.add(n);
        }
        return baterias;
    }
}
