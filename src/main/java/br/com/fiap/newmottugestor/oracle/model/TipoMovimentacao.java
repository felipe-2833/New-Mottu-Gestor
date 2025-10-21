package br.com.fiap.newmottugestor.oracle.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tipo_movimentacao")
@Data
public class TipoMovimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_movimentacao")
    private Long id;

    @Column(name = "nome")
    private String nome;
}