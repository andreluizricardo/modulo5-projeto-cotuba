package br.com.unipds;

import org.apache.commons.cli.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class LeitorOpcoesCLI {

    public ParametrosCotuba ler(String[] args) {
        var options = new Options();

        var opcaoDeDiretorioDosMD = new Option("d", "dir", true,
                "Diretório que contém os arquivos md. Default: diretório atual.");
        options.addOption(opcaoDeDiretorioDosMD);

        var opcaoDeFormatoDoEbook = new Option("f", "format", true,
                "Formato de saída do ebook. Pode ser: pdf ou epub. Default: pdf");
        options.addOption(opcaoDeFormatoDoEbook);

        var opcaoDeArquivoDeSaida = new Option("o", "output", true,
                "Arquivo de saída do ebook. Default: book.{formato}.");
        options.addOption(opcaoDeArquivoDeSaida);

        var opcaoModoVerboso = new Option("v", "verbose", false,
                "Habilita modo verboso.");
        options.addOption(opcaoModoVerboso);

        CommandLineParser cmdParser = new DefaultParser();
        var ajuda = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = cmdParser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            ajuda.printHelp("cotuba", options);
            throw new IllegalStateException(e);
        }

        try {

            Path diretorioMD;
            FormatoEbook formato;
            Path arquivoSaida;
            boolean modoVerboso = false;

            var parametrosCotuba = new ParametrosCotuba();

            String nomeDoDiretorioDosMD = cmd.getOptionValue("dir");

            if (nomeDoDiretorioDosMD != null) {
                diretorioMD = Paths.get(nomeDoDiretorioDosMD);
                if (!Files.isDirectory(diretorioMD)) {
                    throw new IllegalArgumentException(nomeDoDiretorioDosMD + " não é um diretório.");
                }
            } else {
                Path diretorioAtual = Paths.get("");
                diretorioMD = diretorioAtual;
            }

            String nomeDoFormatoDoEbook = cmd.getOptionValue("format");

            if (nomeDoFormatoDoEbook != null) {
                try {
                     formato = FormatoEbook.valueOf(nomeDoFormatoDoEbook.toUpperCase());
                } catch (IllegalArgumentException ex) {
                    throw new IllegalArgumentException("Formato do ebook inválido: " + nomeDoFormatoDoEbook, ex);
                }
            } else {
                formato = FormatoEbook.PDF;
            }

            String nomeDoArquivoDeSaidaDoEbook = cmd.getOptionValue("output");
            if (nomeDoArquivoDeSaidaDoEbook != null) {
                arquivoSaida = Paths.get(nomeDoArquivoDeSaidaDoEbook);
            } else {
                arquivoSaida = Paths.get("book." + formato.name().toLowerCase());
            }
            if (Files.isDirectory(arquivoSaida)) {
                // deleta arquivos do diretório recursivamente
                Files.walk(arquivoSaida).sorted(Comparator.reverseOrder())
                        .map(Path::toFile).forEach(File::delete);
            } else {
                Files.deleteIfExists(arquivoSaida);
            }

            modoVerboso = cmd.hasOption("verbose");

            parametrosCotuba.setDiretorioMD(diretorioMD);
            parametrosCotuba.setFormato(formato);
            parametrosCotuba.setArquivoSaida(arquivoSaida);
            parametrosCotuba.setModoVerboso(modoVerboso);

            return parametrosCotuba;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
