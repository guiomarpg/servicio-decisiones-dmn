package unir.decisiones;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Devuelve las simulaciones de la carpeta "simulaciones" para que la página pruebas.html
 * las ejecute. Si "casos" es el nombre de un fichero CSV, se lee de la misma carpeta y se
 * convierte en una lista de peticiones: los números y los valores true/false se convierten
 * a su tipo; las celdas vacías no se envían.
 */
@jakarta.ws.rs.Path("/pruebas/simulaciones")
public class SimulacionesResource {

    private static final Pattern NUMERO = Pattern.compile("-?\\d+([.,]\\d+)?");

    @Inject
    ObjectMapper mapper;

    @ConfigProperty(name = "simulaciones.carpeta", defaultValue = "simulaciones")
    String carpeta;

    @GET
    @org.eclipse.microprofile.openapi.annotations.Operation(hidden = true)
    @Produces(MediaType.APPLICATION_JSON)
    public List<JsonNode> listar() throws IOException {
        List<JsonNode> sims = new ArrayList<>();
        java.nio.file.Path dir = Paths.get(carpeta).toAbsolutePath();
        if (!Files.isDirectory(dir)) {
            return sims;
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
                JsonNode casos = n.get("casos");
                if (casos != null && casos.isTextual()) {
                    Set<String> texto = new HashSet<>();
                    if (n.has("texto")) n.get("texto").forEach(t -> texto.add(t.asText()));
                    n.put("ficheroCasos", casos.asText());
                    n.set("casos", leerCsv(dir.resolve(casos.asText()), texto));
                }
            } catch (Exception ex) {
                n = mapper.createObjectNode();
                n.put("error", "No se puede leer el fichero: " + ex.getMessage());
            }
            n.put("fichero", f.getFileName().toString());
            sims.add(n);
        }
        return sims;
    }

    private ArrayNode leerCsv(java.nio.file.Path csv, Set<String> texto) throws IOException {
        List<String> lineas = Files.readAllLines(csv, StandardCharsets.UTF_8);
        ArrayNode casos = mapper.createArrayNode();
        if (lineas.isEmpty()) return casos;
        String cab = lineas.get(0).replace("﻿", "");
        String sep = cab.contains(";") ? ";" : ",";
        String[] campos = cab.split(sep, -1);
        for (int i = 1; i < lineas.size(); i++) {
            if (lineas.get(i).isBlank()) continue;
            String[] v = lineas.get(i).split(sep, -1);
            ObjectNode c = mapper.createObjectNode();
            for (int k = 0; k < campos.length && k < v.length; k++) {
                String campo = campos[k].trim(), s = v[k].trim();
                if (s.length() > 1 && s.startsWith("\"") && s.endsWith("\"")) s = s.substring(1, s.length() - 1);
                if (s.isEmpty()) continue;
                String b = s.toLowerCase();
                if (texto.contains(campo)) c.put(campo, s);
                else if (b.equals("true") || b.equals("verdadero")) c.put(campo, true);
                else if (b.equals("false") || b.equals("falso")) c.put(campo, false);
                else if (NUMERO.matcher(s).matches()) {
                    if (s.contains(".") || s.contains(",")) c.put(campo, Double.parseDouble(s.replace(',', '.')));
                    else c.put(campo, Long.parseLong(s));
                } else c.put(campo, s);
            }
            casos.add(c);
        }
        return casos;
    }
}
