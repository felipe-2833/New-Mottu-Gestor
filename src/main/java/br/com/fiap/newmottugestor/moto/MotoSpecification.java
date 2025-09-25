package br.com.fiap.newmottugestor.moto;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class MotoSpecification {

    public static Specification<Moto> comLeitor(Long idLeitor) {
        return (root, query, cb) -> cb.equal(root.get("leitor").get("idLeitor"), idLeitor);
    }

    public static Specification<Moto> comData(LocalDate data) {
        return (root, query, cb) -> cb.equal(root.get("dataCadastro"), data);
    }

    public static Specification<Moto> comModelo(String modelo) {
        return (root, query, cb) -> cb.equal(root.get("modelo"), modelo);
    }

    public static Specification<Moto> comPlaca(String placa) {
        return (root, query, cb) -> cb.equal(root.get("placa"), placa);
    }
}
