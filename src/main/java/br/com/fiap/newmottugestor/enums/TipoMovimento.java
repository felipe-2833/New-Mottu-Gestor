package br.com.fiap.newmottugestor.enums;

import lombok.Getter;

public enum TipoMovimento {
    ENTRADA("E"),
    SAIDA("S"),
    REALOCACAO("R"),
    MANUTENCAO("M"),
    VISTORIA("V");

    @Getter
    private final String codigo;

    TipoMovimento(String codigo) {
        this.codigo = codigo;
    }
}
