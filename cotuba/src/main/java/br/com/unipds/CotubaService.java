package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.nio.file.Path;
import java.util.List;

@ApplicationScoped
public class CotubaService {

    private final RenderizadorMarkdown renderizadorMarkdown;
    private final LeitorPropriedadesEbook leitorPropriedadesEbook;
    private final RepositorioMarkdowns repositoriosMarkdowns;
    private final GeradorEbook geradorEPUB;
    private final GeradorEbook geradorPDF;
    @Inject
    public CotubaService(RenderizadorMarkdown renderizadorMarkdown,
                         LeitorPropriedadesEbook leitorPropriedadesEbook,
                         RepositorioMarkdowns repositoriosMarkdowns,
                         @Named("geradorEPUB") GeradorEbook geradorEPUB,
                         @Named("geradorPDF") GeradorEbook geradorPDF) {
        this.renderizadorMarkdown = renderizadorMarkdown;
        this.leitorPropriedadesEbook = leitorPropriedadesEbook;
        this.repositoriosMarkdowns = repositoriosMarkdowns;
        this.geradorEPUB = geradorEPUB;
        this.geradorPDF = geradorPDF;
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

        GeradorEbook geradorEbook;
        if (FormatoEbook.PDF.equals(ebook.getFormato())) {
            geradorEbook = geradorPDF;
        } else if (FormatoEbook.EPUB.equals(ebook.getFormato())) {
            geradorEbook = geradorEPUB;
        } else {
            throw new IllegalArgumentException("Formato do ebook inválido: " + parametrosCotuba.getFormato());
        }

        geradorEbook.gerar(ebook);
    }
}
