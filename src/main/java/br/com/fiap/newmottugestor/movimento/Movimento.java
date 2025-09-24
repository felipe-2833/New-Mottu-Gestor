package br.com.fiap.newmottugestor.movimento;
import java.time.LocalDate;

import br.com.fiap.newmottugestor.Leitor.Leitor;
import br.com.fiap.newmottugestor.enums.TipoMovimento;
import br.com.fiap.newmottugestor.moto.Moto;
import br.com.fiap.newmottugestor.patio.Patio;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMoviment;

    @PastOrPresent(message = "{movimento.data.pastorpresent}")
    private LocalDate dataEvento;

    @ManyToOne
    @JsonIgnore
    private Patio patio;

    @ManyToOne
    @JsonIgnore
    private Leitor leitor;

    @ManyToOne
    @JsonIgnore
    private Moto moto;

    @NotNull(message = "{movimento.tipomovimento.notnull}")
    @Enumerated(EnumType.STRING)
    private TipoMovimento tipoMovimento;
}
