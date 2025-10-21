package br.com.fiap.newmottugestor.oracle.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "movimentacao_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimentacao_log")
    private Long id;

    @Column(name = "id_mongo_movimentacao")
    private String idMongoMovimentacao;

    @Column(name = "data_log", insertable = false, updatable = false)
    private LocalDate dataLog;

    @ManyToOne
    @JoinColumn(name = "id_moto")
    private Moto moto;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_tipo_movimentacao")
    private TipoMovimentacao tipoMovimentacao;
}