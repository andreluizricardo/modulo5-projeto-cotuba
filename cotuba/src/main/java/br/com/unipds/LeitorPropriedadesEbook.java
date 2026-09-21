package br.com.unipds;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.stream.IntStream;

public class LeitorPropriedadesEbook {
    public void ler(Path diretorioMD, Ebook ebook) {

        Path arquivoProperties = diretorioMD.resolve("ebook.properties");

        if(!Files.exists(arquivoProperties)) {
            throw new IllegalStateException("Arquivo ebook.properties não  encontrado em " +diretorioMD);
        }

        Properties properties = new Properties();
        try(InputStream in = Files.newInputStream(arquivoProperties)) {
            properties.load(in);
        } catch(IOException ex) {
            throw new IllegalStateException("Erro ao ler arquivo " + arquivoProperties);
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
