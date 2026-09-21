package br.com.unipds;

import java.nio.file.Path;

public class ParametrosCotuba {

    private Path diretorioMD;
    private FormatoEbook formato;
    private Path arquivoSaida;
    private boolean modoVerboso = false;

    public Path getDiretorioMD() {
        return diretorioMD;
    }

    public void setDiretorioMD(Path diretorioMD) {
        this.diretorioMD = diretorioMD;
    }

    public FormatoEbook getFormato() {
        return formato;
    }

    public void setFormato(FormatoEbook formato) {
        this.formato = formato;
    }

    public Path getArquivoSaida() {
        return arquivoSaida;
    }

    public void setArquivoSaida(Path arquivoSaida) {
        this.arquivoSaida = arquivoSaida;
    }

    public boolean isModoVerboso() {
        return modoVerboso;
    }

    public void setModoVerboso(boolean modoVerboso) {
        this.modoVerboso = modoVerboso;
    }
}
