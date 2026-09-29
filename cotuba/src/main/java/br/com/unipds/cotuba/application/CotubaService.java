package br.com.unipds.cotuba.application;

import br.com.unipds.cotuba.domain.EbookBuilder;
import br.com.unipds.cotuba.domain.Capitulo;
import br.com.unipds.cotuba.domain.Markdown;
import br.com.unipds.cotuba.dto.ParametrosCotuba;
import br.com.unipds.cotuba.plugin.Plugin;
import br.com.unipds.cotuba.ports.in.CotubaUseCase;
import br.com.unipds.cotuba.ports.out.GeradorEbook;
import br.com.unipds.cotuba.ports.out.LeitorPropriedadesEbook;
import br.com.unipds.cotuba.ports.out.RenderizadorMarkdown;
import br.com.unipds.cotuba.ports.out.RepositorioMarkdowns;
import br.com.unipds.cotuba.support.FormatoEbookFilter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.jmolecules.ddd.annotation.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.ServiceLoader;

@Service
@ApplicationScoped
public class CotubaService implements CotubaUseCase {

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

    @Override
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
                .titulo(propriedadesEbook.titulo())
                .autor(propriedadesEbook.autor())
                .build();

        GeradorEbook geradorEbook = geradoresEbook.select(FormatoEbookFilter.of(ebook.formato())).get();
        geradorEbook.gerar(ebook, parametrosCotuba.arquivoSaida());

        for (Plugin plugin : ServiceLoader.load(Plugin.class)){
            plugin.aposGeracao(ebook);
        }
    }
}
