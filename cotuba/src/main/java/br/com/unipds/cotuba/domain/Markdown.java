package br.com.unipds.cotuba.domain;

import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

import java.nio.file.Path;

@Entity
public record Markdown(@Identity String nome, String conteudo) {
}
