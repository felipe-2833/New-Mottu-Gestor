package br.com.fiap.newmottugestor.oracle.model;

import br.com.fiap.newmottugestor.enums.TipoStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Leitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLeitor;

    @NotBlank(message = "{leitor.nome.notblank}")
    private String nome;

    @NotNull(message = "{leitor.tipostatus.notnull}")
    @Enumerated(EnumType.STRING)
    private TipoStatus status;

    @ManyToOne
    @JsonIgnore
    private Patio patio;
}
