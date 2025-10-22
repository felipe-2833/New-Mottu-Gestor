package br.com.fiap.newmottugestor.oracle.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "tipo_movimentacao")
@Data
@AllArgsConstructor
@Builder
public class TipoMovimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_movimentacao")
    private Long id;

    @Column(name = "nome")
    private String nome;

    public TipoMovimentacao() {}
}