package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.nio.file.Path;
import java.util.List;

@ApplicationScoped
public class CotubaService {

    private final RenderizadorMarkdown renderizadorMarkdown;
    private final LeitorPropriedadesEbook leitorPropriedadesEbook;
    private final RepositorioMarkdowns repositoriosMarkdowns;
    private final Instance<GeradorEbook> geradoresEbook;

    @Inject
    public CotubaService(RenderizadorMarkdown renderizadorMarkdown,
                         LeitorPropriedadesEbook leitorPropriedadesEbook,
                         RepositorioMarkdowns repositoriosMarkdowns,
                         @Any Instance<GeradorEbook> geradoresEbook) {
        this.renderizadorMarkdown = renderizadorMarkdown;
        this.leitorPropriedadesEbook = leitorPropriedadesEbook;
        this.repositoriosMarkdowns = repositoriosMarkdowns;
        this.geradoresEbook = geradoresEbook;
    }

    public void executar(ParametrosCotuba parametrosCotuba) {
        Path diretorioMD = parametrosCotuba.diretorioMD();
        List<Markdown> markdowns = repositoriosMarkdowns.buscar(diretorioMD);
        List<Capitulo> capitulos = renderizadorMarkdown.renderizar(markdowns);



        var propriedadesEbook = leitorPropriedadesEbook.ler(parametrosCotuba.diretorioMD());

        var ebook = EbookBuilder.builder()
                .titulo("Livro")
                .autor("Autor")
                .capitulos(capitulos)
                .formato(parametrosCotuba.formato())
                .arquivoSaida(parametrosCotuba.arquivoSaida())
                .titulo(propriedadesEbook.titulo())
                .autor(propriedadesEbook.autor())
                .build();

        GeradorEbook geradorEbook = geradoresEbook.select(FormatoEbookFilter.of(ebook.formato())).get();
        geradorEbook.gerar(ebook);
    }
}
