package br.com.fiap.newmottugestor.patio;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPatio;

    @NotBlank(message = "{patio.nome.notnull}")
    private String nome;

    @NotBlank(message = "{patio.endereco.notnull}")
    private String endereco;

    @Positive(message = "{patio.capacidade.positive}")
    private Double capacidade;
}
