package br.com.fiap.newmottugestor.moto;
import br.com.fiap.newmottugestor.Leitor.Leitor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMoto;

    @NotBlank(message = "{moto.placa.notblank}")
    @Pattern(regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$",message = "{moto.placa.pattern}")
    private String placa;

    @NotBlank(message = "{moto.modelo.notblank}")
    private String modelo;

    @NotBlank(message = "{moto.rfid.notblank}")
    private String rfid_tag;

    @PastOrPresent(message = "{moto.data.partorpresent}")
    private LocalDate dataCadastro;

    @NotBlank(message = "{moto.servico.notblank}")
    private String servico;

    @ManyToOne
    @JsonIgnore
    private Leitor leitor;
}
