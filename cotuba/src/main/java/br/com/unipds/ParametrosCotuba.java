package br.com.unipds;

import java.nio.file.Path;

public record ParametrosCotuba(Path diretorioMD, FormatoEbook formato, Path arquivoSaida, boolean modoVerboso) {

    ParametrosCotuba (Path diretorioMD, FormatoEbook formato, Path arquivoSaida){
        this(diretorioMD, formato, arquivoSaida, false);
    }
}
