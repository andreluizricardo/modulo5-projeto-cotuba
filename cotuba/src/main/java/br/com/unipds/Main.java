package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

public class Main {

    void main(String[] args) {
        int exitCode = executar(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    int executar(String[] args) {
        boolean modoVerboso = true;

        try {
            LeitorOpcoesCLI leitorOpcoesCLI = new LeitorOpcoesCLI();
            leitorOpcoesCLI.ler(args);
            Path diretorioMD = leitorOpcoesCLI.getDiretorioMD();
            String formato = leitorOpcoesCLI.getFormato();
            Path arquivoSaida = leitorOpcoesCLI.getArquivoSaida();
            modoVerboso = leitorOpcoesCLI.isModoVerboso();

            var renderizadorMarkdown = new RenderizadorMarkdown();

            List<String> hmls = renderizadorMarkdown.renderizar(diretorioMD);

            if ("pdf".equals(formato)) {
                var geradorPdf = new GeradorPDF();
                geradorPdf.gerarPDF(hmls, arquivoSaida);

            } else if ("epub".equals(formato)) {
                var geradorEpub = new GeradorEPUB();
                geradorEpub.gerarEpub(hmls, arquivoSaida);

            } else {
                throw new IllegalArgumentException("Formato do ebook inválido: " + formato);
            }

            System.out.println("Arquivo gerado com sucesso: " + arquivoSaida);
            return 0;

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            if (modoVerboso) {
                System.err.println();
                ex.printStackTrace();
            }
            return 1;
        }
    }

}