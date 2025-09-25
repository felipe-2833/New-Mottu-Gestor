package br.com.fiap.newmottugestor.movimento;

import br.com.fiap.newmottugestor.enums.TipoMovimento;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class MovimentoSpecification {

    public static Specification<Movimento> comPatio(Long idPatio) {
        return (root, query, cb) -> cb.equal(root.get("patio").get("idPatio"), idPatio);
    }

    public static Specification<Movimento> comLeitor(Long idLeitor) {
        return (root, query, cb) -> cb.equal(root.get("leitor").get("idLeitor"), idLeitor);
    }

    public static Specification<Movimento> comTipo(TipoMovimento tipo) {
        return (root, query, cb) -> cb.equal(root.get("tipoMovimento"), tipo);
    }

    public static Specification<Movimento> comData(LocalDate data) {
        return (root, query, cb) -> cb.equal(root.get("dataEvento"), data);
    }

    public static Specification<Movimento> comModelo(String modelo) {
        String pattern = "%" + modelo.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("moto").get("modelo")), pattern);
    }
}
