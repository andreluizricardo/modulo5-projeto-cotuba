package br.com.unipds.cotuba.adapters.out.config;

import br.com.unipds.cotuba.dto.PropriedadesEbook;
import br.com.unipds.cotuba.ports.out.LeitorPropriedadesEbook;
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
    public PropriedadesEbook ler(Path diretorioMD) {

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

        return new PropriedadesEbook(titulo, autor);
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
