package br.com.unipds.cotuba.adapters.out.html;

import br.com.unipds.cotuba.domain.Capitulo;
import br.com.unipds.cotuba.domain.Ebook;
import br.com.unipds.cotuba.domain.FormatoEbook;
import br.com.unipds.cotuba.support.FormatoEbookQualifier;
import br.com.unipds.cotuba.ports.out.GeradorEbook;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@FormatoEbookQualifier(FormatoEbook.HTML)
@ApplicationScoped
public class GeradorHTML implements GeradorEbook {
    @Override
    public void gerar(Ebook ebook, Path arquivoSaida) {
        try {
            Path diretorioHTML = Files.createDirectories(arquivoSaida);

            int i = 1;
            Map<Capitulo,Path> arquivoHTMLCapitulo = new LinkedHashMap<>();
            for(Capitulo capitulo : ebook.capitulos()) {
                String nomerAqruivoHTML = obterNomeArquivoHTML(i,capitulo);
                Path arquivoHTML = diretorioHTML.resolve(nomerAqruivoHTML);
                arquivoHTMLCapitulo.put(capitulo, arquivoHTML);
                escreveArquivoHTML(capitulo, arquivoHTML);
                i++;
            }
            escreveSumario(ebook, diretorioHTML, arquivoHTMLCapitulo);
        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao criar ebook HTML: " + arquivoSaida, ex);
        }

    }

    private void escreveArquivoHTML(Capitulo capitulo, Path arquivoHTML) throws IOException {
        String html = """
                <!DOCTYPE html>
                <html lang"pt-br">
                    <head>
                        <meta charset="UTF-8">
                        <title>%s</title>
                    </head>
                    <body>
                        %s
                        <a href="index.html">Voltar</a>
                    </body>
                </html>
                """.formatted(capitulo.titulo(), capitulo.html());
        Files.writeString(arquivoHTML, html, StandardCharsets.UTF_8);
    }

    private void escreveSumario(Ebook ebook, Path diretorioHTML, Map<Capitulo,Path> arquivoHTMLCapitulo) throws IOException {
        String linksSumarioHTML = ebook.capitulos().stream().map(capitulo ->
             """
                    <li>
                        <a href="%s">%s</a>
                    </li>
                    """.formatted(arquivoHTMLCapitulo.get(capitulo).getFileName(), capitulo.titulo())
        ).collect(Collectors.joining());
        String sumarioHTML = """
                <!DOCTYPE html>
                <html lang"pt-br">
                    <head>
                        <meta charset="UTF-8">
                        <title>%s</title>
                    </head>
                    <body>
                        <h1>%s</h1>
                        <h2>Por: %s</h2>
                        <h3>Sumário</h3>
                        <ul>
                            %s
                        </ul>
                    </body>
                </html>
                """.formatted(ebook.titulo(), ebook.titulo(), ebook.autor(), linksSumarioHTML);
        Path arquivoIndex = diretorioHTML.resolve("index.html");
        Files.writeString(arquivoIndex, sumarioHTML, StandardCharsets.UTF_8);
    }

    private String obterNomeArquivoHTML(int i, Capitulo capitulo) {
        String tituloLimpo = capitulo.titulo().toLowerCase().toLowerCase().replaceAll("\\W", "");
        return "%02d-%s.html".formatted(i, tituloLimpo);
    }
}
