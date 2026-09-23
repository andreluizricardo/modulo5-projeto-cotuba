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
        Path diretorioMD = parametrosCotuba.getDiretorioMD();
        List<Capitulo> capitulos = repositoriosMarkdowns.buscar(diretorioMD);
        renderizadorMarkdown.renderizar(capitulos);

        var ebook = new Ebook();

        leitorPropriedadesEbook.ler(parametrosCotuba.getDiretorioMD(), ebook);

        //TODO: definir título e autor para o livro
        ebook.setTitulo("Livro");
        ebook.setAutor("Autor");

        ebook.setCapitulos(capitulos);
        ebook.setFormato(parametrosCotuba.getFormato());
        ebook.setArquivoSaida(parametrosCotuba.getArquivoSaida());

        FormatoEbook formato = ebook.getFormato();
        GeradorEbook geradorEbook = geradoresEbook.select(FormatoEbookFilter.of(formato)).get();

        geradorEbook.gerar(ebook);
    }
}
