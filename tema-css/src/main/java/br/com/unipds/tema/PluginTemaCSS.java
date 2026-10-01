package br.com.unipds.tema;

import br.com.unipds.cotuba.domain.Ebook;
import br.com.unipds.cotuba.plugin.PluginAposRenderizacao;

public class PluginTemaCSS implements PluginAposRenderizacao {

    @Override
    public String aposRenderizacao(String html) {
        return """
                <style>
                    h1 {
                        border-bottom: 1px dashed black;
                        font-size: 3em;
                        font-weight: bolder;
                        font-variant-caps: small-caps;
                    }
                    h2 {
                        border-left: 1px solid black;
                        padding-left: 5px;
                        border-bottom: 1px solid black
                    }
                </style>
                %s
                """.formatted(html);
    }

}
