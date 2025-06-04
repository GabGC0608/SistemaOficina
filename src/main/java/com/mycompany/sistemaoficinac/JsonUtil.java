package com.mycompany.sistemaoficinac;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.DeserializationFeature;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Classe utilitária para manipulação de arquivos JSON.
 * Utiliza a biblioteca Jackson para serialização e desserialização.
 */
public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * Salva um objeto em um arquivo JSON.
     *
     * @param objeto Objeto a ser serializado.
     * @param arquivo Caminho do arquivo JSON.
     * @throws IOException Se ocorrer erro de I/O.
     */
    public static void salvarParaJson(Object objeto, String arquivo) throws IOException {
        if (arquivo == null || arquivo.trim().isEmpty()) {
            throw new IllegalArgumentException("O caminho do arquivo não pode ser nulo ou vazio");
        }

        Path path = Paths.get(arquivo).toAbsolutePath();

        // Cria diretórios se não existirem (apenas se não for diretório raiz)
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }

        mapper.writeValue(path.toFile(), objeto);
    }

    /**
     * Carrega um objeto de um arquivo JSON.
     *
     * @param <T> Tipo do objeto a ser desserializado.
     * @param arquivo Caminho do arquivo JSON.
     * @param classe Classe do objeto a ser desserializado.
     * @return Objeto desserializado.
     * @throws IOException Se ocorrer erro de I/O ou o arquivo não existir.
     */
    public static <T> T carregarDeJson(String arquivo, Class<T> classe) throws IOException {
        File file = new File(arquivo);
        if (!file.exists()) {
            throw new IOException("Arquivo não encontrado: " + arquivo);
        }
        return mapper.readValue(file, classe);
    }

    /**
     * Carrega especificamente uma instância de Oficina de um arquivo JSON.
     *
     * @param arquivo Caminho do arquivo JSON.
     * @return Instância de Oficina desserializada.
     * @throws IOException Se ocorrer erro de I/O ou o arquivo não existir.
     */
    public static Oficina carregarOficina(String arquivo) throws IOException {
        if (arquivo == null || arquivo.trim().isEmpty()) {
            throw new IllegalArgumentException("O caminho do arquivo não pode ser nulo ou vazio");
        }

        Path path = Paths.get(arquivo).toAbsolutePath();
        if (!Files.exists(path)) {
            throw new IOException("Arquivo não encontrado: " + path);
        }

        return mapper.readValue(path.toFile(), Oficina.class);
    }

    /**
     * Converte um objeto para string JSON.
     *
     * @param objeto Objeto a ser serializado.
     * @return String JSON formatada.
     * @throws IOException Se ocorrer erro na serialização.
     */
    public static String paraJsonString(Object objeto) throws IOException {
        return mapper.writeValueAsString(objeto);
    }

    /**
     * Converte uma string JSON para objeto.
     *
     * @param <T> Tipo do objeto a ser desserializado.
     * @param json String JSON.
     * @param classe Classe do objeto.
     * @return Objeto desserializado.
     * @throws IOException Se ocorrer erro na desserialização.
     */
    public static <T> T deJsonString(String json, Class<T> classe) throws IOException {
        return mapper.readValue(json, classe);
    }
}