package br.com.fiap.newmottugestor.mongo.model;

import br.com.fiap.newmottugestor.enums.TipoMovimento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "movimentacoes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoDocument {

    @Id
    private String id;

    private LocalDate dataEvento;
    private Long idMotoOracle;
    private String placaMoto;
    private String modeloMoto;
    private String nomeUsuario;
    private String emailUsuario;
    private String nomePatio;
    private String nomeLeitor;
    private TipoMovimento tipoMovimento;

}