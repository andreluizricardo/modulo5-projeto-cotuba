package br.com.unipds;

import java.util.List;

public class CotubaService {
    public void executar(ParametrosCotuba parametrosCotuba) {

        var renderizadorMarkdown = new RenderizadorMarkdown();
        List<Capitulo> capitulos = renderizadorMarkdown.renderizar(parametrosCotuba.getDiretorioMD());

        var ebook = new Ebook();

        var leitorPropriedadesEbook = new LeitorPropriedadesEbook();

        leitorPropriedadesEbook.ler(parametrosCotuba.getDiretorioMD(), ebook);

        //TODO: definir título e autor para o livro
        ebook.setTitulo("Livro");
        ebook.setAutor("Autor");

        ebook.setCapitulos(capitulos);
        ebook.setFormato(parametrosCotuba.getFormato());
        ebook.setArquivoSaida(parametrosCotuba.getArquivoSaida());

        if (FormatoEbook.PDF.equals(ebook.getFormato())) {
            var geradorPdf = new GeradorPDF();
            geradorPdf.gerarPDF(ebook);

        } else if (FormatoEbook.EPUB.equals(ebook.getFormato())) {
            var geradorEpub = new GeradorEPUB();
            geradorEpub.gerarEpub(ebook);
        } else {
            throw new IllegalArgumentException("Formato do ebook inválido: " + parametrosCotuba.getFormato());
        }
    }
}
