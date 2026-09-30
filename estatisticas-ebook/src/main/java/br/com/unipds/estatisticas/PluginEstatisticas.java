package br.com.unipds.estatisticas;

import br.com.unipds.cotuba.domain.Capitulo;
import br.com.unipds.cotuba.domain.Ebook;
import br.com.unipds.cotuba.plugin.Plugin;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Map;

public class PluginEstatisticas implements Plugin {
    @Override
    public String aposRenderizacao(String html) {
        return "";
    }

    @Override
    public void aposGeracao(Ebook ebook) {

        var contadorPalavras = new ContadorPalavras();

        for (Capitulo capitulo : ebook.capitulos()) {
            String html = capitulo.html();
            Document doc = Jsoup.parseBodyFragment(html);
            String textoCapitulo = doc.text().toLowerCase();
            textoCapitulo = textoCapitulo.replaceAll("\\p{Punct}", "");
            String[] palavras = textoCapitulo.split("\\s+");

            for(String palavra : palavras) {
                contadorPalavras.adicionarPalavra(palavra);
            }
        }
        for (ContadorPalavras.ContagemPalavra contagem : contadorPalavras) {
            System.out.printf("'%s' : %d \n", contagem.palavra(), contagem.ocorrencias());
        }
    }
}
