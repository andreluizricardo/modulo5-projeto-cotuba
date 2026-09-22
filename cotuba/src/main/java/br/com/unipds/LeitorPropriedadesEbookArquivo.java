package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

@ApplicationScoped
public class LeitorPropriedadesEbookArquivo implements LeitorPropriedadesEbook {
    @Override
    public void ler(Path diretorioMD, Ebook ebook) {

        Path arquivoProperties = diretorioMD.resolve("ebook.properties");

        if(!Files.exists(arquivoProperties)) {
            throw new IllegalStateException("Arquivo ebook.properties não  encontrado em " +diretorioMD);
        }

        Properties properties = new Properties();
        try(Reader reader = Files.newBufferedReader(arquivoProperties, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch(IOException ex) {
            throw new IllegalStateException("Erro ao ler arquivo " + arquivoProperties, ex);
        }

        String proprieadeTitulo = properties.getProperty("cotuba.ebook.titulo");
        String titulo = proprieadeTitulo;
        validarPropriedade(titulo, proprieadeTitulo);

        String proprieadeAutor = properties.getProperty("cotuba.ebook.autor");
        String autor = proprieadeAutor;
        validarPropriedade(autor, proprieadeAutor);

        ebook.setTitulo(titulo);
        ebook.setAutor(autor);
    }

    private void validarPropriedade(String valor, String proprieade) {
        if(valor == null || proprieade.isBlank()) {
            throw new IllegalStateException(("Propriedade inválida: " + proprieade));
        }
    }

    private static void extracted(String titulo) {
        if(titulo == null || titulo.isBlank()) {

        }
    }
}
